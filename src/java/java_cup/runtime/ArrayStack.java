package java_cup.runtime;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * An array-based stack implementation.
 * @param <E> the type of elements held in this collection
 * @author kmar
 */
public class ArrayStack<E> extends ArrayList<E> {

    public E peek() {
        return get(size() - 1);
    }

    public E pop() {
        return remove(size() - 1);
    }

    public void push(E item) {
        add(item);
    }

    public E elementAt(int index) {
        return get(index);
    }

    public void setElementAt(E item, int index) {
        set(index, item);
    }

    public boolean empty() {
        return isEmpty();
    }

    public int search(E dist) {
        int index = lastIndexOf(dist);
        if (index >= 0)
            return size() - index;
        return -1;
    }

    public void copyInto(Object[] anArray) {
        toArray(anArray);
    }

    public void setSize(int newSize) {
        ensureCapacity(newSize);
        int oldSize = size();
        for (int i = oldSize; i != newSize; ++i) {
            add(null);
        }
    }

    public Enumeration<E> elements() {
        return new Enumeration<E>() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < size();
            }

            public E nextElement() {
                if (count < size()) {
                    return get(count++);
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    public E firstElement() {
        return get(0);
    }

    public E lastElement() {
        return get(size() - 1);
    }

    public void removeElementAt(int index) {
        remove(index);
    }

    public void insertElementAt(E item, int index) {
        add(index, item);
    }

    public void addElement(E item) {
        add(item);
    }

    public void removeElement(E item) {
        remove(item);
    }

    public void removeAllElements() {
        clear();
    }

}