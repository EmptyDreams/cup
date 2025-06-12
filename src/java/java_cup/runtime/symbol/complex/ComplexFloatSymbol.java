package java_cup.runtime.symbol.complex;

public class ComplexFloatSymbol extends ComplexSymbol {

    private final float value;

    public ComplexFloatSymbol(int id, Location left, Location right, float value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexFloatSymbol(int id, float value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
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
        return "complex float #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}