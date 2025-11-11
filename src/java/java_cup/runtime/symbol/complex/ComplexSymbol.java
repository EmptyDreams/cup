package java_cup.runtime.symbol.complex;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java_cup.runtime.symbol.Location;

public abstract class ComplexSymbol extends Symbol {

    public ComplexSymbol(int id, Location location) {
        super(id, location);
    }

    @Override
    public void reportError(SymbolFactory factory, String message) {
        System.err.println(
            message +
                " for input symbol \"" + factory.getTerminalName(sym) +
                "\" at position" + getLocation()
        );
    }

}