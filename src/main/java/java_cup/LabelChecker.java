package java_cup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Validates labels against the spec tree (replaces the old flat-graph
 * {@code Main.check_labels} and the duplicate-label loop that used to live
 * in {@code Lowering.add_rhs_part}).
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
    public static void run(NodeSpec spec) {
        for (var prodNode : Tree.elems(spec.getProds())) {
            var prod = (NodeProduction) prodNode;
            if (Tree.str(prod.getLhs()) == null) continue; // error alternative
            for (var rhsNode : Tree.elems(prod.getAlts())) {
                checkRhs((NodeRhs) rhsNode);
            }
        }
    }

    /** Checks one right-hand side: keyword labels and duplicates. */
    private static void checkRhs(NodeRhs rhs) {
        Map<String, NodeProdPart> seen = new HashMap<>();
        for (var partNode : Tree.elems(rhs.getParts())) {
            var part = (NodeProdPart) partNode;
            if (part.getCodeStr() != null) continue; // action part
            var symid = part.getSymid();
            if (symid == null) continue;

            /* anonymous expressions carry their own labeled parts; each
               branch is a right-hand side of its own */
            var anon = symid.getAnon();
            if (anon != null) {
                for (var branchNode : Tree.elems(anon.getBranches())) {
                    checkRhs((NodeRhs) branchNode);
                }
            }

            String label = Tree.str(part.getLabid());
            if (label == null) continue;

            if (JAVA_KEYWORDS.contains(label)) {
                ErrorManager.getManager().emit_error(
                    "Label \"" + label + "\" is a Java keyword/reserved word and"
                        + " cannot be used as a label (at " + pos(part) + ")");
            }

            NodeProdPart prev = seen.put(label, part);
            if (prev != null) {
                ErrorManager.getManager().emit_error(
                    "Label \"" + label + "\" at " + pos(part)
                        + " is already used at " + pos(prev)
                        + " in the same production; compilation will fail");
            }
        }
    }

    /** "line:column" of a part's anchor token, or "?" without a position. */
    private static String pos(NodeProdPart part) {
        var loc = Tree.anchor(part);
        if (loc == null) return "?";
        return loc.getStartLine() + ":" + loc.getStartColumn();
    }
}
