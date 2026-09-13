package java_cup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lowers the sugar-preserving spec tree (the generated Node* classes) into
 * the internal grammar graph.
 *
 * <p>This class is a verbatim transplant of the semantic actions that used to
 * live in parser.cup's action-code block (add_lab / add_rhs_part /
 * handle_rhs_expr / the declaration actions ...). The discipline of this
 * transplant is <b>order fidelity</b>: Production and non_terminal objects
 * must be created in exactly the sequence the parse-time actions produced,
 * because their numbering decides the case-table layout of the generated
 * parser. The tree is walked in strict source order, which equals the old
 * reduction order since every list rule of the meta grammar is
 * left-recursive.</p>
 *
 * <p>Historic quirks are preserved on purpose (documented in REFACTORING.md):
 * {@code %prec X %namer N} drops the namer, and an undeclared {@code %prec}
 * terminal still crashes with an NPE exactly as before. Anonymous
 * expressions expand per use site: one fresh hidden non-terminal with one
 * real production per branch, branch labels and trailing actions included,
 * so plain LALR dispatches between the branches -- no runtime machinery.</p>
 */
public final class Lowering {

    /** Whether -debugsymbols was requested (debug markers on user code). */
    private final boolean debugSymbols;

    /** Records the tree-to-graph correlation while lowering. */
    private final SpecCorrelation corr;

    /**
     * The right-hand side (top-level alternative or anonymous branch) the
     * next constructed Production belongs to; cleared once recorded. Lets
     * handle_rhs_expr stamp the production into the correlation.
     */
    private NodeRhs pendingRhs = null;

    /** The four section kinds of a code part. */
    private enum CodeKind { ACTION, PARSER, INIT, SCAN }

    /*---------------------------------------------------------------*/
    /* Ported instance state (was the parser.cup action-code block)  */
    /*---------------------------------------------------------------*/

    /** Max size of right hand side we will support. */
    private static final int MAX_RHS = 200;

    /** Array for accumulating right hand side parts. */
    private final production_part[] rhs_parts = new production_part[MAX_RHS + 1];

    /** Where we are currently in building a right hand side. */
    private int rhs_pos = 0;

    /** Table of declared symbols -- contains production parts indexed by name. */
    private final Map<String, production_part> symbols = new HashMap<>();

    /** Table of just non terminals -- contains non_terminals indexed by name. */
    private final Map<String, non_terminal> non_terms = new HashMap<>();

    /** Declared start non_terminal. */
    private non_terminal start_nt = null;

    /** Left hand side non terminal of the current production. */
    private non_terminal lhs_nt = null;

    /** Current precedence number. */
    private int _cur_prec = 0;

    /** Current precedence side. */
    private int _cur_side = assoc.no_prec;

    /**
     * Whether we are currently between the parentheses of an anonymous
     * expression -- the nesting guard (anonymous expressions cannot nest).
     */
    private boolean inAnon = false;

    /** Shared id counter for debug symbols on code parts and embedded actions. */
    private int cur_debug_id = 0;

    public Lowering(boolean debugSymbols, SpecCorrelation corr) {
        this.debugSymbols = debugSymbols;
        this.corr = corr;
        corr.symbols = symbols;
    }

    /*---------------------------------------------------------------*/
    /* Entry point                                                    */
    /*---------------------------------------------------------------*/

    /**
     * Lowers a whole specification. Sections are processed in grammar
     * section order (package, imports, class, code parts, symbols,
     * precedence, start, productions), which is exactly the order the
     * parse-time actions ran in.
     */
    public void run(NodeSpec spec) throws Exception {
        /* declare "error" as a terminal and the start non terminal --
           the old position-0 action of the spec rule */
        symbols.put("error", new symbol_part(terminal.error));
        non_terms.put("$START", non_terminal.START_nt);

        String packageName = Tree.str(spec.getPkg());
        if (packageName != null) {
            emit.package_name = packageName;
        }
        for (var impNode : Tree.elems(spec.getImports())) {
            var imp = (NodeImportSpec) impNode;
            emit.import_list.push(imp.getSt() != null
                    ? " static " + Tree.str(imp.getTarget())
                    : Tree.str(imp.getTarget()));
        }
        String className = Tree.str(spec.getCls());
        if (className != null) {
            emit.parser_class_name = className;
            emit.symbol_const_class_name = className + "Sym";
        }
        for (var cpNode : Tree.elems(spec.getCodes())) {
            lowerCodePart((NodeCodePart) cpNode);
        }
        for (var sdNode : Tree.elems(spec.getSyms())) {
            lowerSymbolDecl((NodeSymbol) sdNode);
        }
        for (var pNode : Tree.elems(spec.getPrecs())) {
            lowerPrecedence((NodePreced) pNode);
        }
        lowerStart(Tree.str(spec.getStart()));
        for (var prodNode : Tree.elems(spec.getProds())) {
            lowerProduction((NodeProduction) prodNode);
        }
    }

    /*---------------------------------------------------------------*/
    /* Directive sections                                             */
    /*---------------------------------------------------------------*/

    private void lowerCodePart(NodeCodePart cp) {
        /* which keyword introduced the part discriminates the kind */
        CodeKind kind = cp.getA() != null ? CodeKind.ACTION
                : cp.getP() != null ? CodeKind.PARSER
                : cp.getI() != null ? CodeKind.INIT
                : cp.getSc() != null ? CodeKind.SCAN
                : null;
        if (kind == null) return; // error-recovery sentinel

        String current;
        String redundantMessage;
        switch (kind) {
            case ACTION:
                current = emit.action_code;
                redundantMessage = "Redundant action code (skipping)";
                break;
            case PARSER:
                current = emit.parser_code;
                redundantMessage = "Redundant parser code (skipping)";
                break;
            case INIT:
                current = emit.init_code;
                redundantMessage = "Redundant init code (skipping)";
                break;
            case SCAN:
                current = emit.scan_code;
                redundantMessage = "Redundant scan code (skipping)";
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(kind));
        }
        if (current != null) {
            ErrorManager.getManager().emit_warning(redundantMessage);
            return;
        }
        String decorated = attach_debug_symbol(get_new_debug_id(), Tree.str(cp.getCode()));
        switch (kind) {
            case ACTION: emit.action_code = decorated; break;
            case PARSER: emit.parser_code = decorated; break;
            case INIT:   emit.init_code = decorated; break;
            case SCAN:   emit.scan_code = decorated; break;
        }
    }

    private void lowerSymbolDecl(NodeSymbol sd) throws internal_error {
        boolean isTerminal = sd.getTkw() != null;
        String type = Tree.str(sd.getTy());
        if (type == null) type = "Object";
        for (var nameNode : Tree.elems(sd.getNames())) {
            String name = Tree.str(((NodeIdRef) nameNode).getTheId());
            if (symbols.get(name) != null) {
                ErrorManager.getManager().emit_error(
                        "java_cup.runtime.Symbol \"" + name + "\" has already been declared");
            } else if (isTerminal) {
                symbols.put(name, new symbol_part(new terminal(name, type)));
            } else {
                non_terminal this_nt = new non_terminal(name, type);
                non_terms.put(name, this_nt);
                symbols.put(name, new symbol_part(this_nt));
            }
        }
    }

    private void lowerPrecedence(NodePreced p) throws Exception {
        /* the old mid-rule action: update_precedence(p.side) */
        _cur_side = p.getL() != null ? assoc.left
                : p.getR() != null ? assoc.right
                : p.getN() != null ? assoc.nonassoc
                : assoc.no_prec;
        _cur_prec++;
        for (var refNode : Tree.elems(p.getRefs())) {
            add_precedence(lowerTermId((NodeSymbolId) refNode));
        }
    }

    private void lowerStart(String startName) throws Exception {
        if (startName == null) {
            return;
        }
        /* verify that the name has been declared as a non terminal */
        non_terminal nt = (non_terminal) non_terms.get(startName);
        if (nt == null) {
            ErrorManager.getManager().emit_error(
                    "Start non terminal \"" + startName + "\" has not been declared");
        } else {
            /* remember the non-terminal for later */
            start_nt = nt;

            /* build a special start production */
            new_rhs();
            add_rhs_part(add_lab(new symbol_part(start_nt), "start_val"));
            add_rhs_part(new symbol_part(terminal.EOF));
            add_rhs_part(new action_part("RESULT = start_val;"));
            emit.start_production =
                    new Production(non_terminal.START_nt, rhs_parts, rhs_pos);
            new_rhs();
        }
    }

    /*---------------------------------------------------------------*/
    /* Productions                                                    */
    /*---------------------------------------------------------------*/

    private void lowerProduction(NodeProduction pn) throws Exception {
        String lhsName = Tree.str(pn.getLhs());
        if (lhsName == null) {
            /* production ::= error SEMI */
            ErrorManager.getManager().emit_error("Syntax Error");
            return;
        }
        /* lookup the lhs nt */
        lhs_nt = (non_terminal) non_terms.get(lhsName);

        /* if it wasn't declared, emit a message */
        if (lhs_nt == null) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_warning(
                        "LHS non terminal \"" + lhsName + "\" has not been declared");
            }
        }

        /* reset the rhs accumulation */
        new_rhs();
        for (var rnNode : Tree.elems(pn.getAlts())) {
            lowerRhs((NodeRhs) rnNode);
        }
    }

    private void lowerRhs(NodeRhs rn) throws Exception {
        if (lhs_nt != null) corr.addAlternative(lhs_nt, rn);
        for (var partNode : Tree.elems(rn.getParts())) {
            lowerPart((NodeProdPart) partNode);
        }
        pendingRhs = rn;
        /* the %prec terminal is checked after all parts, before the rhs
           action runs (that is when the old term_id action reduced) */
        if (rn.getPrec() != null) {
            /* historical quirk: `prod_part_list %prec T %namer N` calls
               handle_rhs_expr with is_namer = false, dropping the namer */
            handle_rhs_expr(true, lowerTermId(rn.getPrec()), false, null);
        } else if (rn.getNamer() != null) {
            handle_rhs_expr(false, null, true, Tree.str(rn.getNamer()));
        } else {
            handle_rhs_expr(false, null, false, null);
        }
    }

    /**
     * Ports the prod_part action for one part. Event order inside a single
     * part matches the old reduction order: the anonymous expression of the
     * symbol reference lowers first (symbol_id), then the quantifier's
     * separator list resolves (opt_quantifier), then the symbol lookup,
     * validations and quantifier desugaring run (the prod_part action).
     */
    private void lowerPart(NodeProdPart part) throws Exception {
        if (part.getCodeStr() != null) {
            /* add a new production part */
            add_rhs_part(new action_part(
                    attach_debug_symbol(get_new_debug_id(), Tree.str(part.getCodeStr()))));
            return;
        }
        var symid = part.getSymid();

        /* symbol_id: an anonymous expression lowers here, a plain name is
           just a name (the old ObjectPair first/second) */
        String symName;
        String symType;
        var anon = symid.getAnon();
        if (anon != null) {
            symName = lowerAnonExpr(anon);
            symType = Tree.str(anon.getType());
        } else {
            symName = Tree.str(symid.getTheId());
            symType = null;
        }

        /* opt_quantifier: build the SymQuantifier, resolving the bracket
           separator list (each separator may itself be an anon expression) */
        SymQuantifier quantifier = null;
        var quant = part.getQuant();
        if (quant != null) {
            if (quant.getQ() != null) {
                /* X? */
                quantifier = new SymQuantifier();
            } else if (quant.getSeps() == null) {
                /* X* / X+ */
                quantifier = new SymQuantifier(null, "*".equals(Tree.str(quant.getSp())), false);
            } else {
                /* [SEP...] !|*  /  [SEP...] !|+ */
                List<production_part> partList = new ArrayList<>();
                for (var sepNode : Tree.elems(quant.getSeps())) {
                    production_part p = symbols.get(lowerSymRef((NodeSymbolId) sepNode));
                    partList.add(p);
                }
                quantifier = new SymQuantifier(partList,
                        "*".equals(Tree.str(quant.getSp())),
                        "?".equals(Tree.str(quant.getQe())));
            }
        }

        /* try to look up the id */
        production_part symb = symbols.get(symName);

        /* if that fails, symbol is undeclared */
        if (symb == null) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_error(
                        "java_cup.runtime.Symbol \"" + symName + "\" has not been declared");
            }
        } else if (quantifier != null && symb.is_action()) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_error(
                        "Action symbol \"" + symName + "\" cannot be used with a quantifier.");
            }
        } else if (part.hasS() && symb.is_action()) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_error(
                        "Action symbol \"" + symName + "\" cannot use '... 'operator");
            }
        } else if (part.hasS() && quantifier != null && quantifier.isList()) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_error(
                        "List symbol \"" + symName + "\" cannot use '... 'operator");
            }
        } else {
            if (quantifier != null) {
                if (!quantifier.isList()) {
                    // ?
                    non_terminal newNt = ((symbol_part) symb).the_symbol().createOptBox(symbols, null);
                    symb = new symbol_part(newNt, null, symType);
                } else {
                    non_terminal newNt = ((symbol_part) symb).the_symbol().createListBox(
                            symbols, quantifier.getPartList(), quantifier.isAllowTail(), quantifier.isAllowEmpty()
                    );
                    symb = new symbol_part(newNt, null, symType == null ? null : emit.buildListExpr(symType));
                }
            } else if (symType != null) {
                symb = new symbol_part(((symbol_part) symb).the_symbol(), null, symType);
            }
            /* add a labeled production part */
            production_part p = add_lab(symb, Tree.str(part.getLabid()));
            if (part.hasS()) ((symbol_part) p).markInline();
            add_rhs_part(p);
        }
    }

    /*---------------------------------------------------------------*/
    /* Anonymous expressions                                          */
    /*---------------------------------------------------------------*/

    /**
     * Lowers one anonymous-expression use site: a fresh hidden non-terminal
     * with one real production per branch. Branch labels stay on the parts
     * and a trailing branch action stays the production's tail action, so
     * plain LALR dispatches between the branches -- no runtime machinery.
     * Every use site gets its own hidden NT (no shape memoization): sharing
     * one NT across use sites is incompatible with per-use-site branch
     * labels. Returns the hidden NT's name.
     */
    private String lowerAnonExpr(NodeAnonExpr ae) throws Exception {
        if (inAnon) {
            throw new internal_error("Anonymous non-terminals cannot be nested");
        }
        /* degenerate use sites (e.g. inside a precedence declaration before
           any production): nothing was ever created for these */
        if (lhs_nt == null) {
            return null;
        }
        /* shelve the enclosing rhs accumulation so the branch lowering below
           does not clobber it */
        production_part[] shelved = new production_part[rhs_pos];
        if (rhs_pos != 0) {
            System.arraycopy(rhs_parts, 0, shelved, 0, rhs_pos);
        }
        int shelvedPos = rhs_pos;
        new_rhs();

        non_terminal hidden = non_terminal.create_new(
                "_EBNF_", Main.ast_format == null ? "Object" : "AstNode");
        symbols.put(hidden.name(), new symbol_part(hidden));
        corr.recordAnon(ae, hidden);

        inAnon = true;
        try {
            for (var branchNode : Tree.elems(ae.getBranches())) {
                var branch = (NodeRhs) branchNode;
                new_rhs();
                for (var partNode : Tree.elems(branch.getParts())) {
                    lowerPart((NodeProdPart) partNode);
                }
                finishBranchProduction(hidden, branch);
            }
        } finally {
            inAnon = false;
        }

        /* restore the enclosing rhs accumulation */
        if (shelvedPos != 0) {
            System.arraycopy(shelved, 0, rhs_parts, 0, shelvedPos);
        }
        rhs_pos = shelvedPos;
        return hidden.name();
    }

    /**
     * Constructs the production for one branch of an anonymous expression:
     * %prec / %namer are honored exactly like a top-level alternative
     * (including the historical quirk that {@code %prec T %namer N} drops
     * the namer).
     */
    private void finishBranchProduction(non_terminal hidden, NodeRhs branch) throws Exception {
        String termName = branch.getPrec() == null ? null : lowerTermId(branch.getPrec());
        GrammarSymbol precSym = termName == null
                ? null
                : ((symbol_part) symbols.get(termName)).the_symbol();

        /* non-AST default: a branch that is exactly one symbol and carries
           no action passes that symbol's value through (the opt-box
           precedent); every other shape without a user action produces no
           value. In -ast mode the Production constructor's auto action
           builds the node instead. */
        if (Main.ast_format == null && rhs_pos == 1 && !rhs_parts[0].is_action()) {
            var sole = (symbol_part) rhs_parts[0];
            add_rhs_part(new action_part(
                    "RESULT = " + emit.buildStackValueReader(sole.the_symbol().stack_type(), 0) + ";"));
        }

        Production p = buildProductionCore(
                hidden, precSym, termName,
                branch.getNamer() != null && branch.getPrec() == null,
                Tree.str(branch.getNamer()));
        corr.recordRhs(branch, p);
        new_rhs();
    }

    /**
     * Resolves a symbol reference at a point of use (separator list, %prec
     * target, precedence entry). Anonymous expressions lower in place,
     * exactly where the old symbol_id action ran.
     */
    private String lowerSymRef(NodeSymbolId ref) throws Exception {
        var anon = ref.getAnon();
        if (anon != null) {
            return lowerAnonExpr(anon);
        }
        return Tree.str(ref.getTheId());
    }

    /*---------------------------------------------------------------*/
    /* Verbatim ports of the old action-code helpers                  */
    /*---------------------------------------------------------------*/

    /** check that the symbol_id is a terminal (ports term_id). */
    private String lowerTermId(NodeSymbolId ref) throws Exception {
        String name = lowerSymRef(ref);
        if (symbols.get(name) == null) {
            /* issue a message */
            ErrorManager.getManager().emit_error(
                    "Terminal \"" + name + "\" has not been declared");
        }
        return name;
    }

    /** helper routine to clone a new production part adding a given label */
    private production_part add_lab(production_part part, String lab)
            throws internal_error {
        /* if there is no label, or this is an action, just return the original */
        if (lab == null || part.is_action()) return part;
        var symPart = (symbol_part) part;

        /* otherwise build a new one with the given label attached */
        return new symbol_part(symPart.the_symbol(), lab, symPart.getType());
    }

    /** start a new right hand side */
    private void new_rhs() { rhs_pos = 0; }

    /** add a new right hand side part */
    private void add_rhs_part(production_part part) throws java.lang.Exception {
        if (rhs_pos >= MAX_RHS)
            throw new Exception("Internal Error: Productions limited to " +
                    MAX_RHS + " symbols and actions");
        rhs_parts[rhs_pos] = part;
        rhs_pos++;
    }

    /** add relevant data to terminals */
    private void add_precedence(String term) {
        if (term == null) {
            System.err.println("Unable to add precedence to nonexistent terminal");
        } else {
            symbol_part sp = (symbol_part) symbols.get(term);
            if (sp == null) {
                System.err.println("Could find terminal " + term + " while declaring precedence");
            } else {
                java_cup.GrammarSymbol sym = sp.the_symbol();
                if (sym instanceof terminal)
                    ((terminal) sym).set_precedence(_cur_side, _cur_prec);
                else System.err.println("Precedence declaration: Can't find terminal " + term);
            }
        }
    }

    private int get_new_debug_id() {
        return cur_debug_id++;
    }

    private String attach_debug_symbol(int id, String code) {
        if (!debugSymbols)
            return code;
        return "//@@CUPDBG" + id + "\n" + code;
    }

    /**
     * The end-of-rhs funnel for a top-level alternative: %prec resolution,
     * the shared production-construction core, then reset.
     */
    private void handle_rhs_expr(
            boolean is_prec, String term_name, boolean is_namer, String prod_name
    ) throws Exception {
        NodeRhs pending = pendingRhs;
        pendingRhs = null;
        if (lhs_nt != null) {
            GrammarSymbol sym = null;
            if (is_prec) {
                /* Find the precedence symbol */
                if (term_name == null) {
                    System.err.println("No terminal for contextual precedence");
                } else {
                    sym = ((symbol_part) symbols.get(term_name)).the_symbol();
                }
            }
            Production p = buildProductionCore(lhs_nt, sym, term_name, is_namer, prod_name);
            if (pending != null) corr.recordRhs(pending, p);
        }

        /* reset the rhs accumulation in any case */
        new_rhs();
    }

    /**
     * Shared production-construction core (top-level alternatives and
     * anonymous branches alike): builds the Production for the current
     * rhs_parts -- with the precedence-argument constructor when the %prec
     * target resolved to a terminal -- applies %namer, and builds the
     * implicit $START production after the very first construction.
     */
    private Production buildProductionCore(
            non_terminal lhs, GrammarSymbol precSym, String termName,
            boolean is_namer, String prod_name
    ) throws Exception {
        Production p;
        if (precSym instanceof terminal) {
            p = new Production(
                    lhs, rhs_parts, rhs_pos,
                    ((terminal) precSym).precedence_num(),
                    ((terminal) precSym).precedence_side()
            );
            ((symbol_part) symbols.get(termName)).the_symbol().note_use();
        } else {
            if (precSym != null) {
                System.err.println(
                        "Invalid terminal " + termName + " for contextual precedence assignment"
                );
            }
            p = new Production(lhs, rhs_parts, rhs_pos);
        }
        if (is_namer) {
            if (prod_name == null || prod_name.isEmpty()) {
                System.err.println("No production name for precedence assignment");
            } else {
                p.setProdName(prod_name);
            }
        }

        maybeBuildImplicitStart(precSym);
        return p;
    }

    /**
     * If we have no start non-terminal declared and this is the first
     * production, make its lhs nt the start_nt and build a special start
     * production for it. Kept as its own helper so anonymous branch
     * productions trigger it at exactly the same point they used to (after
     * the first branch production of the first alternative).
     */
    private void maybeBuildImplicitStart(GrammarSymbol sym) throws Exception {
        if (start_nt != null) return;
        start_nt = lhs_nt;

        /* build a special start production */
        new_rhs();
        add_rhs_part(add_lab(new symbol_part(start_nt), "start_val"));
        add_rhs_part(new symbol_part(terminal.EOF));
        add_rhs_part(new action_part("RESULT = start_val;"));
        if (sym instanceof terminal) {
            emit.start_production = new Production(
                    non_terminal.START_nt,
                    rhs_parts,
                    rhs_pos,
                    ((terminal) sym).precedence_num(),
                    ((terminal) sym).precedence_side()
            );
        } else {
            emit.start_production = new Production(non_terminal.START_nt, rhs_parts, rhs_pos);
        }
        new_rhs();
    }
}
