package java_cup.runtime.symbol.complex;

public class ComplexShortSymbol extends ComplexSymbol {

    private final short value;

    public ComplexShortSymbol(int id, Location left, Location right, short value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexShortSymbol(int id, short value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Short.valueOf(value);
    }

    @Override
    public short getAsShort() {
        return value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex short #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}