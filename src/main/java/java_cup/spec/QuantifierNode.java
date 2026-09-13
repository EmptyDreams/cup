package java_cup.spec;

import java.util.List;

/**
 * A quantifier on a symbol part. The fields mirror the constructor inputs the
 * old parse-time action fed into {@link java_cup.SymQuantifier}, so Lowering
 * can rebuild that object exactly:
 *
 * <ul>
 *   <li>{@code X?}      -- {@link #isList()} false</li>
 *   <li>{@code X*}/{@code X+} -- list, {@link #separators} is {@code null}</li>
 *   <li>{@code X[a,b]*?} -- list, separators {@code [a,b]},
 *       {@link #allowEmpty} from {@code *}, {@link #allowTail} from the
 *       trailing {@code ?}</li>
 * </ul>
 */
public final class QuantifierNode {

    /** {@code false} for {@code X?} (opt-box); {@code true} for all list forms. */
    public final boolean isList;

    /** Separator refs for the bracket form, or {@code null} for the plain
     * {@code *}/{@code +} forms (Lowering must pass {@code null}, not an empty
     * list, to SymQuantifier). */
    public final List<SymRef> separators;

    /** Empty reduction allowed ({@code *}, or the trailing {@code ?}). */
    public final boolean allowEmpty;

    /** Trailing separator allowed (the trailing {@code ?}). */
    public final boolean allowTail;

    private QuantifierNode(boolean isList, List<SymRef> separators, boolean allowEmpty, boolean allowTail) {
        this.isList = isList;
        this.separators = separators;
        this.allowEmpty = allowEmpty;
        this.allowTail = allowTail;
    }

    /** {@code X?} */
    public static QuantifierNode opt() {
        return new QuantifierNode(false, null, true, false);
    }

    /** {@code X*} ({@code allowEmpty}) or {@code X+}. */
    public static QuantifierNode starPlus(boolean allowEmpty) {
        return new QuantifierNode(true, null, allowEmpty, false);
    }

    /** {@code X[a,b]*?} and friends. */
    public static QuantifierNode bracket(List<SymRef> separators, boolean allowEmpty, boolean allowTail) {
        return new QuantifierNode(true, separators, allowEmpty, allowTail);
    }
}
