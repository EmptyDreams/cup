package java_cup;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a non-terminal symbol in the grammar. Each non terminal
 * has a textual name, an index, and a string which indicates the type of object
 * it will be implemented with at runtime (i.e. the class of object that will be
 * pushed on the parse stack to represent it).
 *
 * @version last updated: 11/25/95
 * @author Scott Hudson
 */

public class non_terminal extends symbol {

  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Full constructor.
   * 
   * @param nm the name of the non terminal.
   * @param tp the type string for the non terminal.
   */
  public non_terminal(String nm, String tp) {
    /* super class does most of the work */
    super(nm, tp);

    /* add to set of all non terminals and check for duplicates */
    Object conflict = _all.put(nm, this);
    if (conflict != null)
      // can't throw an exception here because these are used in static
      // initializers, so we crash instead
      // was:
      // throw new internal_error("Duplicate non-terminal ("+nm+") created");
      (new internal_error("Duplicate non-terminal (" + nm + ") created")).crash();

    /* assign a unique index */
    _index = next_index++;

    /* add to by_index set */
    _all_by_index.put(_index, this);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Constructor with default type.
   * 
   * @param nm the name of the non terminal.
   */
  public non_terminal(String nm) {
    this(nm, null);
  }

  /*-----------------------------------------------------------*/
  /*--- (Access to) Static (Class) Variables ------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Table of all non-terminals -- elements are stored using name strings as the
   * key
   */
  protected static Map<String, non_terminal> _all = new HashMap<>();

  // Hm Added clear to clear all static fields
  public static void clear() {
    _all.clear();
    _all_by_index.clear();
    next_index = 0;
    next_nt = 0;
  }

  /** Access to all non-terminals. */
  public static Iterable<non_terminal> all() {
    return _all.values();
  }

  /** lookup a non terminal by name string */
  public static non_terminal find(String with_name) {
    if (with_name == null)
      return null;
    else
      return _all.get(with_name);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Table of all non terminals indexed by their index number. */
  protected static MonotonicIntObjectArrayMap<non_terminal> _all_by_index = new MonotonicIntObjectArrayMap<>();

  /** Lookup a non terminal by index. */
  public static non_terminal find(int indx) {
    return _all_by_index.get(indx);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Total number of non-terminals. */
  public static int number() {
    return _all.size();
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Static counter to assign unique indexes. */
  protected static int next_index = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Static counter for creating unique non-terminal names */
  static protected int next_nt = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** special non-terminal for start symbol */
  public static final non_terminal START_nt = new non_terminal("$START");

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** flag non-terminals created to embed action productions */
  public boolean is_embedded_action = false; /* added 24-Mar-1998, CSA */

  /*-----------------------------------------------------------*/
  /*--- Static Methods ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Method for creating a new uniquely named hidden non-terminal using the given
   * string as a base for the name (or "NT$" if null is passed).
   * 
   * @param prefix base name to construct unique name from.
   */
  static non_terminal create_new(String prefix) throws internal_error {
    return create_new(prefix, null); // TUM 20060608 embedded actions patch
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** static routine for creating a new uniquely named hidden non-terminal */
  static non_terminal create_new() throws internal_error {
    return create_new(null);
  }

  /**
   * TUM 20060608 bugfix for embedded action codes
   */
  static non_terminal create_new(String prefix, String type) {
    if (prefix == null)
      prefix = "NT$";
    return new non_terminal(prefix + next_nt++, type);
  }
  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Compute nullability of all non-terminals. */
  public static void compute_nullability() throws internal_error {
    boolean change = true;
    /* repeat this process until there is no change */
    while (change) {
      /* look for a new change */
      change = false;

      /* consider each non-terminal */
      for (non_terminal nt : all())
        /* only look at things that aren't already marked nullable */
        if (!nt.nullable())
          if (nt.looks_nullable()) {
            nt._nullable = true;
            change = true;
          }

    }

    /* do one last pass over the productions to finalize all of them */
    for (production prod : production.all())
      prod.set_nullable(prod.check_nullable());
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Compute first sets for all non-terminals. This assumes nullability has
   * already computed.
   */
  public static void compute_first_sets() throws internal_error {
    boolean change = true;
    /* repeat this process until we have no change */
    while (change) {
      /* look for a new change */
      change = false;

      /* consider each non-terminal */
      for (non_terminal nt : all()) {
        /* consider every production of that non terminal */
        for (production prod : nt.productions()) {
          /* get the updated first of that production */
          terminal_set prod_first = prod.check_first_set();

          /* if this going to add anything, add it */
          if (!prod_first.is_subset_of(nt._first_set)) {
            change = true;
            nt._first_set.add(prod_first);
          }
        }
      }
    }
  }

  /*-----------------------------------------------------------*/
  /*--- (Access to) Instance Variables ------------------------*/
  /*-----------------------------------------------------------*/

  /** Table of all productions with this non terminal on the LHS. */
  protected Map<production, production> _productions = new HashMap<>(11);

  /** Access to productions with this non terminal on the LHS. */
  public Iterable<production> productions() {
    return _productions.values();
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Total number of productions with this non terminal on the LHS. */
  public int num_productions() {
    return _productions.size();
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Add a production to our set of productions. */
  public void add_production(production prod) throws internal_error {
    /* catch improper productions */
    if (prod == null || prod.lhs() == null || prod.lhs().the_symbol() != this)
      throw new internal_error("Attempt to add invalid production to non terminal production table");

    /* add it to the table, keyed with itself */
    _productions.put(prod, prod);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Nullability of this non terminal. */
  protected boolean _nullable;

  /** Nullability of this non terminal. */
  public boolean nullable() {
    return _nullable;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** First set for this non-terminal. */
  protected terminal_set _first_set = new terminal_set();

  /** First set for this non-terminal. */
  public terminal_set first_set() {
    return _first_set;
  }

  /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Indicate that this symbol is a non-terminal. */
  @Override
  public boolean is_non_term() {
    return true;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Test to see if this non terminal currently looks nullable. */
  protected boolean looks_nullable() throws internal_error {
    /* look and see if any of the productions now look nullable */
    for (production prod : productions())
      /* if the production can go to empty, we are nullable */
      if (prod.check_nullable())
        return true;

    /* none of the productions can go to empty, so we are not nullable */
    return false;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** convert to string */
  @Override
  public String toString() {
    return super.toString() + "[" + index() + "]" + (nullable() ? "*" : "");
  }

  /*-----------------------------------------------------------*/
}