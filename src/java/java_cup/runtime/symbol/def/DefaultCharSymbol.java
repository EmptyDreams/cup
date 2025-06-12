package java_cup.runtime.symbol.def;

public class DefaultCharSymbol extends DefaultSymbol {

    private final char value;

    public DefaultCharSymbol(int id, int left, int right, char value) {
        super(id, left, right);
        this.value = value;
    }

    public DefaultCharSymbol(int id, char value) {
        this(id, -1, -1, value);
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
        return "default char #" + sym + ", value=[" + value + "], pos=[" + getLeft() + ", " + getRight() + "]";
    }

}