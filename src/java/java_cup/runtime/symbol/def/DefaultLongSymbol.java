package java_cup.runtime.symbol.def;

public class DefaultLongSymbol extends DefaultSymbol {

    private final long value;

    public DefaultLongSymbol(int id, int left, int right, long value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultLongSymbol(int id, long value) {
        this(id, -1, -1, value);
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
        return "default long #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}