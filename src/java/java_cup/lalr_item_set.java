
package java_cup;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class represents a set of LALR items. For purposes of building these
 * sets, items are considered unique only if they have unique cores (i.e.,
 * ignoring differences in their lookahead sets).
 * <p>
 *
 * This class provides fairly conventional set oriented operations (union,
 * sub/super-set tests, etc.), as well as an LALR "closure" operation (see
 * compute_closure()).
 *
 * @see java_cup.lalr_item
 * @see java_cup.lalr_state
 * @version last updated: 3/6/96
 * @author Scott Hudson
 */

public class lalr_item_set implements Iterable<lalr_item> {

  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Constructor for an empty set. */
  public lalr_item_set() {
    _all = new HashMap<>(11);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Constructor for cloning from another set.
   * 
   * @param other indicates set we should copy from.
   */
  public lalr_item_set(lalr_item_set other) throws internal_error {
    not_null(other);
    _all = new HashMap<>(other._all);
    hashcode_cache = other.hashcode_cache;
  }

  /*-----------------------------------------------------------*/
  /*--- (Access to) Instance Variables ------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * A hash map to implement the set. We store the items using themselves as
   * keys.
   */
  protected Map<lalr_item, lalr_item> _all;

  /** Access to all elements of the set. */
  @Override
  public Iterator<lalr_item> iterator() {
    return _all.values().iterator();
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Cached hashcode for this set. */
  protected Integer hashcode_cache = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Size of the set */
  public int size() {
    return _all.size();
  }

  /*-----------------------------------------------------------*/
  /*--- Set Operation Methods ---------------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Does the set contain a particular item?
   * 
   * @param itm the item in question.
   */
  public boolean contains(lalr_item itm) {
    return _all.containsKey(itm);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Return the item in the set matching a particular item (or null if not found)
   * 
   * @param itm the item we are looking for.
   */
  public lalr_item find(lalr_item itm) {
    return _all.get(itm);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Is this set an (improper) subset of another?
   * 
   * @param other the other set in question.
   */
  public boolean is_subset_of(lalr_item_set other) throws internal_error {
    not_null(other);
    /* walk down our set and make sure every element is in the other */
    for (lalr_item e : this)
      if (!other.contains(e))
        return false;
    /* they were all there */
    return true;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Is this set an (improper) superset of another?
   * 
   * @param other the other set in question.
   */
  public boolean is_superset_of(lalr_item_set other) throws internal_error {
    not_null(other);
    return other.is_subset_of(this);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Add a singleton item, merging lookahead sets if the item is already part of
   * the set. returns the element of the set that was added or merged into.
   * 
   * @param itm the item being added.
   */
  public lalr_item add(lalr_item itm) throws internal_error {
    lalr_item other;

    not_null(itm);

    /* see if an item with a matching core is already there */
    other = _all.get(itm);

    /* if so, merge this lookahead into the original and leave it */
    if (other != null) {
      other.lookahead().add(itm.lookahead());
      return other;
    }
    /* otherwise we just go in the set */
    else {
      /* invalidate cached hashcode */
      hashcode_cache = null;
      _all.put(itm, itm);
      return itm;
    }
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Remove a single item if it is in the set.
   * 
   * @param itm the item to remove.
   */
  public void remove(lalr_item itm) throws internal_error {
    not_null(itm);

    /* invalidate cached hashcode */
    hashcode_cache = null;

    /* remove it from hash table implementing set */
    _all.remove(itm);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Add a complete set, merging lookaheads where items are already in the set
   * 
   * @param other the set to be added.
   */
  public void add(lalr_item_set other) throws internal_error {
    not_null(other);

    /* walk down the other set and do the adds individually */
    for (lalr_item e : other)
      add(e);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Remove (set subtract) a complete set.
   * 
   * @param other the set to remove.
   */
  public void remove(lalr_item_set other) throws internal_error {
    not_null(other);

    /* walk down the other set and do the removes individually */
    for (lalr_item e : other)
      remove(e);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Remove and return one item from the set (done in hash order). */
  public lalr_item get_one() throws internal_error {
    if (_all.isEmpty())
      return null;
    var result = iterator().next();
    remove(result);
    return result;
  }

  /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Helper function for null test. Throws an interal_error exception if its
   * parameter is null.
   * 
   * @param obj the object we are testing.
   */
  protected void not_null(Object obj) throws internal_error {
    if (obj == null)
      throw new internal_error("Null object used in set operation");
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Compute the closure of the set using the LALR closure rules. Basically for
   * every item of the form:
   * 
   * <pre>
   *    [L ::= a *N alpha, l]
   * </pre>
   * 
   * (where N is a a non terminal and alpha is a string of symbols) make sure
   * there are also items of the form:
   * 
   * <pre>
   *    [N ::= *beta, first(alpha l)]
   * </pre>
   * 
   * corresponding to each production of N. Items with identical cores but
   * differing lookahead sets are merged by creating a new item with the same core
   * and the union of the lookahead sets (the LA in LALR stands for "lookahead
   * merged" and this is where the merger is). This routine assumes that
   * nullability and first sets have been computed for all productions before it
   * is called.
   */
  public void compute_closure() throws internal_error {
    /* invalidate cached hashcode */
    hashcode_cache = null;

    /* each current element needs to be considered */
    var consider = new lalr_item_set(this);

    /* repeat this until there is nothing else to consider */
    while (consider.size() > 0) {
      /* get one item to consider */
      var itm = consider.get_one();

      /* do we have a dot before a non terminal */
      var nt = itm.dot_before_nt();
      if (nt != null) {
        /* create the lookahead set based on first after dot */
        var new_lookaheads = itm.calc_lookahead(itm.lookahead());

        /* are we going to need to propagate our lookahead to new item */
        var need_prop = itm.lookahead_visible();

        /* create items for each production of that non term */
        for (var prod : nt.productions()) {

          /* create new item with dot at start and that lookahead */
          var new_itm = new lalr_item(prod, new terminal_set(new_lookaheads));

          /* add/merge item into the set */
          var add_itm = add(new_itm);
          /* if propagation is needed link to that item */
          if (need_prop)
            itm.add_propagate(add_itm);

          /* was this was a new item */
          if (add_itm == new_itm) {
            /* that may need further closure, consider it also */
            consider.add(new_itm);
          }
        }
      }
    }
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Equality comparison. */
  public boolean equals(lalr_item_set other) {
    if (other == null || other.size() != size())
      return false;

    /* once we know they are the same size, then improper subset does test */
    try {
      return is_subset_of(other);
    } catch (internal_error e) {
      /* can't throw error from here (because superclass doesn't) so crash */
      e.crash();
      return false;
    }

  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Generic equality comparison. */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof lalr_item_set))
      return false;
    else
      return equals((lalr_item_set) other);
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Return hash code. */
  @Override
  public int hashCode() {
    int result = 0;

    /* only compute a new one if we don't have it cached */
    if (hashcode_cache == null) {
      /* hash together codes from at most first 5 elements */
      // CSA fix! we'd *like* to hash just a few elements, but
      // that means equal sets will have inequal hashcodes, which
      // we're not allowed (by contract) to do. So hash them all.
      for (var e : this)
        result ^= e.hashCode();

      hashcode_cache = result;
    }

    return hashcode_cache;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Convert to string. */
  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    result.append("{\n");
    for (var e : this)
      result.append("  ").append(e).append('\n');

    result.append('}');
    return result.toString();
  }
  /*-----------------------------------------------------------*/
}