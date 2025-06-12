package java_cup.runtime.symbol.complex;

public class ComplexCharSymbol extends ComplexSymbol {

    private final char value;

    public ComplexCharSymbol(int id, Location left, Location right, char value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexCharSymbol(int id, char value) {
        this(id, Location.EMPTY, Location.EMPTY, value);
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) Character.valueOf(value);
    }

    @Override
    public char getAsChar() {
        return value;
    }

    @Override
    public int hashCode() {
        return Character.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex char #" + sym + ", value=[" + value + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }

}