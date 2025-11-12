package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexByteSymbol extends ComplexSymbol {

    private final byte value;

    public ComplexByteSymbol(int id, Location location, byte value) {
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
        return (T) Byte.valueOf(value);
    }

    @Override
    public byte getAsByte() {
        return value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex byte #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}