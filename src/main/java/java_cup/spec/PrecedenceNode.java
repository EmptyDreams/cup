package java_cup.spec;

import java.util.List;

/**
 * One {@code precedence left|right|nonassoc ...} declaration. The side is
 * stored as the {@link java_cup.assoc} constant; Lowering bumps the running
 * precedence level once per declaration, then applies it to every listed
 * terminal, matching the old mid-rule action order.
 */
public final class PrecedenceNode {

    /** One of {@link java_cup.assoc}#left, #right, #nonassoc. */
    public final int side;

    /** The declared terminals in source order; each entry is a {@link SymRef}
     * because the parser also accepts {@code precedence left (A|B);}. */
    public final List<SymRef> terminalRefs;

    public PrecedenceNode(int side, List<SymRef> terminalRefs) {
        this.side = side;
        this.terminalRefs = terminalRefs;
    }
}
