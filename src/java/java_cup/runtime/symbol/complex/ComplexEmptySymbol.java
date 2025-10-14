package java_cup.runtime.symbol.complex;

public class ComplexEmptySymbol extends ComplexSymbol {

    public ComplexEmptySymbol(int id, Location left, Location right) {
        super(id, left, right);
    }

    public ComplexEmptySymbol(int id) {
        this(id, Location.EMPTY, Location.EMPTY);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public <T> T value() {
        return null;
    }

    @Override
    public String toString() {
        return "complex #" + sym + ", empty, pos=[" +  getLeft() + " / " + getRight() + "]";
    }

}