package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexFloatSymbol extends ComplexSymbol {

    private final float value;

    public ComplexFloatSymbol(int id, Location location, float value) {
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
        return (T) Float.valueOf(value);
    }

    @Override
    public float getAsFloat() {
        return value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex float #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}