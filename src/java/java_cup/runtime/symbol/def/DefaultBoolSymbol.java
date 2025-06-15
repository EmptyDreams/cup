package java_cup.runtime.symbol.def;

public class DefaultBoolSymbol extends DefaultSymbol {

    private final boolean value;

    public DefaultBoolSymbol(int id, int left, int right, boolean value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultBoolSymbol(int id, boolean value) {
        this(id, -1, -1, value);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Boolean.valueOf(value);
    }

    @Override
    public boolean getAsBoolean() {
        return value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "default boolean #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}