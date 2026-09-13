package java_cup.spec;

import java.util.List;

/**
 * One symbol declaration statement, e.g.
 * {@code terminal String ID, NUM;} or {@code non terminal Object stmt;}.
 * The names are declared left to right in one statement; Lowering registers
 * them in exactly that order.
 */
public final class SymbolDeclNode {

    /** {@code true} for {@code terminal}, {@code false} for {@code non terminal}. */
    public final boolean isTerminal;

    /** Declared stack type, or {@code null} when the statement had no type id
     * (Lowering then applies the historical "Object" default). */
    public final String type;

    /** Declared names in source order. */
    public final List<String> names;

    public SymbolDeclNode(boolean isTerminal, String type, List<String> names) {
        this.isTerminal = isTerminal;
        this.type = type;
        this.names = names;
    }
}
