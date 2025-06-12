package java_cup.runtime.symbol.def;

public class DefaultFloatSymbol extends DefaultSymbol {

    private final float value;

    public DefaultFloatSymbol(int id, int left, int right, float value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultFloatSymbol(int id, float value) {
        this(id, -1, -1, value);
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Float.valueOf(value);
    }

    @Override
    public float getAsFloat() {
        return value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "default float #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}