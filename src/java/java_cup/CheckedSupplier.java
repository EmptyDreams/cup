package java_cup;

/**
 * A functional interface that can throw an exception.
 * @param <R> The return type of the function.
 * @author kmar
 */
@FunctionalInterface
public interface CheckedSupplier<R> {

    R get() throws internal_error;

}