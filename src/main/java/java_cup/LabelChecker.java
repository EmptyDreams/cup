package java_cup;

import java_cup.spec.AnonExprNode;
import java_cup.spec.PartNode;
import java_cup.spec.ProductionNode;
import java_cup.spec.RhsNode;
import java_cup.spec.SpecNode;
import java_cup.spec.SymbolPartNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validates labels against the sugar-preserving spec tree (replaces the old
 * flat-graph {@code Main.check_labels} and the duplicate-label loop that used
 * to live in {@code Lowering.add_rhs_part}).
 *
 * <p>Two checks, both in every mode: a label must not be a Java keyword or
 * reserved word (labels become variable, field and method names in the
 * generated code), and a label must not repeat within one right-hand side.
 * Working on the tree gives both checks real source positions, and reaches the
 * branch labels of anonymous expressions -- the old graph-stage check never
 * saw them because lowering stripped them off the production parts.</p>
 */
public final class LabelChecker {

    private LabelChecker() {}

    /**
     * Java keywords, reserved words and literals. Labels become variable, field
     * and method names in the generated code, so they must be valid Java
     * identifiers and not keywords. {@code const} and {@code goto} are reserved
     * though unused; contextual keywords such as {@code var}, {@code record} or
     * {@code yield} remain valid identifiers and are allowed.
     */
    private static final Set<String> JAVA_KEYWORDS = Set.of(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
        "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native", "new",
        "package", "private", "protected", "public", "return", "short", "static",
        "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while",
        "true", "false", "null");

    /** Checks every production alternative (and each anonymous-expression
     * branch, recursively) of the spec. */
    public static void run(SpecNode spec) {
        for (ProductionNode prod : spec.productions) {
            if (prod.hasError) continue;
            for (RhsNode rhs : prod.alternatives) {
                checkRhs(rhs);
            }
        }
    }

    /** Checks one right-hand side: keyword labels and duplicates. */
    private static void checkRhs(RhsNode rhs) {
        Map<String, PartNode> seen = new HashMap<>();
        for (PartNode part : rhs.parts) {
            if (!(part instanceof SymbolPartNode)) continue;
            SymbolPartNode sp = (SymbolPartNode) part;

            /* anonymous expressions carry their own labeled parts; each
               branch is a right-hand side of its own */
            if (sp.ref instanceof AnonExprNode) {
                for (RhsNode branch : ((AnonExprNode) sp.ref).branches) {
                    checkRhs(branch);
                }
            }

            String label = sp.label;
            if (label == null) continue;

            if (JAVA_KEYWORDS.contains(label)) {
                ErrorManager.getManager().emit_error(
                    "Label \"" + label + "\" is a Java keyword/reserved word and"
                        + " cannot be used as a label (at " + pos(part) + ")");
            }

            PartNode prev = seen.put(label, part);
            if (prev != null) {
                ErrorManager.getManager().emit_error(
                    "Label \"" + label + "\" at " + pos(part)
                        + " is already used at " + pos(prev)
                        + " in the same production; compilation will fail");
            }
        }
    }

    /** "line:column" of a part's anchor token, or "?" without a position. */
    private static String pos(PartNode part) {
        if (part.loc == null) return "?";
        return part.loc.getStartLine() + ":" + part.loc.getStartColumn();
    }
}
