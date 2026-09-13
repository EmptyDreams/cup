package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;
import java.util.List;

/**
 * An anonymous expression {@code (::T A | B C ...)}: a parenthesized list of
 * alternative branches, optionally preceded by a {@code ::T} return type.
 * Lowering (Phase 1) keeps the historical lowering: one hidden NT per distinct
 * branch shape, plus a group NT when there is more than one branch.
 */
public final class AnonExprNode extends SymRef {

    /** The {@code ::T} return type, or {@code null} when absent. */
    public final String type;

    /** The {@code |}-separated branches in source order. */
    public final List<RhsNode> branches;

    /** Location of the opening parenthesis. */
    public final ComplexLocation loc;

    public AnonExprNode(String type, List<RhsNode> branches, ComplexLocation loc) {
        this.type = type;
        this.branches = branches;
        this.loc = loc;
    }
}
