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
     * Returns the child AST node at the specified index.
     * <p>
     * This method may return {@code null} if the child at the given index is optional
     * and not present. The valid range of indices depends on the specific node type.
     *
     * @param index the zero-based index of the child node to retrieve
     * @return the child node at the given index, or {@code null} if the slot is empty
     * @throws IndexOutOfBoundsException if the index is invalid for this node type
     */
    protected abstract Map.Entry<String, AstNode> getByIndex(int index);

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
        return AstNodeIterator.EMPTY;
    }

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