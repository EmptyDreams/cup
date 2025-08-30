package java_cup;

import java_cup.Production.PositionFinder;

import java.util.*;

/**
 * This class represents a non-terminal symbol in the grammar. Each non terminal
 * has a textual name, an index, and a string which indicates the type of object
 * it will be implemented with at runtime (i.e. the class of object that will be
 * pushed on the parse stack to represent it).
 *
 * @author Scott Hudson
 * @version last updated: 11/25/95
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

    private static final Map<String, non_terminal> _useRhsCache = new HashMap<>();
    /**
     * <p>Stores labels and action code blocks for each anonymous non-terminal.
     * <p>The key is the production in which the anonymous non-terminal resides,
     * supporting different labels and actions for the same anonymous non-terminal
     * used in different positions.
     * <p>The value is a sequence of all anonymous
     * non-terminals contained in that production.
     */
    private static final Map<PositionFinder, List<AnnoNtInfo>> _annoLabels = new HashMap<>();
    private static Map<Production, List<AnnoNtInfo>> _annoLabelsCache;

    /**
     * Reads label and action information for all inline expressions contained in the specified production
     *
     * @return The key is the number
     */
    public static List<AnnoNtInfo> getAnnoLabelAndAction(Production prod) {
        if (_annoLabelsCache == null) {
            Map<Production, List<AnnoNtInfo>> cache = new HashMap<>();
            for (var entry : _annoLabels.entrySet()) {
                cache.put(entry.getKey().getProd(), entry.getValue());
            }
            _annoLabelsCache = cache;
        }
        return _annoLabelsCache.get(prod);
    }

    /**
     * Get all information about the anonymous nonterminal
     * @param nt the anonymous nonterminal
     * @return all information about the anonymous nonterminal
     */
    public static List<ObjectPair<PositionFinder, AnnoNtInfo>> getAnnoNtAllInfo(non_terminal nt) {
        var list = new ArrayList<ObjectPair<PositionFinder, AnnoNtInfo>>();
        for (var entry : _annoLabels.entrySet()) {
            for (var info : entry.getValue()) {
                if (info.getNt() == nt) {
                    list.add(new ObjectPair<>(entry.getKey(), info));
                }
            }
        }
        return list;
    }

    boolean _isAnno = false;
    private boolean _isLaAnno = false;

    /**
     * Checks if the current nonterminal is an annoying nonterminal
     */
    public boolean isAnno() {
        return _isAnno;
    }

    /**
     * Check if the nonterminal is an annoying expression containing a label or action.
     */
    public boolean isLaAnno() {
        return _isLaAnno;
    }

    /**
     * This method is used to create a child non_terminal of the current non_terminal from the production_part sequence,
     * sharing the same non_terminal object if the passed sequence has already been used globally.
     *
     * @return the child non_terminal, if the nt object is newly created, it does not contain any production
     */
    non_terminal createSubNt(
        production_part[] parts, int length, PositionFinder posFinder
    ) throws internal_error {
        boolean hasLabel = false;
        boolean hasAction = length != 0 && parts[length - 1].is_action();
        if (hasAction) --length;
        List<String> labels = new ArrayList<>(length);
        StringBuilder sb = new StringBuilder();
        sb.append("vn").append(length);
        String action = null;
        for (int i = 0; i < length; i++) {
            var part = parts[i];
            if (part.is_action()) {
                if (i != length - 1 && !parts[i + 1].is_action())
                    throw new internal_error("Inline action can only appear at the end of an inline production");
                continue;
            }
            sb.append(((symbol_part) part).the_symbol().name()).append("|\"s\"|");
            if (part.label() != null) {
                hasLabel = true;
            }
            labels.add(part.label());
            part._label = null;
        }
        if (hasAction) {
            action = ((action_part) parts[length]).code_string();
        }
        var cacheKey = sb.toString();
        var subNt = _useRhsCache.computeIfAbsent(
            cacheKey,
            k -> non_terminal.create_new(
                "_EBNF_",
                Main.ast_format == null ? "Object" : "IAstNode"
            )
        );
        subNt._isAnno = true;
        subNt._isLaAnno = true;
        emit.hasAnnoCode = true;
        if (hasLabel || hasAction) {
            var infoList = _annoLabels.computeIfAbsent(
                posFinder,
                k -> new LinkedList<>()
            );
            var info = new AnnoNtInfo(subNt, infoList.size(), labels, action);
            infoList.add(info);
        }
        return subNt;
    }

    /**
     * This method is used to create a child nonterminal of the current nonterminal from the production_part list.
     * <p>
     * Each item in the production_part list is the complete content of a production of that child nonterminal.
     *
     * @return the child non_terminal, if the nt object is newly created, it does not contain any production
     */
    non_terminal createSubNts(List<production_part> subNtList) {
        StringBuilder sb = new StringBuilder();
        sb.append("vl").append(subNtList.size());
        for (production_part part : subNtList) {
            sb.append(((symbol_part) part).the_symbol().name()).append("|\"l\"|");
        }
        var cacheKey = sb.toString();
        var subNt = _useRhsCache.computeIfAbsent(
            cacheKey,
            k -> non_terminal.create_new(
                "_EBNF_",
                Main.ast_format == null ? "Object" : "IAstNode"
            )
        );
        subNt._isAnno = true;
        return subNt;
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
                /* only look at things that aren't already marked nullable */ {
                if (!nt.nullable()) {
                    if (nt.looks_nullable()) {
                        nt._nullable = true;
                        change = true;
                    }
                }
            }
        }

        /* do one last pass over the productions to finalize all of them */
        for (Production prod : Production.all()) {
            prod.set_nullable(prod.check_nullable());
        }
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
                for (Production prod : nt.productions()) {
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
    protected Set<Production> _productions = new LinkedHashSet<>(11);

    /**
     * Access to productions with this non terminal on the LHS.
     *
     * <p>Traverses in the order of addition.
     */
    public Iterable<Production> productions() {
        return _productions;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Total number of productions with this non terminal on the LHS. */
    public int num_productions() {
        return _productions.size();
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Add a production to our set of productions. */
    public void add_production(Production prod) throws internal_error {
        /* catch improper productions */
        if (prod == null || prod.lhs() == null || prod.lhs().the_symbol() != this)
            throw new internal_error("Attempt to add invalid production to non terminal production table");

        /* add it to the table, keyed with itself */
        _productions.add(prod);
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
        for (Production prod : productions())
            /* if the production can go to empty, we are nullable */ {
            if (prod.check_nullable())
                return true;
        }

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