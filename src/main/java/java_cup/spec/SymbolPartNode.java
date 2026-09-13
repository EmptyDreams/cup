package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;

/**
 * A symbol occurrence on the right-hand side: {@code [ ... ] ref [quantifier]
 * [:label]} plus the {@code ...} spread flag. {@code ref} is either a plain
 * name or an anonymous expression; quantifier and label are optional.
 */
public final class SymbolPartNode extends PartNode {

    /** The {@code ...} spread marker was present. */
    public final boolean spread;

    /** The referenced symbol (plain name or anonymous expression). */
    public final SymRef ref;

    /** The quantifier ({@code ?}, {@code *}, {@code +}, {@code [a,b]*?}), or {@code null}. */
    public final QuantifierNode quant;

    /** The label, or {@code null}; {@link #loc} carries the label token's position. */
    public final String label;

    public SymbolPartNode(boolean spread, SymRef ref, QuantifierNode quant, String label, ComplexLocation loc) {
        super(loc);
        this.spread = spread;
        this.ref = ref;
        this.quant = quant;
        this.label = label;
    }
}
