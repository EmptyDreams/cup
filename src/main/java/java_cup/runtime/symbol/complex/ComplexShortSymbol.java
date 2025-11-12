package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexShortSymbol extends ComplexSymbol {

    private final short value;

    public ComplexShortSymbol(int id, Location location, short value) {
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
        return (T) Short.valueOf(value);
    }

    @Override
    public short getAsShort() {
        return value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex short #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}