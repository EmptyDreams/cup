package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexCharSymbol extends ComplexSymbol {

    private final char value;

    public ComplexCharSymbol(int id, Location location, char value) {
        super(id, location);
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return false;
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
        return "complex char #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}