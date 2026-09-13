package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;
import java.util.List;

/**
 * One production statement {@code nt ::= rhs | rhs | ... ;}. The alternatives
 * list keeps the source order of the {@code |}-separated branches; Lowering
 * turns each alternative into one internal Production.
 */
public final class ProductionNode {

    /** Left-hand-side non-terminal name (the {@code "ILLEGAL"} marker for the
     * {@code production ::= error SEMI} recovery alternative, which also sets
     * {@link #hasError}). */
    public final String lhsName;

    /** Location of the LHS name token. */
    public final ComplexLocation loc;

    /** {@code |}-separated right-hand sides in source order. */
    public final List<RhsNode> alternatives;

    /** Marks the {@code production ::= error SEMI} recovery alternative. */
    public final boolean hasError;

    public ProductionNode(String lhsName, ComplexLocation loc, List<RhsNode> alternatives, boolean hasError) {
        this.lhsName = lhsName;
        this.loc = loc;
        this.alternatives = alternatives;
        this.hasError = hasError;
    }
}
