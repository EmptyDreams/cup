package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexDoubleSymbol extends ComplexSymbol {

    private final double value;

    public ComplexDoubleSymbol(int id, Location location, double value) {
        super(id, location);
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public <T> T value() {
        return null;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex double #" + sym + ", value=[" + value + "], pos=[" + getLocation() + "]";
    }

}