package java_cup.spec;

import java_cup.runtime.symbol.complex.ComplexLocation;

/**
 * One of the four optional code sections ({@code action code},
 * {@code parser code}, {@code init with}, {@code scan with}).
 */
public final class CodePartNode {

    /** Which of the four sections this node represents. */
    public enum Kind {
        ACTION, PARSER, INIT, SCAN
    }

    public final Kind kind;

    /** Raw code string ({@code {: ... :}} body), without the debug-symbol prefix. */
    public final String code;

    /** Location of the code string token, for future positioned diagnostics. */
    public final ComplexLocation loc;

    public CodePartNode(Kind kind, String code, ComplexLocation loc) {
        this.kind = kind;
        this.code = code;
        this.loc = loc;
    }
}
