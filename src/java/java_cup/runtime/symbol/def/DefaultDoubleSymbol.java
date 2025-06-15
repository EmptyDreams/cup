package java_cup.runtime.symbol.def;

public class DefaultDoubleSymbol extends DefaultSymbol {

    private final double value;

    public DefaultDoubleSymbol(int id, int left, int right, double value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultDoubleSymbol(int id, double value) {
        this(id, -1, -1, value);
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
        return "default double #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}