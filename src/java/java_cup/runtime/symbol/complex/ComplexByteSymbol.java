package java_cup.runtime.symbol.complex;

public class ComplexByteSymbol extends ComplexSymbol {

    private final byte value;

    public ComplexByteSymbol(int id, Location left, Location right, byte value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexByteSymbol(int id, byte value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Byte.valueOf(value);
    }

    @Override
    public byte getAsByte() {
        return value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex byte #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}