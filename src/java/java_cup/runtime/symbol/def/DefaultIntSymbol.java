package java_cup.runtime.symbol.def;

public class DefaultIntSymbol extends DefaultSymbol {

    private final int value;

    public DefaultIntSymbol(int id, int left, int right, int value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultIntSymbol(int id, int value) {
        this(id, -1, -1, value);
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
        return "default int #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}