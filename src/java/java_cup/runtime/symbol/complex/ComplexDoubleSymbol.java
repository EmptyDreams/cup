package java_cup.runtime.symbol.complex;

public class ComplexDoubleSymbol extends ComplexSymbol {

    private final double value;

    public ComplexDoubleSymbol(int id, Location left, Location right, double value) {
        super(id, left, right);
        this.value = value;
    }
    public ComplexDoubleSymbol(int id, double value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T> T value() {
        return null;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex double #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}