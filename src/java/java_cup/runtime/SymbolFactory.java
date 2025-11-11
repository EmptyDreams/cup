package java_cup.runtime;

import java_cup.runtime.symbol.Location;

import java.util.List;

/**
 * Interface for creating new symbols. You can also use this interface for your
 * own callback hooks. Declare your own factory methods for creation of Objects
 * in your scanner!
 * <p>
 * This factory provides two sets of methods for creating symbols:
 * <ul>
 * <li>Methods accepting {@link Location} parameters are intended for lexical analyzer
 *     to create terminal symbols with specific positions</li>
 * <li>Methods accepting {@link Symbol} or {@link List}&lt;{@link Symbol}&gt; parameters are 
 *     intended for parser to create non-terminal symbols by reducing other symbols</li>
 * </ul>
 * <p>
 * For terminal symbol creation, specialized methods for primitive types are provided
 * to optimize performance in the lexical analysis phase where storing primitive values
 * efficiently is more important than in the parsing phase.
 *
 * @author Michael Petter, kmar
 * @version last updated 12-06-2025
 */
public interface SymbolFactory {

    /**
     * getTerminalName returns the name of the terminal with the given id
     *
     * @param id the terminal id
     * @return null if no symbol name is available for this id
     */
    String getTerminalName(int id);

    /**
     * getNonTerminalName returns the name of the non_terminal with the given id
     *
     * @param id the non-terminal id
     * @return null if no symbol name is available for this id
     */
    String getNonTerminalName(int id);

    /**
     * newSymbol creates a symbol with a value, grouping other symbols with
     * left/right locations; used frequently by the parser to implement non-terminal
     * symbols
     * <p>
     * This method is designed for parser to create non-terminal symbols by reducing
     * a list of symbols.
     *
     * @param id      the symbol id assigned by CUP
     * @param symList symbols, list of symbols included in this reduce
     * @param value   value, attached to this symbol
     * @return a new non-terminal symbol representing the reduction of the symbols in symList
     */
    Symbol newSymbol(int id, List<Symbol> symList, Object value);

    /**
     * newSymbol creates a symbol, grouping other symbols with left/right locations;
     * used frequently by the parser to implement non-terminal symbols
     * <p>
     * This method is designed for parser to create non-terminal symbols by reducing
     * a list of symbols.
     *
     * @param id      the symbol id assigned by CUP
     * @param symList symbols, list of symbols included in this reduce
     * @return a new non-terminal symbol representing the reduction of the symbols in symList
     */
    Symbol newSymbol(int id, List<Symbol> symList);

    /**
     * newSymbol creates a symbol for an empty production, taking its location from
     * the Symbol on the left
     * <p>
     * This method is designed for parser to create non-terminal symbols for empty productions.
     *
     * @param id    the symbol id assigned by CUP
     * @param left  symbol, to take the left location from
     * @param value value, attached to this symbol
     * @return a new non-terminal symbol for an empty production
     */
    Symbol newSymbol(int id, Symbol left, Object value);

    /**
     * newSymbol creates a symbol for an empty value, taking its location from
     * the Symbol on the left
     * <p>
     * This method is designed for parser to create non-terminal symbols for empty productions.
     *
     * @param id   the symbol id assigned by CUP
     * @param left symbol, to take the left location from
     * @return a new non-terminal symbol for an empty production
     */
    Symbol newSymbol(int id, Symbol left);

    /**
     * Creates a symbol with an Object value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated object values.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with an Object value
     */
    Symbol newSymbol(int id, Location location, Object value);

    /**
     * Creates a symbol with a byte value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated byte values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a byte value
     */
    Symbol newSymbol(int id, Location location, byte value);

    /**
     * Creates a symbol with a short value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated short values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a short value
     */
    Symbol newSymbol(int id, Location location, short value);

    /**
     * Creates a symbol with an int value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated int values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with an int value
     */
    Symbol newSymbol(int id, Location location, int value);

    /**
     * Creates a symbol with a long value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated long values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a long value
     */
    Symbol newSymbol(int id, Location location, long value);

    /**
     * Creates a symbol with a float value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated float values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a float value
     */
    Symbol newSymbol(int id, Location location, float value);

    /**
     * Creates a symbol with a double value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated double values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a double value
     */
    Symbol newSymbol(int id, Location location, double value);

    /**
     * Creates a symbol with a char value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated char values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a char value
     */
    Symbol newSymbol(int id, Location location, char value);

    /**
     * Creates a symbol with a boolean value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols with
     * associated boolean values, optimizing storage for primitive types in the lexical
     * analysis phase.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @param value    value, attached to this symbol
     * @return a new terminal symbol with a boolean value
     */
    Symbol newSymbol(int id, Location location, boolean value);

    /**
     * Creates a symbol without a value at a specific location.
     * <p>
     * This method is designed for lexical analyzer to create terminal symbols without
     * associated values.
     *
     * @param id       the symbol id assigned by CUP
     * @param location position information for this symbol
     * @return a new terminal symbol without a value
     */
    Symbol newSymbol(int id, Location location);

}