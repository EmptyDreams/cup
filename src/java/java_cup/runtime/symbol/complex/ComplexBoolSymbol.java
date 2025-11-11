package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexBoolSymbol extends ComplexSymbol {

    private final boolean value;

    public ComplexBoolSymbol(int id, Location location, boolean value) {
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
        return (T) Boolean.valueOf(value);
    }

    @Override
    public boolean getAsBoolean() {
        return value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex boolean #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}