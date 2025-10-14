package java_cup.runtime.symbol.complex;

import java_cup.runtime.Symbol;

public abstract class ComplexSymbol extends Symbol {

    private final Location left, right;

    public ComplexSymbol(int id, Location left, Location right) {
        super(id);
        assert left != null && right != null;
        this.left = left;
        this.right = right;
    }

    public ComplexSymbol(int id) {
        this(id, Location.EMPTY, Location.EMPTY);
    }

    public final Location getLeft() {
        return left;
    }

    public final Location getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }

}