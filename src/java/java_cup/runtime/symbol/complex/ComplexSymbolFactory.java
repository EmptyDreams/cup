package java_cup.runtime.symbol.complex;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;

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

    public Symbol newSymbol(int id, Location left, Location right) {
        return new ComplexEmptySymbol(id, left, right);
    }

    public Symbol newSymbol(int id, Location left, Location right, Object value) {
        return new ComplexObjectSymbol(id, left, right, value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, Object value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        if (value == null) return new ComplexEmptySymbol(id, first.getLeft(), last.getRight());
        return new ComplexObjectSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, boolean value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexBoolSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, byte value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexByteSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, short value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexShortSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, char value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexCharSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, int value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexIntSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, long value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexLongSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, float value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexFloatSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, double value) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexDoubleSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList) {
        var first = (ComplexSymbol) symList.get(0);
        var last = (ComplexSymbol) symList.get(symList.size() - 1);
        return new ComplexEmptySymbol(id, first.getLeft(), last.getRight());
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, Object value) {
        var def = (ComplexSymbol) left;
        return new ComplexObjectSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, boolean value) {
        var def = (ComplexSymbol) left;
        return new ComplexBoolSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, byte value) {
        var def = (ComplexSymbol) left;
        return new ComplexByteSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, short value) {
        var def = (ComplexSymbol) left;
        return new ComplexShortSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, char value) {
        var def = (ComplexSymbol) left;
        return new ComplexCharSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, int value) {
        var def = (ComplexSymbol) left;
        return new ComplexIntSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, long value) {
        var def = (ComplexSymbol) left;
        return new ComplexLongSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, float value) {
        var def = (ComplexSymbol) left;
        return new ComplexFloatSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, double value) {
        var def = (ComplexSymbol) left;
        return new ComplexDoubleSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left) {
        var def = (ComplexSymbol) left;
        return new ComplexEmptySymbol(id, def.getLeft(), def.getRight());
    }

    @Override
    public Symbol newSymbol(int id, Object value) {
        return new ComplexObjectSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, boolean value) {
        return new ComplexBoolSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, byte value) {
        return new ComplexByteSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, short value) {
        return new ComplexShortSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, char value) {
        return new ComplexCharSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, int value) {
        return new ComplexIntSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, long value) {
        return new ComplexLongSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, float value) {
        return new ComplexFloatSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, double value) {
        return new ComplexDoubleSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id) {
        return new ComplexEmptySymbol(id);
    }

    @Override
    public Symbol startSymbol(int id, int state) {
        return new ComplexEmptySymbol(id, state);
    }
    
}