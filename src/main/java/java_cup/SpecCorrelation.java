package java_cup;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Correlation between the spec tree (the generated Node* classes) and the
 * flat graph {@link Lowering} built from it. Lowering records entries while
 * it constructs; the tree-based passes (the AST node builder) query them for
 * the things only the flat side knows -- most importantly each right-hand
 * side's {@link Production}, whose {@code getProdName()} is the single source
 * of the alternative names that are also baked into the generated parser's
 * actions.
 *
 * <p>All maps are identity-based: every tree node is lowered exactly once.</p>
 */
public final class SpecCorrelation {

    /**
     * The Production Lowering built for a right-hand side (a top-level
     * alternative or an anonymous-expression branch). Absent when no
     * production exists for it (broken grammar).
     */
    private final IdentityHashMap<NodeRhs, Production> rhsToProduction = new IdentityHashMap<>();

    /** The hidden non-terminal an anonymous-expression use site lowered into. */
    private final IdentityHashMap<NodeAnonExpr, non_terminal> anonToNt = new IdentityHashMap<>();

    /**
     * Each user non-terminal's alternatives, in source (= creation) order --
     * the tree-side equivalent of {@code non_terminal.productions()}.
     */
    private final Map<non_terminal, List<NodeRhs>> ntAlternatives = new IdentityHashMap<>();

    /** Lowering's symbol table, for name resolution. */
    Map<String, production_part> symbols;

    void recordRhs(NodeRhs rhs, Production production) {
        rhsToProduction.put(rhs, production);
    }

    void recordAnon(NodeAnonExpr ae, non_terminal nt) {
        anonToNt.put(ae, nt);
    }

    void addAlternative(non_terminal nt, NodeRhs rhs) {
        ntAlternatives.computeIfAbsent(nt, k -> new ArrayList<>()).add(rhs);
    }

    public Production productionOf(NodeRhs rhs) {
        return rhsToProduction.get(rhs);
    }

    public non_terminal anonNtOf(NodeAnonExpr ae) {
        return anonToNt.get(ae);
    }

    public List<NodeRhs> alternativesOf(non_terminal nt) {
        var list = ntAlternatives.get(nt);
        return list == null ? List.of() : list;
    }

    /**
     * Resolves a declared symbol name to its flat GrammarSymbol (terminal or
     * non-terminal), or null when undeclared.
     */
    public GrammarSymbol resolve(String name) {
        var part = symbols.get(name);
        return part == null ? null : ((symbol_part) part).the_symbol();
    }
}
