package java_cup.runtime;

import java.util.function.IntConsumer;

/**
 * An array-based int stack implementation that avoids boxing overhead.
 *
 * <p>This specialized stack stores primitive int values directly without
 * requiring Integer object wrappers.</p>
 *
 * <p>The implementation intentionally does not implement the {@code List<Integer>}
 * interface. This prevents signature conflicts between {@code int get(int)}
 * and {@code Integer get(int)} methods, thus minimizing required changes
 * to existing code.</p>
 *
 * @author kmar
 */
public class IntArrayStack {

    private int[] data;
    private int size;

    public IntArrayStack() {
        this(4);
    }

    public IntArrayStack(int size) {
        data = new int[size];
    }

    public void push(int value) {
        int index = size;
        ensureCapacity(++size);
        data[index] = value;
    }

    public int peek() {
        return data[size - 1];
    }

    public int pop() {
        return data[--size];
    }

    public int get(int index) {
        return data[index];
    }

    public int size() {
        return size;
    }

    public void set(int index, int value) {
        data[index] = value;
    }

    public int elementAt(int index) {
        return data[index];
    }

    public void setElementAt(int value, int index) {
        data[index] = value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean empty() {
        return size == 0;
    }

    public int search(int dist) {
        int size = this.size;
        for (int i = size - 1; i >= 0; i--) {
            if (data[i] == dist) {
                return size - i;
            }
        }
        return -1;
    }

    public void clear() {
        size = 0;
    }

    public void ensureCapacity(int size) {
        int oldLength = data.length;
        if (oldLength < size) {
            int newSize = Math.max(size - oldLength, oldLength >> 1);
            int[] newData = new int[newSize];
            System.arraycopy(data, 0, newData, 0, oldLength);
            data = newData;
        }
    }

    public void copyInto(int[] anArray) {
        System.arraycopy(data, 0, anArray, 0, size);
    }

    public void copyInto(Integer[] anArray) {
        for (int i = 0; i < size; i++) {
            anArray[i] = data[i];
        }
    }

    public void toArray(int[] anArray) {
        copyInto(anArray);
    }

    public void toArray(Integer[] anArray) {
        copyInto(anArray);
    }

    public void forEachInt(IntConsumer consumer) {
        int size = this.size;
        for (int i = 0; i < size; i++) {
            consumer.accept(data[i]);
        }
    }

}