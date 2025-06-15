package java_cup.runtime.symbol.complex;

public class ComplexLongSymbol extends ComplexSymbol {

    private final long value;

    public ComplexLongSymbol(int id, Location left, Location right, long value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexLongSymbol(int id, long value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Long.valueOf(value);
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex long #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}