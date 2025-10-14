package java_cup.runtime.symbol.def;

import java_cup.runtime.Symbol;

public abstract class DefaultSymbol extends Symbol {

    private final int left, right;

    public DefaultSymbol(int id, int left, int right) {
        super(id);
        this.left = left;
        this.right = right;
    }

    public DefaultSymbol(int id) {
        this(id, -1, -1);
    }

    public final int getLeft() {
        return left;
    }

    public final int getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left * 31 + right;
    }

}