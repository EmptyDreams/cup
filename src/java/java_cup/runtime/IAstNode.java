package java_cup.runtime;

import java.util.List;

public interface IAstNode {

    /**
     * Checks whether the value for the given label exists
     * @param label The label to check,
     *              the existence check label for the <code>isXxx</code> format
     *              should be passed along with the <code>is</code> prefix.
     */
    boolean hasLabel(String label);

    /**
     * Returns the node or list with the given label.
     * @return the node or list with the given label, or null if no such node or list exists.
     */
    Object getByLabel(String label);

    /**
     * Returns the node with the given label.
     * @return the node with the given label, or null if no such node exists.
     */
    default IAstNode getNodeByLabel(String label) {
        Object node = getByLabel(label);
        if (node instanceof IAstNode) {
            return (IAstNode) node;
        } else {
            return null;
        }
    }

    /**
     * Returns the list with the given label.
     * @return the list with the given label, or null if no such list exists.
     */
    default List<Object> getListByLabel(String label) {
        Object node = getByLabel(label);
        if (node instanceof List) {
            //noinspection unchecked
            return (List<Object>) node;
        } else {
            return null;
        }
    }

}