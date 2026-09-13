package java_cup.spec;

/**
 * A plain symbol-name reference. The name {@code "ILLEGAL"} marks the
 * {@code symbol_id ::= error} recovery alternative.
 */
public final class NamedRefNode extends SymRef {

    /** The referenced (undeclared at tree level) symbol name. */
    public final String name;

    public NamedRefNode(String name) {
        this.name = name;
    }
}
