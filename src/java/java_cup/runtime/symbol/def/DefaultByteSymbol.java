package java_cup.runtime.symbol.def;

public class DefaultByteSymbol extends DefaultSymbol {

    private final byte value;

    public DefaultByteSymbol(int id, int left, int right, byte value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultByteSymbol(int id, byte value) {
        this(id, -1, -1, value);
    }

    @Override
    public boolean isNull() {
        return false;
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
        return "default byte #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}