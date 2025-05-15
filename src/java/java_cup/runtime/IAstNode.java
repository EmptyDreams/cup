package java_cup.runtime;

import java.util.List;

/**
 * Interface for all AST nodes.
 * @author kmar
 */
@SuppressWarnings("unused")
public interface IAstNode {

    /**
     * Checks whether the value for the given label exists
     * @param label The label to check,
     *              the existence check label for the <code>isXxx</code> format
     *              should be passed along with the <code>is</code> prefix.
     */
    boolean hasLabel(String label);

    /**
     * Returns the value with the given label.
     * @return the value with the given label, or null if the label does not exist
     *         or the value is not of the expected type.
     */
    Object getByLabel(String label);

    /**
     * Returns the node with the given label.
     * @return the node with the given label, or null if the label does not exist
     *         or the value is not of type IAstNode.
     */
    default IAstNode getNodeByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof IAstNode ? (IAstNode) node : null;
    }

    /**
     * Returns the list with the given label.
     * @return the list with the given label, or null if the label does not exist
     *         or the value is not of type List.
     */
    default List<Object> getListByLabel(String label) {
        Object node = getByLabel(label);
        //noinspection unchecked
        return node instanceof List ? (List<Object>) node : null;
    }

    /**
     * Returns the String with the given label.
     * @return the String, or null if the label does not exist 
     *         or the value is not of type String.
     */
    default String getStringByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof String ? (String) node : null;
    }

    /**
     * Returns the Boolean with the given label.
     * @return the Boolean, or null if the label does not exist 
     *         or the value is not of type Boolean.
     */
    default Boolean getBooleanByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Boolean ? (Boolean) node : null;
    }

    /**
     * Returns the Byte with the given label.
     * @return the Byte, or null if the label does not exist 
     *         or the value is not of type Byte.
     */
    default Byte getByteByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Byte ? (Byte) node : null;
    }

    /**
     * Returns the Short with the given label.
     * @return the Short, or null if the label does not exist 
     *         or the value is not of type Short.
     */
    default Short getShortByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Short ? (Short) node : null;
    }

    /**
     * Returns the Character with the given label.
     * @return the Character, or null if the label does not exist 
     *         or the value is not of type Character.
     */
    default Character getCharacterByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Character ? (Character) node : null;
    }

    /**
     * Returns the Integer with the given label.
     * @return the Integer, or null if the label does not exist 
     *         or the value is not of type Integer.
     */
    default Integer getIntegerByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Integer ? (Integer) node : null;
    }

    /**
     * Returns the Long with the given label.
     * @return the Long, or null if the label does not exist 
     *         or the value is not of type Long.
     */
    default Long getLongByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Long ? (Long) node : null;
    }

    /**
     * Returns the Float with the given label.
     * @return the Float, or null if the label does not exist 
     *         or the value is not of type Float.
     */
    default Float getFloatByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Float ? (Float) node : null;
    }

    /**
     * Returns the Double with the given label.
     * @return the Double, or null if the label does not exist 
     *         or the value is not of type Double.
     */
    default Double getDoubleByLabel(String label) {
        Object node = getByLabel(label);
        return node instanceof Double ? (Double) node : null;
    }

}