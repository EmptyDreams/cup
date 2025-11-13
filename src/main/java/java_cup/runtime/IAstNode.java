package java_cup.runtime;

import java_cup.runtime.symbol.Location;

/**
 * Interface for all AST nodes.
 *
 * @author kmar
 */
@SuppressWarnings("unused")
public interface IAstNode {

    /**
     * Returns the location of the node.
     */
    Location getLocation();

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
    IAstNode getByLabel(String label);

}