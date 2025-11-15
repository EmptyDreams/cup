package java_cup.runtime;

import java_cup.runtime.symbol.Location;

import java.util.*;
import java.util.function.Consumer;

/**
 * Interface for all AST nodes.
 *
 * @author kmar
 */
@SuppressWarnings("unused")
public abstract class AstNode implements Iterable<Map.Entry<String, AstNode>> {

    /**
     * Returns the location of the node.
     */
    public abstract Location getLocation();

    /**
     * Checks whether the value for the given label exists
     *
     * @param label The label to check,
     *              the existence check label for the <code>isXxx</code> format
     *              should be passed along with the <code>is</code> prefix.
     */
    public abstract boolean hasLabel(String label);

    /**
     * Returns the value with the given label.
     *
     * @return the value with the given label, or null if the label does not exist
     * or the value is not of the expected type.
     */
    public abstract AstNode getByLabel(String label);

    /**
     * Returns whether this node is a terminal node.
     */
    public boolean isTerminal() {
        return false;
    }

    /**
     * Returns the name of the node.
     */
    public abstract String getNodeName();

    /**
     * Returns the child AST node at the specified index.
     * <p>
     * This method may return {@code null} if the child at the given index is optional
     * and not present. The valid range of indices depends on the specific node type.
     *
     * @param index the zero-based index of the child node to retrieve
     * @return the child node at the given index, or {@code null} if the slot is empty
     * @throws IndexOutOfBoundsException if the index is invalid for this node type
     */
    protected Map.Entry<String, AstNode> getByIndex(int index) {
        throw new IndexOutOfBoundsException(index);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
        return AstNodeIterator.EMPTY;
    }

    /**
     * Returns a string representation of this AST node and its subtree in tree format.
     *
     * @param withHighlight whether to add console color highlighting to different parts of the output
     * @return a string representation of the AST subtree rooted at this node
     */
    public String toTreeString(boolean withHighlight) {
        StringBuilder sb = new StringBuilder();
        sb.append(this).append('\n');
        buildTreeString(sb, "", withHighlight);
        if (withHighlight) {
            sb.append(ANSI_RESET);
        }
        return sb.toString();
    }

    private void buildTreeString(StringBuilder sb, String prefix, boolean withHighlight) {
        for (var entry : this) {
            var label = entry.getKey();
            var child = entry.getValue();

            if (withHighlight) {
                sb.append(ANSI_CYAN).append(prefix).append("+-- ");
                if (!Character.isDigit(label.charAt(0))) {
                    if (child.isTerminal()) {
                        sb.append(ANSI_BOLD).append(ANSI_YELLOW);
                    } else {
                        sb.append(ANSI_GREEN);
                    }
                    sb.append(label).append(ANSI_PURPLE).append("@");
                }
                sb.append(ANSI_RESET).append(child).append('\n');
            } else {
                sb.append(prefix).append("+-- ");
                if (!Character.isDigit(label.charAt(0))) {
                    sb.append(label).append("@");
                }
                sb.append(child).append('\n');
            }
            child.buildTreeString(sb, prefix + "|   ", withHighlight);
        }
    }
    
    // ANSI color codes for console highlighting
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BOLD = "\u001B[1m";

    protected final static class AstNodeIterator implements Iterator<Map.Entry<String, AstNode>> {

        static final AstNodeIterator EMPTY = new AstNodeIterator(null, 0);

        private final AstNode node;
        private final int size;
        private int index = 0;
        private Map.Entry<String, AstNode> next = null;

        public AstNodeIterator(AstNode node, int size) {
            this.node = node;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            if (this.next != null) {
                return true;
            }
            Map.Entry<String, AstNode> next = null;
            int index = this.index;
            while (index != size) {
                next = node.getByIndex(index++);
                if (next != null) {
                    this.next = next;
                    break;
                }
            }
            this.index = index;
            return next != null;
        }

        @Override
        public Map.Entry<String, AstNode> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            var next = this.next;
            this.next = null;
            return next;
        }

        @Override
        public void forEachRemaining(Consumer<? super Map.Entry<String, AstNode>> action) {
            Objects.requireNonNull(action);
            if (this.next != null) {
                action.accept(this.next);
            }
            for (int i = index; i != size; ++i) {
                var next = node.getByIndex(i);
                if (next != null) {
                    action.accept(next);
                }
            }
            index = size;
            this.next = null;
        }

    }

}