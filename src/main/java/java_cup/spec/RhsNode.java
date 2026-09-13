package java_cup.spec;

import java.util.List;

/**
 * One {@code |}-separated right-hand side of a production. A trailing action
 * is simply the last {@link ActionPartNode} of {@link #parts}, exactly as the
 * grammar sees it. {@code %prec} and {@code %namer} suffixes are optional.
 */
public final class RhsNode {

    /** The parts in source order (symbols, embedded actions, anonymous groups). */
    public final List<PartNode> parts;

    /** {@code %prec} target, or {@code null}; a {@link SymRef} because the
     * grammar also accepts {@code %prec (X|Y)}. */
    public final SymRef precTerminal;

    /** {@code %namer} name, or {@code null}. */
    public final String namer;

    public RhsNode(List<PartNode> parts, SymRef precTerminal, String namer) {
        this.parts = parts;
        this.precTerminal = precTerminal;
        this.namer = namer;
    }
}
