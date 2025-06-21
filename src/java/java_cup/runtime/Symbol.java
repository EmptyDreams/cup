package java_cup.runtime;

/**
 * Defines the Symbol class, which is used to represent all terminals and
 * nonterminals while parsing. The lexer should pass CUP Symbols and CUP returns
 * a Symbol.
 *
 * @author Frank Flannery, kmar
 * @version last updated: 21/6/25
 */

/*
 * **************************************************************** Class Symbol
 * what the parser expects to receive from the lexer. the token is identified as
 * follows: sym: the symbol type parse_state: the parse state. value: is the
 * lexical value of type Object left : is the left position in the original
 * input file right: is the right position in the original input file xleft: is
 * the left position Object in the original input file xright: is the left
 * position Object in the original input file
 ******************************************************************/

public abstract class Symbol {

    /**
     * Constructor for no value or l,r
     */
    public Symbol(int sym_num) {
        this(sym_num, -1);
    }

    /**
     * Constructor to give a start state
     */
    protected Symbol(int sym_num, int state) {
        sym = sym_num;
        parse_state = state;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** The symbol number of the terminal or non terminal being represented */
    public int sym;

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * The parse state to be recorded on the parse stack with this symbol. This
     * field is for the convenience of the parser and shouldn't be modified except
     * by the parser.
     */
    public int parse_state;
    /**
     * This allows us to catch some errors caused by scanners recycling symbols. For
     * the use of the parser only. [CSA, 23-Jul-1999]
     */
    boolean used_by_parser = false;

    /* The data passed to parser */

    /**
     * Checks whether the symbol's value is null.
     * <p>
     * Unless your design requires it, you should always return true for symbol classes specialized for primitive types.
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

    /*****************************
     * Printing this token out. (Override for pretty-print).
     ****************************/
    @Override
    public String toString() {
        return "#" + sym;
    }

}