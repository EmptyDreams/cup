package java_cup;

import java.util.function.Supplier;

public class LazyContainer<T> {

  private T value = null;
  private Supplier<T> supplier;

  LazyContainer(Supplier<T> supplier) {
    this.supplier = supplier;
  }

  T get() {
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