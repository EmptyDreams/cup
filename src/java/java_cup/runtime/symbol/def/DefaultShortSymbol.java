package java_cup.runtime.symbol.def;

public class DefaultShortSymbol extends DefaultSymbol {

    private final short value;

    public DefaultShortSymbol(int id, int left, int right, short value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultShortSymbol(int id, short value) {
        this(id, -1, -1, value);
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
        return "default short #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}