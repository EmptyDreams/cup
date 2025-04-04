package java_cup;

import java.util.*;
import java.util.function.*;

/**
 * <p>An int-to-Object Map implementation that avoids boxing/unboxing overhead.</p>
 *
 * <p>This class stores keys in ascending order using an internal array structure.
 * Binary search is employed for efficient lookups during read operations.</p>
 *
 * Usage constraints:
 * <ol>
 *   <li>New keys can only be inserted if they are greater than all existing keys</li>
 *   <li>Existing values cannot be modified or removed (except through clear())</li>
 *   <li>Does not support iterating over keys or key-value pairs<br/>
 *      (use values() method to iterate over stored objects)</li>
 * </ol>
 *
 * @param <E> The type of objects stored in this map
 * @author kmar
 */
@SuppressWarnings("unchecked")
public class MonotonicIntObjectArrayMap<E> implements Map<Integer, E> {

    private int[] keyArray;
    private Object[] valueArray;
    private int size = 0;

    public MonotonicIntObjectArrayMap() {
        this(16);
    }

    public MonotonicIntObjectArrayMap(int initialCapacity) {
        this.keyArray = new int[initialCapacity];
        this.valueArray = new Object[initialCapacity];
    }

    public void ensureCapacity(int newSize) {
        int oldSize = size;
        if (newSize > oldSize) {
            int plusSize = Math.max(newSize - oldSize, oldSize >> 1) + oldSize;
            int[] newKeyArray = new int[plusSize];
            Object[] newValueArray = new Object[plusSize];
            System.arraycopy(keyArray, 0, newKeyArray, 0, oldSize);
            System.arraycopy(valueArray, 0, newValueArray, 0, oldSize);
            this.keyArray = newKeyArray;
            this.valueArray = newValueArray;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(int key) {
        int index = Arrays.binarySearch(keyArray, 0, size, key);
        return index >= 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return containsKey(((Integer) key).intValue());
    }

    @Override
    public boolean containsValue(Object value) {
        int hash = value.hashCode();
        for (int i = 0; i < size; i++) {
            Object item = valueArray[i];
            if (hash == item.hashCode() && value.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public E get(int key) {
        int index = Arrays.binarySearch(keyArray, 0, size, key);
        if (index >= 0) {
            return (E) valueArray[index];
        } else {
            return null;
        }
    }

    @Override
    public E get(Object key) {
        return get(((Integer) key).intValue());
    }

    public E put(int key, E value) {
        assert size == 0 || key >= keyArray[size - 1];
        int index = size;
        ensureCapacity(index + 1);
        ++size;
        keyArray[index] = key;
        valueArray[index] = value;
        return null;
    }

    @Override
    public E put(Integer key, E value) {
        return put(key.intValue(), value);
    }

    @Override
    public E remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends E> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        Arrays.fill(valueArray, 0, size, null);
        size = 0;
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    private Collection<E> _values;

    @Override
    public Collection<E> values() {
        if (_values == null) {
            _values = new ValueList();
        }
        return _values;
    }

    @Override
    public Set<Entry<Integer, E>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MonotonicIntObjectArrayMap)) return false;
        MonotonicIntObjectArrayMap<?> m = (MonotonicIntObjectArrayMap<?>) o;
        int size = this.size();
        if (size != m.size()) return false;
        for (int i = 0; i < size; i++) {
            if (keyArray[i] != m.keyArray[i] || !valueArray[i].equals(m.valueArray[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return size * 31 + Arrays.hashCode(keyArray);
    }

    public E getOrDefault(int key, E defaultValue) {
        E value = get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public E getOrDefault(Object key, E defaultValue) {
        return getOrDefault(((Integer) key).intValue(), defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super Integer, ? super E> action) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(BiFunction<? super Integer, ? super E, ? extends E> function) {
        throw new UnsupportedOperationException();
    }

    public E putIfAbsent(int key, E value) {
        if (key > keyArray[size - 1]) {
            put(key, value);
            return null;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public E putIfAbsent(Integer key, E value) {
        return putIfAbsent(key.intValue(), value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(Integer key, E oldValue, E newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E replace(Integer key, E value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E computeIfAbsent(Integer key, Function<? super Integer, ? extends E> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    private class ValueList implements Collection<E> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean isEmpty() {
            return MonotonicIntObjectArrayMap.this.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return containsValue(o);
        }

        @Override
        public Iterator<E> iterator() {
            return (Iterator<E>) Arrays.stream(valueArray, 0, size).iterator();
        }

        @Override
        public Object[] toArray() {
            Object[] result = new Object[size];
            System.arraycopy(valueArray, 0, result, 0, size);
            return result;
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            if (a.length < size) {
                return (T1[]) Arrays.copyOf(valueArray, size, a.getClass());
            } else {
                //noinspection SuspiciousSystemArraycopy
                System.arraycopy(valueArray, 0, a, 0, size);
                if (a.length > size) {
                    a[size] = null;
                }
                return a;
            }
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object o : c) {
                if (!contains(o)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            MonotonicIntObjectArrayMap.this.clear();
        }

    }

}