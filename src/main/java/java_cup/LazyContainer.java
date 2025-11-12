package java_cup;

/**
 * A container that lazily evaluates its value.
 *
 * @param <T> The type of the value.
 * @author kmar
 */
public class LazyContainer<T> {

    private T value = null;
    private CheckedSupplier<T> supplier;

    LazyContainer(CheckedSupplier<T> supplier) {
        this.supplier = supplier;
    }

    T get() throws internal_error {
        if (value == null) {
            value = supplier.get();
            if (value == null) {
                throw new IllegalStateException("LazyContainer supplier returned null");
            }
            supplier = null;
        }
        return value;
    }

}