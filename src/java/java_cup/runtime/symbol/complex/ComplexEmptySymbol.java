package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

public class ComplexEmptySymbol extends ComplexSymbol {

    public ComplexEmptySymbol(int id, Location location) {
        super(id, location);
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
        return "complex #" + sym + ", empty, pos=[" + getLocation() + "]";
    }

}