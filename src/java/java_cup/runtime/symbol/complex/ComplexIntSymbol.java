package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexIntSymbol extends ComplexSymbol {

    private final int value;

    public ComplexIntSymbol(int id, Location location, int value) {
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
        return (T) Integer.valueOf(value);
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex int #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}