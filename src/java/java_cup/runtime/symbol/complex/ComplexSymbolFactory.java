package java_cup.runtime.symbol.complex;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import java_cup.runtime.symbol.Location;

import java.util.List;

/**
 * @author kmar
 */
public class ComplexSymbolFactory implements SymbolFactory {

    private final String[] terminalNames;
    private final String[] nonTerminalNames;

    public ComplexSymbolFactory(String[] terminalNames, String[] nonTerminalNames) {
        this.terminalNames = terminalNames;
        this.nonTerminalNames = nonTerminalNames;
    }

    @Override
    public String getTerminalName(int id) {
        return terminalNames == null || id < 0 || id >= terminalNames.length ? null : terminalNames[id];
    }

    @Override
    public String getNonTerminalName(int id) {
        return nonTerminalNames == null || id < 0 || id >= nonTerminalNames.length ? null : nonTerminalNames[id];
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, Object value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        var location = first.getLocation().span(last.getLocation());
        return new ComplexObjectSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        var location = first.getLocation().span(last.getLocation());
        return new ComplexEmptySymbol(id, location);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, Object value) {
        var def = (ComplexSymbol) left;
        return new ComplexObjectSymbol(id, def.getLocation(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left) {
        var def = (ComplexSymbol) left;
        return new ComplexEmptySymbol(id, def.getLocation());
    }

    @Override
    public Symbol newSymbol(int id, Location location, Object value) {
        return new ComplexObjectSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, byte value) {
        return new ComplexByteSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, short value) {
        return new ComplexShortSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, int value) {
        return new ComplexIntSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, long value) {
        return new ComplexLongSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, float value) {
        return new ComplexFloatSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, double value) {
        return new ComplexDoubleSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, char value) {
        return new ComplexCharSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location, boolean value) {
        return new ComplexBoolSymbol(id, location, value);
    }

    @Override
    public Symbol newSymbol(int id, Location location) {
        return new ComplexEmptySymbol(id, location);
    }

}