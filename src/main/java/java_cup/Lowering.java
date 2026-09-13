package java_cup;

import java_cup.spec.AnonExprNode;
import java_cup.spec.CodePartNode;
import java_cup.spec.ImportNode;
import java_cup.spec.NamedRefNode;
import java_cup.spec.PartNode;
import java_cup.spec.ActionPartNode;
import java_cup.spec.PrecedenceNode;
import java_cup.spec.ProductionNode;
import java_cup.spec.QuantifierNode;
import java_cup.spec.RhsNode;
import java_cup.spec.SpecNode;
import java_cup.spec.SymbolDeclNode;
import java_cup.spec.SymbolPartNode;
import java_cup.spec.SymRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lowers the sugar-preserving spec tree into the internal grammar graph.
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
 * {@code %prec X %namer N} drops the namer, the memoization caches in
 * non_terminal are shared across the whole run and never cleared, the group
 * non-terminal of an anonymous expression only ever gets one production, and
 * an undeclared {@code %prec} terminal still crashes with an NPE exactly as
 * before. Do not "fix" any of these here -- Phase 2 replaces this whole
 * anonymous-expression layer.</p>
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
    private java_cup.spec.RhsNode pendingRhs = null;

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
     * expression. Replaces the old rhsPartsCache emptiness test and the
     * subNtRecord nesting guard.
     */
    private boolean inAnon = false;

    /** The name of the last child non_terminal (null again once consumed). */
    private String lastSubNtName = null;

    /** The branch non-terminals of the anonymous expression being lowered. */
    private List<production_part> subNtList = null;

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
    public void run(SpecNode spec) throws Exception {
        /* declare "error" as a terminal and the start non terminal --
           the old position-0 action of the spec rule */
        symbols.put("error", new symbol_part(terminal.error));
        non_terms.put("$START", non_terminal.START_nt);

        if (spec.packageName != null) {
            emit.package_name = spec.packageName;
        }
        for (ImportNode imp : spec.imports) {
            emit.import_list.push(imp.isStatic ? " static " + imp.target : imp.target);
        }
        if (spec.className != null) {
            emit.parser_class_name = spec.className;
            emit.symbol_const_class_name = spec.className + "Sym";
        }
        for (CodePartNode cp : spec.codeParts) {
            lowerCodePart(cp);
        }
        for (SymbolDeclNode sd : spec.symbolDecls) {
            lowerSymbolDecl(sd);
        }
        for (PrecedenceNode p : spec.precedences) {
            lowerPrecedence(p);
        }
        lowerStart(spec.startName);
        for (ProductionNode pn : spec.productions) {
            lowerProduction(pn);
        }
    }

    /*---------------------------------------------------------------*/
    /* Directive sections                                             */
    /*---------------------------------------------------------------*/

    private void lowerCodePart(CodePartNode cp) {
        String current = null;
        String redundantMessage = null;
        switch (cp.kind) {
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
        }
        if (current != null) {
            ErrorManager.getManager().emit_warning(redundantMessage);
            return;
        }
        String decorated = attach_debug_symbol(get_new_debug_id(), cp.code);
        switch (cp.kind) {
            case ACTION: emit.action_code = decorated; break;
            case PARSER: emit.parser_code = decorated; break;
            case INIT:   emit.init_code = decorated; break;
            case SCAN:   emit.scan_code = decorated; break;
        }
    }

    private void lowerSymbolDecl(SymbolDeclNode sd) throws internal_error {
        String type = sd.type == null ? "Object" : sd.type;
        for (String name : sd.names) {
            if (symbols.get(name) != null) {
                ErrorManager.getManager().emit_error(
                        "java_cup.runtime.Symbol \"" + name + "\" has already been declared");
            } else if (sd.isTerminal) {
                symbols.put(name, new symbol_part(new terminal(name, type)));
            } else {
                non_terminal this_nt = new non_terminal(name, type);
                non_terms.put(name, this_nt);
                symbols.put(name, new symbol_part(this_nt));
            }
        }
    }

    private void lowerPrecedence(PrecedenceNode p) throws Exception {
        /* the old mid-rule action: update_precedence(p.side) */
        _cur_side = p.side;
        _cur_prec++;
        for (SymRef ref : p.terminalRefs) {
            add_precedence(lowerTermId(ref));
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

    private void lowerProduction(ProductionNode pn) throws Exception {
        if (pn.hasError) {
            /* production ::= error SEMI */
            ErrorManager.getManager().emit_error("Syntax Error");
            return;
        }
        /* lookup the lhs nt */
        lhs_nt = (non_terminal) non_terms.get(pn.lhsName);

        /* if it wasn't declared, emit a message */
        if (lhs_nt == null) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_warning(
                        "LHS non terminal \"" + pn.lhsName + "\" has not been declared");
            }
        }

        /* reset the rhs accumulation */
        new_rhs();
        for (RhsNode rn : pn.alternatives) {
            lowerRhs(rn);
        }
    }

    private void lowerRhs(RhsNode rn) throws Exception {
        if (lhs_nt != null) corr.addAlternative(lhs_nt, rn);
        for (PartNode part : rn.parts) {
            lowerPart(part);
        }
        pendingRhs = rn;
        /* the %prec terminal is checked after all parts, before the rhs
           action runs (that is when the old term_id action reduced) */
        if (rn.precTerminal != null) {
            /* historical quirk: `prod_part_list %prec T %namer N` calls
               handle_rhs_expr with is_namer = false, dropping the namer */
            handle_rhs_expr(true, lowerTermId(rn.precTerminal), false, null, true);
        } else if (rn.namer != null) {
            handle_rhs_expr(false, null, true, rn.namer, true);
        } else {
            handle_rhs_expr(false, null, false, null, true);
        }
    }

    /**
     * Ports the prod_part action for one part. Event order inside a single
     * part matches the old reduction order: the anonymous expression of the
     * symbol reference lowers first (symbol_id), then the quantifier's
     * separator list resolves (opt_quantifier), then the symbol lookup,
     * validations and quantifier desugaring run (the prod_part action).
     */
    private void lowerPart(PartNode node) throws Exception {
        if (node instanceof ActionPartNode) {
            /* add a new production part */
            add_rhs_part(new action_part(
                    attach_debug_symbol(get_new_debug_id(), ((ActionPartNode) node).code)));
            return;
        }
        SymbolPartNode sp = (SymbolPartNode) node;

        /* symbol_id: an anonymous expression lowers here, a plain name is
           just a name (the old ObjectPair first/second) */
        String symName;
        String symType;
        if (sp.ref instanceof AnonExprNode) {
            AnonExprNode ae = (AnonExprNode) sp.ref;
            symName = lowerAnonExpr(ae);
            symType = ae.type;
        } else {
            symName = ((NamedRefNode) sp.ref).name;
            symType = null;
        }

        /* opt_quantifier: build the SymQuantifier, resolving the bracket
           separator list (each separator may itself be an anon expression) */
        SymQuantifier quantifier = null;
        if (sp.quant != null) {
            QuantifierNode q = sp.quant;
            if (!q.isList) {
                quantifier = new SymQuantifier();
            } else if (q.separators == null) {
                quantifier = new SymQuantifier(null, q.allowEmpty, false);
            } else {
                List<production_part> partList = new ArrayList<>(q.separators.size());
                for (SymRef sep : q.separators) {
                    production_part part = symbols.get(lowerSymRef(sep));
                    partList.add(part);
                }
                quantifier = new SymQuantifier(partList, q.allowEmpty, q.allowTail);
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
        } else if (sp.spread && symb.is_action()) {
            if (ErrorManager.getManager().getErrorCount() == 0) {
                ErrorManager.getManager().emit_error(
                        "Action symbol \"" + symName + "\" cannot use '... 'operator");
            }
        } else if (sp.spread && quantifier != null && quantifier.isList()) {
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
            production_part part = add_lab(symb, sp.label);
            if (sp.spread) ((symbol_part) part).markInline();
            add_rhs_part(part);
        }
    }

    /*---------------------------------------------------------------*/
    /* Anonymous expressions                                          */
    /*---------------------------------------------------------------*/

    /**
     * Ports the symbol_id LPAREN alternative: lowers every branch (creating
     * the shared branch non-terminal and its production via the
     * handle_rhs_expr branch path), then the group non-terminal when there
     * is more than one branch. Returns the resulting non-terminal's name.
     */
    private String lowerAnonExpr(AnonExprNode ae) throws Exception {
        /* the old newSubNtList() */
        if (inAnon) {
            throw new internal_error("Anonymous non-terminals cannot be nested");
        }
        /* the old cacheRhs(): shelve the enclosing rhs accumulation so the
           branch lowering below does not clobber it */
        production_part[] shelved = new production_part[rhs_pos];
        if (rhs_pos != 0) {
            System.arraycopy(rhs_parts, 0, shelved, 0, rhs_pos);
        }
        int shelvedPos = rhs_pos;
        new_rhs();

        inAnon = true;
        subNtList = new ArrayList<>();
        try {
            for (RhsNode branch : ae.branches) {
                new_rhs();
                for (PartNode part : branch.parts) {
                    lowerPart(part);
                }
                pendingRhs = branch;
                if (branch.precTerminal != null) {
                    handle_rhs_expr(true, lowerTermId(branch.precTerminal), false, null, true);
                } else if (branch.namer != null) {
                    handle_rhs_expr(false, null, true, branch.namer, true);
                } else {
                    handle_rhs_expr(false, null, false, null, true);
                }
            }
        } finally {
            inAnon = false;
        }
        List<production_part> branchNts = subNtList;
        subNtList = null;

        if (branchNts.size() > 1) {
            var subNt = lhs_nt.createSubNts(branchNts);
            lastSubNtName = subNt.name();
            symbols.put(lastSubNtName, new symbol_part(subNt));
            for (production_part part : branchNts) {
                rhs_pos = 1;
                rhs_parts[0] = part;
                handle_rhs_expr(false, null, false, null, false);
            }
        }
        String result = lastSubNtName;
        lastSubNtName = null;
        /* correlate the use site with the hidden NT it resolved to (the
           group NT, or the single branch NT) */
        if (result != null) {
            corr.recordAnon(ae, (non_terminal) ((symbol_part) symbols.get(result)).the_symbol());
        }

        /* the old popRhsCache(): restore the enclosing rhs accumulation */
        if (shelvedPos != 0) {
            System.arraycopy(shelved, 0, rhs_parts, 0, shelvedPos);
        }
        rhs_pos = shelvedPos;
        return result;
    }

    /**
     * Resolves a symbol reference at a point of use (separator list, %prec
     * target, precedence entry). Anonymous expressions lower in place,
     * exactly where the old symbol_id action ran.
     */
    private String lowerSymRef(SymRef ref) throws Exception {
        if (ref instanceof NamedRefNode) {
            return ((NamedRefNode) ref).name;
        }
        return lowerAnonExpr((AnonExprNode) ref);
    }

    /*---------------------------------------------------------------*/
    /* Verbatim ports of the old action-code helpers                  */
    /*---------------------------------------------------------------*/

    /** check that the symbol_id is a terminal (ports term_id). */
    private String lowerTermId(SymRef ref) throws Exception {
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

    private void addSubNt(non_terminal nt) throws internal_error {
        subNtList.add(new symbol_part(nt));
    }

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
     * The end-of-rhs funnel, ported verbatim from parser.cup.
     *
     * <p>Callers, as before: {@code isSubList == true} from the rhs rule
     * (either a top-level alternative, or an anonymous-expression branch when
     * {@link #inAnon} is set), {@code isSubList == false} from the anonymous
     * group loop (when {@link #lastSubNtName} names the group NT).</p>
     */
    private void handle_rhs_expr(
            boolean is_prec, String term_name, boolean is_namer, String prod_name, boolean isSubList
    ) throws Exception {
        java_cup.GrammarSymbol sym = null;
        java_cup.spec.RhsNode pending = pendingRhs;
        pendingRhs = null;
        if (lhs_nt != null) {
            non_terminal subNt = null;
            if (inAnon && isSubList) {
                subNt = lhs_nt.createSubNt(
                        rhs_parts, rhs_pos, Production.PositionFinder.newInstance(lhs_nt)
                );
                lastSubNtName = subNt.name();
                symbols.put(lastSubNtName, new symbol_part(subNt));
                addSubNt(subNt);
            }
            if (!isSubList && lastSubNtName != null) {
                subNt = (non_terminal) ((symbol_part) symbols.get(lastSubNtName)).the_symbol();
            }
            non_terminal nt = subNt == null ? lhs_nt : subNt;
            if (is_prec) {
                /* Find the precedence symbol */
                if (term_name == null) {
                    System.err.println("No terminal for contextual precedence");
                } else {
                    sym = ((symbol_part) symbols.get(term_name)).the_symbol();
                }
            }
            if (subNt == null) {
                boolean exists = Arrays.stream(rhs_parts, 0, rhs_pos)
                        .anyMatch(
                                part -> {
                                    if (part.is_action()) return false;
                                    GrammarSymbol symbol = ((symbol_part) part).the_symbol();
                                    if (!symbol.is_non_term()) return false;
                                    non_terminal partNt = (non_terminal) symbol;
                                    return partNt.isAnno() && !partNt.isListBox() && !partNt.isOptBox();
                                }
                        );
                if (exists) {
                    // The logic here is to determine the index of the production,
                    // we need to know the index in advance in order to insert the action for it.
                    int count = 1;
                    boolean inActionSequence = true;
                    for (int i = 0; i < rhs_pos; i++) {
                        if (rhs_parts[i].is_action()) {
                            if (!inActionSequence) {
                                count++;
                                inActionSequence = true;
                            }
                        } else {
                            inActionSequence = false;
                        }
                    }
                    // insert the action to the first position
                    System.arraycopy(rhs_parts, 0, rhs_parts, 1, rhs_pos++);
                    rhs_parts[0] = new action_part("_pushInlineProd(" + (Production.number() + count) + ");", true);
                }
            } else if (subNt.num_productions() != 0) {
                new_rhs();
                return;
            }
            /* build the production */
            Production p;
            if (sym instanceof terminal) {
                p = new Production(
                        nt, rhs_parts, rhs_pos,
                        ((terminal) sym).precedence_num(),
                        ((terminal) sym).precedence_side()
                );
                ((symbol_part) symbols.get(term_name)).the_symbol().note_use();
            } else {
                if (is_prec) {
                    System.err.println(
                            "Invalid terminal " + term_name + " for contextual precedence assignment"
                    );
                }
                p = new Production(nt, rhs_parts, rhs_pos);
            }
            if (pending != null) corr.recordRhs(pending, p);
            if (is_namer) {
                if (prod_name == null || prod_name.isEmpty()) {
                    System.err.println("No production name for precedence assignment");
                } else {
                    p.setProdName(prod_name);
                }
            }

            /* if we have no start non-terminal declared and this is
                       the first production, make its lhs nt the start_nt
                       and build a special start production for it. */
            if (start_nt == null) {
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

        /* reset the rhs accumulation in any case */
        new_rhs();
    }
}
