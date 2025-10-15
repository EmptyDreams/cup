package java_cup.runtime;

/**
 * Defines the Symbol class, which is used to represent all terminals and
 * nonterminals while parsing. The lexer should pass CUP Symbols and CUP returns
 * a Symbol.
 * <p>
 * This is an abstract base class that contains only the symbol type [sym].
 * All other attributes such as value, left/right positions and their corresponding
 * Objects have been moved to concrete subclasses for optional implementation.
 * </p>
 * <p>
 * Subclasses provide specialized implementations for different symbol types:
 * </p>
 * <ul>
 *   <li>Complex symbols ([ComplexSymbol] - using [Location] objects for positions</li>
 * </ul>
 *
 * @author Frank Flannery, kmar
 * @version last updated: 14/10/2025
 */
public abstract class Symbol {

    /**
     * Constructor for no value or l,r
     */
    public Symbol(int sym_num) {
        this.sym = sym_num;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** The symbol number of the terminal or non terminal being represented */
    public final int sym;

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /* The data passed to parser */

    /**
     * Checks whether the symbol's value is null.
     * <p>
     * Unless your design requires it, you should always return false for symbol classes specialized for primitive types.
     */
    public abstract boolean isNull();

    /**
     * Equivalent of just retrieving the value directly, but does the typecast here;
     * Removes lots of unchecked cast warnings from the actual parser class by using this one function
     *
     * @param <T> the type of the value to be casted to
     * @return just the value, like the attribute of the same name
     */
    public abstract <T> T value();

    /**
     * Get the value as a boolean.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Boolean or boolean
     */
    public boolean getAsBoolean() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to boolean");
    }

    /**
     * Get the value as a byte.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Byte or byte
     */
    public byte getAsByte() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to byte");
    }

    /**
     * Get the value as a short.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Short or short
     */
    public short getAsShort() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to short");
    }

    /**
     * Get the value as an int.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Integer or int
     */
    public int getAsInt() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to int");
    }

    /**
     * Get the value as a long.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Long or long
     */
    public long getAsLong() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to long");
    }

    /**
     * Get the value as a float.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Float or float
     */
    public float getAsFloat() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to float");
    }

    /**
     * Get the value as a double.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Double or double
     */
    public double getAsDouble() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to double");
    }

    /**
     * Get the value as a char.
     * <br/>
     * Hermits and casts between numeric types are not supported, and automatic unboxing is supported.
     *
     * @throws ClassCastException if the stored value is not Character or char
     */
    public char getAsChar() {
        throw new ClassCastException(getClass().getSimpleName() + " value can not cast to char");
    }

    /**
     * Report a non fatal error (or warning). This method takes a message string and
     * an additional object (to be used by specializations implemented in
     * subclasses). Here in the base class a very simple implementation is provided
     * which simply prints the message to System.err.
     *
     * @param message an error message.
     */
    public void reportError(String message) {
        System.err.println(message);
    }

    /*****************************
     * Printing this token out. (Override for pretty-print).
     ****************************/
    @Override
    public String toString() {
        return "#" + sym;
    }

}