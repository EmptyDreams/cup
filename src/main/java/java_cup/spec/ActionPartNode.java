package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;

/**
 * An embedded action inside a right-hand side (a raw {@code {: ... :}} code
 * block, including the trailing action of a production or of an anonymous
 * branch). The debug-symbol prefix is added by Lowering, as it was by the old
 * parse-time action.
 */
public final class ActionPartNode extends PartNode {

    /** Raw code string of the block. */
    public final String code;

    public ActionPartNode(String code, ComplexLocation loc) {
        super(loc);
        this.code = code;
    }
}
