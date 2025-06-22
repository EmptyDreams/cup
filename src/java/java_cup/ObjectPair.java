package java_cup;

import java.util.Objects;

/**
 * A simple pair of objects.
 *
 * @author kmar
 */
public final class ObjectPair<T, U> {

    private final T first;
    private final U second;

    public ObjectPair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public ObjectPair<T, U> modifySecond(U second) {
        return new ObjectPair<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ObjectPair<?, ?> that = (ObjectPair<?, ?>) o;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(first);
        result = 31 * result + Objects.hashCode(second);
        return result;
    }

    @Override
    public String toString() {
        return "ObjectPair{" +
            "first=" + first +
            ", second=" + second +
            '}';
    }

}