package java_cup.runtime;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayStack<T> extends ArrayList<T> {

    public T peek() {
        return get(size() - 1);
    }

    public T pop() {
        return remove(size() - 1);
    }

    public void push(T item) {
        add(item);
    }

    public T elementAt(int index) {
        return get(index);
    }

    public void setElementAt(T item, int index) {
        set(index, item);
    }

    public boolean empty() {
        return isEmpty();
    }

    public int search(T dist) {
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

    public Enumeration<T> elements() {
        return new Enumeration<T>() {
            int count = 0;

            public boolean hasMoreElements() {
                return count < size();
            }

            public T nextElement() {
                if (count < size()) {
                    return get(count++);
                }
                throw new NoSuchElementException("Vector Enumeration");
            }
        };
    }

    public T firstElement() {
        return get(0);
    }

    public T lastElement() {
        return get(size() - 1);
    }

    public void removeElementAt(int index) {
        remove(index);
    }

    public void insertElementAt(T item, int index) {
        add(index, item);
    }

    public void addElement(T item) {
        add(item);
    }

    public void removeElement(T item) {
        remove(item);
    }

    public void removeAllElements() {
        clear();
    }

}