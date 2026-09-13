package java_cup;

import java_cup.runtime.AstNode;
import java_cup.runtime.symbol.complex.ComplexLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Unwrapping helpers for reading the generated spec-tree node classes
 * (NodeSpec and friends, produced by generating parser.cup with
 * {@code -ast Node%p}).
 *
 * <p>The generated tree stores every value in wrapper nodes: strings as
 * {@code NodeString}, lists as {@code NodeListX}. Sentinels produced on
 * error-recovery alternatives are all-null base instances, and empty list
 * boxes may surface as null fields, so every read goes through these
 * null-tolerant helpers instead of touching the wrappers directly.</p>
 *
 * <p>Coding rule for value checks: always test {@code getXxx() != null}
 * (then {@code Tree.str(...)} for the string itself), never
 * {@code hasXxx()} -- on a fabricated base instance the checker methods
 * of required fields still answer true while the getters yield null.</p>
 */
public final class Tree {

    private Tree() {}

    /** String value of a wrapper field; null-safe on both levels. */
    public static String str(NodeString n) {
        return n == null ? null : n.getValue();
    }

    /**
     * Elements of a list-wrapper field in source order; an absent or
     * sentinel wrapper yields an empty list, never null.
     */
    public static List<AstNode> elems(AstNode list) {
        var out = new ArrayList<AstNode>();
        if (list == null) return out;
        for (var entry : list) out.add(entry.getValue());
        return out;
    }

    /**
     * The anchor position of a production part: the label token when the
     * part is labeled (the wrapper's position is the label reduction's),
     * otherwise the symbol reference (the ID token or the parenthesized
     * anonymous expression).
     */
    public static ComplexLocation anchor(NodeProdPart part) {
        var labid = part.getLabid();
        if (labid != null && str(labid) != null) {
            return (ComplexLocation) labid.getLocation();
        }
        var symid = part.getSymid();
        return symid == null ? null : (ComplexLocation) symid.getLocation();
    }
}
