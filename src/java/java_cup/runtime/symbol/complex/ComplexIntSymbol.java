package java_cup.runtime.symbol.complex;

public class ComplexIntSymbol extends ComplexSymbol {

    private final int value;

    public ComplexIntSymbol(int id, Location left, Location right, int value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexIntSymbol(int id, int value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Integer.valueOf(value);
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex int #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}