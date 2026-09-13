package java_cup.ast;

import java_cup.ErrorManager;
import java_cup.GrammarSymbol;
import java_cup.Main;
import java_cup.Production;
import java_cup.SpecCorrelation;
import java_cup.internal_error;
import java_cup.non_terminal;

import java_cup.spec.ActionPartNode;
import java_cup.spec.AnonExprNode;
import java_cup.spec.NamedRefNode;
import java_cup.spec.PartNode;
import java_cup.spec.RhsNode;
import java_cup.spec.SymbolPartNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builds the node-type graph for {@code -ast} mode by walking the
 * sugar-preserving spec tree.
 *
 * <p>This used to reverse-engineer the same information out of the lowered
 * flat graph (opt-box/list-box flag inference, {@code fromProd}/{@code index}
 * threading, label re-attachment through side tables). The tree makes all of
 * that directly observable: quantifiers carry nullability and list-ness,
 * spread marks carry inline-ness, anonymous expressions carry their branch
 * structure. The only thing taken from the flat side, through the
 * {@link SpecCorrelation} Lowering records, is each alternative's
 * {@link Production#getProdName()} -- the single source of the names that are
 * also baked into the generated parser's actions -- plus symbol resolution
 * and the hidden non-terminals anonymous expressions lowered into.</p>
 */
public class AstNodeBuilder {

    /** Correlation to the lowered flat graph. */
    private final SpecCorrelation corr;

    /**
     * Memo for user non-terminals, put before walking their alternatives so
     * that recursive references terminate with the in-progress type.
     */
    private final Map<non_terminal, VirtualType> ntTypes = new HashMap<>();

    /** Memo for anonymous-expression node types (one per use site). */
    private final IdentityHashMap<AnonExprNode, VirtualType> anonTypes = new IdentityHashMap<>();

    /**
     * Class name to type of the most recent build. Read by
     * {@code emit.node_classes} after the build ({@link #isNodeClass},
     * {@link #getNtName}); replaced wholesale at the start of each build so
     * repeated invocations never observe a stale pass.
     */
    private static final Map<String, VirtualType> classNameToType = new HashMap<>();

    private AstNodeBuilder(SpecCorrelation corr) {
        this.corr = corr;
    }

    public static boolean isNodeClass(String className) {
        return classNameToType.containsKey(className) || "AstNode".equals(className);
    }

    public static String getNtName(String className) {
        var type = classNameToType.get(className);
        if (type == null || type.getSymId() == -1) return className;
        return non_terminal.find(type.getSymId()).name();
    }

    /** Entry point: builds the type graph starting at the given symbol. */
    public static VirtualType buildGraph(GrammarSymbol sym) throws internal_error {
        classNameToType.clear();
        return new AstNodeBuilder(Main.spec_corr).typeOf(sym);
    }

    /*---------------------------------------------------------------*/

    /** Type of a referenced symbol: a node type for {@code ::AstNode}
     *  non-terminals, a basic (template-wrapped) type for everything else. */
    private VirtualType typeOf(GrammarSymbol sym) throws internal_error {
        if (sym.is_non_term() && ("AstNode".equals(sym.stack_type()))) {
            return typeOfNt((non_terminal) sym);
        }
        return VirtualType.ofBasic(sym.stack_type(), -1);
    }

    /** Node type of a user non-terminal, one alternative per production. */
    private VirtualType typeOfNt(non_terminal nt) throws internal_error {
        var cached = ntTypes.get(nt);
        if (cached != null) return cached;

        var type = new VirtualType(nt.index(), true, "");
        type.className = nt.astClassName();
        ntTypes.put(nt, type);
        classNameToType.put(type.className, type);

        Map<String, VirtualProduction> prods = new HashMap<>();
        for (RhsNode rhs : corr.alternativesOf(nt)) {
            var vp = virtualProduction(rhs);
            if (vp == null) continue;
            prods.putIfAbsent(vp.name, vp);
        }
        type.prods = List.copyOf(prods.values());
        checkLabelTypes(nt.name(), type.prods);
        return type;
    }

    /**
     * Node type of one anonymous-expression use site: named after the hidden
     * non-terminal Lowering expanded it into (the same name its branch
     * productions' auto actions reference), one alternative per branch.
     */
    private VirtualType typeOfAnon(AnonExprNode ae) throws internal_error {
        var cached = anonTypes.get(ae);
        if (cached != null) return cached;
        non_terminal hidden = corr.anonNtOf(ae);
        if (hidden == null) return null;

        var type = new VirtualType(hidden.index(), true, "");
        type.className = hidden.astClassName();
        anonTypes.put(ae, type);
        classNameToType.put(type.className, type);

        Map<String, VirtualProduction> prods = new HashMap<>();
        for (RhsNode branch : ae.branches) {
            var vp = virtualProduction(branch);
            if (vp == null) continue;
            prods.putIfAbsent(vp.name, vp);
        }
        type.prods = List.copyOf(prods.values());
        checkLabelTypes(hidden.name(), type.prods);
        return type;
    }

    /**
     * One builder alternative for a right-hand side (a top-level alternative
     * or an anonymous branch). Null when it is not a builder alternative:
     * no lowered production exists for it, or it ends in a user action.
     */
    private VirtualProduction virtualProduction(RhsNode rhs) throws internal_error {
        Production flat = corr.productionOf(rhs);
        if (flat == null) return null;
        if (flat.hasTailAction()) return null;

        List<VirtualField> fields = new ArrayList<>();
        for (PartNode part : rhs.parts) {
            if (part instanceof ActionPartNode) continue;
            var sp = (SymbolPartNode) part;
            /* skip parts that are neither labeled nor spread: they carry no
               value into the node */
            if (sp.label == null && !sp.spread) continue;

            VirtualType t;
            if (sp.ref instanceof AnonExprNode) {
                t = typeOfAnon((AnonExprNode) sp.ref);
            } else {
                var sym = corr.resolve(((NamedRefNode) sp.ref).name);
                if (sym == null) continue; // undeclared: already reported, no field
                t = typeOf(sym);
            }
            if (t == null) continue;

            int mask = 0;
            if (sp.spread) mask |= 0b1;
            if (sp.quant != null && (!sp.quant.isList || sp.quant.allowEmpty)) mask |= 0b100;
            if (sp.quant != null && sp.quant.isList) {
                /* mirrors the old toList call on the list-box NT; the symbol
                   id argument was never read */
                t = t.toList(-1);
            }
            var field = new VirtualField(sp.label, t, mask);
            field.loc = sp.loc;
            fields.add(field);
        }
        return new VirtualProduction(flat.getProdName(), fields);
    }

    /*---------------------------------------------------------------*/

    /**
     * A label shared by several alternatives of this non-terminal must carry
     * the same type: the node class keeps a single field/getter per label,
     * and VirtualType.allFields() deduplicates by label, so a mismatch would
     * either fail to compile or silently drop the field.
     */
    private static void checkLabelTypes(String ntName, List<VirtualProduction> prods) {
        Map<String, String> labelTypes = new HashMap<>();
        for (var virtualProd : prods) {
            for (var field : virtualProd.fields) {
                // for an unlabeled spread container the hoisted fields are the
                // ones that end up as this node's fields
                var candidates = field.isInline() && field.label == null
                    ? field.allSubFields().collect(Collectors.toList())
                    : List.of(field);
                for (var candidate : candidates) {
                    String name = candidate.joinLabel();
                    String typeName = candidate.type.getRealName();
                    String prev = labelTypes.putIfAbsent(name, typeName);
                    if (prev != null && !prev.equals(typeName)) {
                        String at = candidate.loc == null
                            ? ""
                            : " (at " + candidate.loc.getStartLine() + ':' + candidate.loc.getStartColumn() + ")";
                        ErrorManager.getManager().emit_error(
                            "Label \"" + name + "\" is used with different types (\"" + prev
                                + "\" and \"" + typeName + "\") in productions of non-terminal \""
                                + ntName + "\"; a label must have the same type in every production"
                                + " of a non-terminal" + at);
                    }
                }
            }
        }
    }

}
