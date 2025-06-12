package java_cup.runtime.symbol.complex;

public class ComplexBoolSymbol extends ComplexSymbol {

    private final boolean value;

    public ComplexBoolSymbol(int id, Location left, Location right, boolean value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexBoolSymbol(int id, boolean value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
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
        return "complex boolean #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}