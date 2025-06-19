package java_cup.runtime.symbol.def;

import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;

import java.util.List;

/**
 * @author kmar
 */
public class DefaultSymbolFactory implements SymbolFactory {

    private final String[] terminalNames;
    private final String[] nonTerminalNames;

    public DefaultSymbolFactory(String[] terminalNames, String[] nonTerminalNames) {
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
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        if (value == null) return new DefaultEmptySymbol(id, first.getLeft(), last.getRight());
        return new DefaultObjectSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, boolean value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultBoolSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, byte value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultByteSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, short value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultShortSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, char value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultCharSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, int value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultIntSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, long value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultLongSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, float value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultFloatSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList, double value) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultDoubleSymbol(id, first.getLeft(), last.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, List<Symbol> symList) {
        var first = (DefaultSymbol) symList.get(0);
        var last = (DefaultSymbol) symList.get(symList.size() - 1);
        return new DefaultEmptySymbol(id, first.getLeft(), last.getRight());
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, Object value) {
        var def = (DefaultSymbol) left;
        return new DefaultObjectSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, boolean value) {
        var def = (DefaultSymbol) left;
        return new DefaultBoolSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, byte value) {
        var def = (DefaultSymbol) left;
        return new DefaultByteSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, short value) {
        var def = (DefaultSymbol) left;
        return new DefaultShortSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, char value) {
        var def = (DefaultSymbol) left;
        return new DefaultCharSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, int value) {
        var def = (DefaultSymbol) left;
        return new DefaultIntSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, long value) {
        var def = (DefaultSymbol) left;
        return new DefaultLongSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, float value) {
        var def = (DefaultSymbol) left;
        return new DefaultFloatSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left, double value) {
        var def = (DefaultSymbol) left;
        return new DefaultDoubleSymbol(id, def.getLeft(), def.getRight(), value);
    }

    @Override
    public Symbol newSymbol(int id, Symbol left) {
        var def = (DefaultSymbol) left;
        return new DefaultEmptySymbol(id, def.getLeft(), def.getRight());
    }

    @Override
    public Symbol newSymbol(int id, Object value) {
        return new DefaultObjectSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, boolean value) {
        return new DefaultBoolSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, byte value) {
        return new DefaultByteSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, short value) {
        return new DefaultShortSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, char value) {
        return new DefaultCharSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, int value) {
        return new DefaultIntSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, long value) {
        return new DefaultLongSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, float value) {
        return new DefaultFloatSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id, double value) {
        return new DefaultDoubleSymbol(id, value);
    }

    @Override
    public Symbol newSymbol(int id) {
        return new DefaultEmptySymbol(id);
    }

    @Override
    public Symbol startSymbol(int id, int state) {
        return new DefaultEmptySymbol(id, state);
    }

}