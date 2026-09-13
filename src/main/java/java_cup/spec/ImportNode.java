package java_cup.spec;

/**
 * One {@code import} directive. The {@code static} flag is kept separately;
 * Lowering renders the {@code " static "} prefix the old parse-time action
 * produced.
 */
public final class ImportNode {

    /** Whether the directive was {@code import static}. */
    public final boolean isStatic;

    /** Dotted target name, including a trailing {@code .*} for on-demand imports. */
    public final String target;

    public ImportNode(boolean isStatic, String target) {
        this.isStatic = isStatic;
        this.target = target;
    }
}
