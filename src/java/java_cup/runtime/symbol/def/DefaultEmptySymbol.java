package java_cup.runtime.symbol.def;

public class DefaultEmptySymbol extends DefaultSymbol {

    public DefaultEmptySymbol(int id, int left, int right) {
        super(id, left, right);
    }

    public DefaultEmptySymbol(int id) {
        this(id, -1, -1);
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
        return "default #" + sym + ", empty, pos=[" +  getLeft() + ", " + getRight() + "]";
    }

}