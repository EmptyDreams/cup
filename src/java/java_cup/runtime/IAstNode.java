package java_cup.runtime;

/**
 * Interface for all AST nodes.
 *
 * @author kmar
 */
@SuppressWarnings("unused")
public interface IAstNode {

    /**
     * Checks whether the value for the given label exists
     *
     * @param label The label to check,
     *              the existence check label for the <code>isXxx</code> format
     *              should be passed along with the <code>is</code> prefix.
     */
    boolean hasLabel(String label);

    /**
     * Returns the value with the given label.
     *
     * @return the value with the given label, or null if the label does not exist
     * or the value is not of the expected type.
     */
    Object getByLabel(String label);

    /**
     * Returns the node with the given label.
     *
     * @return the node with the given label, or null if the label does not exist
     * or the value is not of type IAstNode.
     */
    default IAstNode getNodeByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof IAstNode ? (IAstNode) node : null;
    }

}