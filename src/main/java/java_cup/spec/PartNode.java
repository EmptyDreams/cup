package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;

/**
 * Base class of a single right-hand-side part: either a symbol part
 * (optionally quantified, labeled, spread, or an anonymous expression) or an
 * embedded action.
 */
public abstract class PartNode {

    /** Location of the part's anchoring token (symbol name, {@code (}, or the
     * action's code string). */
    public final ComplexLocation loc;

    protected PartNode(ComplexLocation loc) {
        this.loc = loc;
    }
}
