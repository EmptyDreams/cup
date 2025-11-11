package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexLongSymbol extends ComplexSymbol {

    private final long value;

    public ComplexLongSymbol(int id, Location location, long value) {
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
        return (T) Long.valueOf(value);
    }

    @Override
    public long getAsLong() {
        return value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex long #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}