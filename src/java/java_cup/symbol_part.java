package java_cup;

/**
 * This class represents a part of a production which is a symbol (terminal or
 * non terminal). This simply maintains a reference to the symbol in question.
 *
 * @author Scott Hudson
 * @version last updated: 11/25/95
 * @see Production
 */
public class symbol_part extends production_part {

    /*-----------------------------------------------------------*/
    /*--- Constructor(s) ----------------------------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Full constructor.
     *
     * @param sym the symbol that this part is made up of.
     * @param lab an optional label string for the part.
     */
    public symbol_part(symbol sym, String lab) throws internal_error {
        this(sym, lab, null);
    }

    public symbol_part(symbol sym, String lab, String type) throws internal_error {
        super(lab, type);
        if (sym == null)
            throw new internal_error("Attempt to construct a symbol_part with a null symbol");
        _the_symbol = sym;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Constructor with no label.
     *
     * @param sym the symbol that this part is made up of.
     */
    public symbol_part(symbol sym) throws internal_error {
        this(sym, null);
    }

    /*-----------------------------------------------------------*/
    /*--- (Access to) Instance Variables ------------------------*/
    /*-----------------------------------------------------------*/

    /** The symbol that this part is made up of. */
    protected symbol _the_symbol;

    /** The symbol that this part is made up of. */
    public symbol the_symbol() {
        return _the_symbol;
    }

    private boolean isInline = false;

    public boolean isInline() {
        return isInline;
    }

    public void markInline() {
        isInline = true;
    }

    /*-----------------------------------------------------------*/
    /*--- General Methods ---------------------------------------*/
    /*-----------------------------------------------------------*/

    /** Respond that we are not an action part. */
    @Override
    public boolean is_action() {
        return false;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Equality comparison. */
    public boolean equals(symbol_part other) {
        return other != null && super.equals(other) && the_symbol().equals(other.the_symbol());
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Generic equality comparison. */
    @Override
    public boolean equals(Object other) {
        return other instanceof symbol_part && equals((symbol_part) other);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Produce a hash code. */
    @Override
    public int hashCode() {
        return super.hashCode() ^ (the_symbol() == null ? 0 : the_symbol().hashCode());
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Convert to a string. */
    @Override
    public String toString() {
        return the_symbol() != null ? super.toString() + the_symbol() : super.toString() + "$$MISSING-SYMBOL$$";
    }

    /*-----------------------------------------------------------*/

}