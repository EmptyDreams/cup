
package java_cup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class represents a production in the grammar. It contains a LHS non
 * terminal, and an array of RHS symbols. As various transformations are done on
 * the RHS of the production, it may shrink. As a result a separate length is
 * always maintained to indicate how much of the RHS array is still valid.
 * <p>
 * I addition to construction and manipulation operations, productions provide
 * methods for factoring out actions (see remove_embedded_actions()), for
 * computing the nullability of the production (i.e., can it derive the empty
 * string, see check_nullable()), and operations for computing its first set
 * (i.e., the set of terminals that could appear at the beginning of some string
 * derived from the production, see check_first_set()).
 *
 * @author Frank Flannery
 * @version last updated: 7/3/96
 * @see java_cup.production_part
 * @see java_cup.symbol_part
 * @see java_cup.action_part
 */
public class Production {

    /*-----------------------------------------------------------*/
    /*--- Constructor(s) ----------------------------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Full constructor. This constructor accepts a LHS non terminal, an array of
     * RHS parts (including terminals, non terminals, and actions), and a string for
     * a final reduce action. It does several manipulations in the process of
     * creating a production object. After some validity checking it translates
     * labels that appear in actions into code for accessing objects on the runtime
     * parse stack. It them merges adjacent actions if they appear and moves any
     * trailing action into the final reduce actions string. Next it removes any
     * embedded actions by factoring them out with new action productions. Finally
     * it assigns a unique index to the production.
     * <p>
     * Factoring out of actions is accomplished by creating new "hidden" non
     * terminals. For example if the production was originally:
     *
     * <pre>
     *    A ::= B {action} C D
     * </pre>
     *
     * then it is factored into two productions:
     *
     * <pre>
     *    A ::= B X C D
     *    X ::= {action}
     * </pre>
     *
     * (where X is a unique new non terminal). This has the effect of placing all
     * actions at the end where they can be handled as part of a reduce by the
     * parser.
     */
    public Production(
        non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str
    ) throws internal_error {
        int rightlen = rhs_l;
        StringBuilder actionBuilder = new StringBuilder(64);
        if (action_str != null) {
            actionBuilder.append(action_str);
        }

        /* remember the length */
        if (rhs_l >= 0)
            _rhs_length = rhs_l;
        else if (rhs_parts != null)
            _rhs_length = rhs_parts.length;
        else
            _rhs_length = 0;

        /* make sure we have a valid left-hand-side */
        if (lhs_sym == null)
            throw new internal_error("Attempt to construct a production with a null LHS");

        /*
         * I'm not translating labels anymore, I'm adding code to declare labels as
         * valid variables. This way, the users code string is untouched 6/96 frankf
         */

        /*
         * check if the last part of the right hand side is an action. If it is, it
         * won't be on the stack, so we don't want to count it in the rightlen. Then
         * when we search down the stack for a Symbol, we don't try to search past
         * action
         */

        if (rhs_l > 0 && rhs_parts[rhs_l - 1].is_action()) {
            rightlen = rhs_l - 1;
        }

        /* get the generated declaration code for the necessary labels. */
        String declare_str = declare_labels(rhs_parts, rightlen, action_str);
        actionBuilder.insert(0, declare_str);

        /* count use of lhs */
        lhs_sym.note_use();

        /* create the part for left-hand-side */
        _lhs = new symbol_part(lhs_sym);

        /* merge adjacent actions (if any) */
        _rhs_length = merge_adjacent_actions(rhs_parts, _rhs_length);

        /* strip off any trailing action */
        action_part tail_action = strip_trailing_action(rhs_parts, _rhs_length);
        if (tail_action != null)
            _rhs_length--;

        /*
         * Why does this run through the right hand side happen over and over? here a
         * quick combination of two prior runs plus one I wanted of my own frankf
         * 6/25/96
         */
        /* allocate and copy over the right-hand-side */
        /* count use of each rhs symbol */
        _rhs = new production_part[_rhs_length];
        for (int i = 0; i < _rhs_length; i++) {
            //noinspection DataFlowIssue
            _rhs[i] = rhs_parts[i];
            if (!_rhs[i].is_action()) {
                ((symbol_part) _rhs[i]).the_symbol().note_use();
                if (((symbol_part) _rhs[i]).the_symbol() instanceof terminal) {
                    _rhs_prec = ((terminal) ((symbol_part) _rhs[i]).the_symbol()).precedence_num();
                    _rhs_assoc = ((terminal) ((symbol_part) _rhs[i]).the_symbol()).precedence_side();
                }
            }
        }

        /*
         * now action string is really declaration string, so put it in front! 6/14/96
         * frankf
         */
        if (tail_action != null && tail_action.code_string() != null) {
            hasTailAction = true;
            actionBuilder.append("\t\t").append(tail_action.code_string());
        } else {
            hasTailAction = action_str != null;
        }
        _action = new LazyContainer<>(() -> {
            if (Main.ast_format != null && !hasTailAction) {
                String indentation = "              ";
                if (isNull()) {
                    actionBuilder.append(indentation)
                        .append("RESULT = new ")
                        .append(lhs_sym.astClassName())
                        .append("();\n");
                } else {
                    String className = lhs_sym.astClassName();
                    String nodeName = emit.pre("treeNode");
                    var partMap = getLabel2SymbolPartMap();
                    if (partMap.isEmpty()) {
                        actionBuilder.append(indentation)
                            .append(className).append(' ').append(nodeName)
                            .append(" = new ").append(className).append("();\n");
                    } else {
                        // Xxx xxx = Xxx._xx(
                        actionBuilder.append(indentation)
                            .append(className).append(' ').append(nodeName).append(" = ")
                            .append(className).append(".build").append(getProdName()).append("(\n");
                        boolean isFirst = true;
                        for (var entry : partMap.entrySet()) {
                            var label = entry.getKey();
                            var part = entry.getValue();
                            if (part.isExistCheck()) continue;
                            if (isFirst) isFirst = false;
                            else actionBuilder.append(",\n");
                            actionBuilder.append(indentation).append("  ").append(emit.joinName(label, "sym"));
                        }
                        actionBuilder.append('\n').append(indentation).append(");\n");
                    }
                    actionBuilder.append(indentation).append("RESULT = ").append(nodeName).append(';');
                }
            }
            /* stash the action */
            return new action_part(actionBuilder.toString());
        });

        /* rewrite production to remove any embedded actions */
        remove_embedded_actions();

        /* assign an index */
        _index = next_index++;

        /* put us in the global collection of productions */
        _all.put(_index, this);

        /* put us in the production list of the lhs non terminal */
        lhs_sym.add_production(this);
    }

    private String prodName = null;

    public String getProdName() {
        if (prodName == null) {
            var nt = (non_terminal) lhs().the_symbol();
            int count = 0;
            Collection<String> labels = getLabel2SymbolPartMap().keySet();
            o:
            while (true) {
                var newName = 'S' + signature(labels, ++count);
                for (Production prod : nt.productions()) {
                    if (newName.equals(prod.prodName) && !equalsLabels(prod)) {
                        continue o;
                    }
                }
                prodName = newName;
                break;
            }
        }
        return prodName;
    }

    void setProdName(String name) {
        prodName = name;
    }

    private static String signature(Collection<String> labels, int append) {
        try {
            StringBuilder sb = new StringBuilder(256);
            sb.append(append).append(':');
            for (String label : labels) {
                sb.append(label).append(';');
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(sb.toString().getBytes());
            return convertToBase62(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final char[] CHAR_SET =
        "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static String convertToBase62(byte[] hash) {
        long value = 0;
        for (int i = 0; i < 6; i++) {
            value = (value << 8) | (hash[i] & 0xFF);
        }

        char[] result = new char[8];
        for (int i = 8 - 1; i >= 0; i--) {
            result[i] = CHAR_SET[(int) (value % 62)];
            value /= 62;
        }
        return new String(result);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Constructor with no action string. */
    public Production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l) throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, null);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /*
     * Constructor with precedence and associativity of production contextually
     * define
     */
    public Production(
        non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, String action_str, int prec_num,
        int prec_side
    ) throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, action_str);
        /* set the precedence */
        set_precedence_num(prec_num);
        set_precedence_side(prec_side);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /*
     * Constructor w/ no action string and contextual precedence defined
     */
    public Production(non_terminal lhs_sym, production_part[] rhs_parts, int rhs_l, int prec_num, int prec_side)
        throws internal_error {
        this(lhs_sym, rhs_parts, rhs_l, null);
        /* set the precedence */
        set_precedence_num(prec_num);
        set_precedence_side(prec_side);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /*-----------------------------------------------------------*/
    /*--- (Access to) Static (Class) Variables ------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Table of all productions. Elements are stored using their index as the key.
     */
    protected static MonotonicIntObjectArrayMap<Production> _all = new MonotonicIntObjectArrayMap<>();

    /** Access to all productions. */
    public static Iterable<Production> all() {
        return _all.values();
    }

    /** Lookup a production by index. */
    public static Production find(int indx) {
        return _all.get(indx);
    }

    // Hm Added clear to clear all static fields
    public static void clear() {
        _all.clear();
        next_index = 0;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Total number of productions. */
    public static int number() {
        return _all.size();
    }

    /** Static counter for assigning unique index numbers. */
    protected static int next_index;

    /*-----------------------------------------------------------*/
    /*--- (Access to) Instance Variables ------------------------*/
    /*-----------------------------------------------------------*/

    /** The left hand side non-terminal. */
    protected symbol_part _lhs;

    /** The left hand side non-terminal. */
    public symbol_part lhs() {
        return _lhs;
    }

    /** The precedence of the rule */
    protected int _rhs_prec = -1;
    protected int _rhs_assoc = -1;

    /** Access to the precedence of the rule */
    public int precedence_num() {
        return _rhs_prec;
    }

    public int precedence_side() {
        return _rhs_assoc;
    }

    /** Setting the precedence of a rule */
    public void set_precedence_num(int prec_num) {
        _rhs_prec = prec_num;
    }

    public void set_precedence_side(int prec_side) {
        _rhs_assoc = prec_side;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** A collection of parts for the right hand side. */
    protected production_part[] _rhs;

    /** Access to the collection of parts for the right hand side. */
    public production_part rhs(int indx) throws internal_error {
        if (indx >= 0 && indx < _rhs_length)
            return _rhs[indx];
        else
            throw new internal_error("Index out of range for right hand side of production");
    }

    private Map<String, symbol_part> _labels;

    /**
     * Returns an unmodifiable map of labeled symbol parts in the production's right-hand side (RHS).
     * The map is keyed by label strings and contains only non-action symbol parts.
     * <p>
     * The result is cached after first computation (lazy initialization) and subsequent calls
     * return the cached unmodifiable map. This ensures labels are only processed once.
     *
     * @return An unmodifiable map where:
     * - Keys are String labels from production parts
     * - Values are the corresponding symbol_part objects
     * - Only includes parts with non-null labels that aren't actions
     * - Preserves insertion order
     */
    public Map<String, symbol_part> getLabel2SymbolPartMap() {
        if (_labels == null) {
            var labels = new LinkedHashMap<String, symbol_part>();
            for (int i = 0; i < _rhs_length; i++) {
                production_part part = _rhs[i];
                if (part.label() != null && !part.is_action()) {
                    labels.put(part.label(), (symbol_part) part);
                }
            }
            _labels = Collections.unmodifiableMap(labels);
        }
        return _labels;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** How much of the right hand side array we are presently using. */
    protected int _rhs_length;

    /** How much of the right hand side array we are presently using. */
    public int rhs_length() {
        return _rhs_length;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * An action_part containing code for the action to be performed when we reduce
     * with this production.
     */
    protected LazyContainer<action_part> _action;
    private final boolean hasTailAction;

    /**
     * An action_part containing code for the action to be performed when we reduce
     * with this production.
     */
    public action_part action() throws internal_error {
        return _action.get();
    }

    /**
     * Whether this production has a tail action
     * @return The return value of {@link #action} may have a value when false is returned
     */
    public boolean hasTailAction() {
        return hasTailAction;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Index number of the production. */
    protected int _index;

    /** Index number of the production. */
    public int index() {
        return _index;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Count of number of reductions using this production. */
    protected int _num_reductions = 0;

    /** Count of number of reductions using this production. */
    public int num_reductions() {
        return _num_reductions;
    }

    /** Increment the count of reductions with this non-terminal */
    public void note_reduction_use() {
        _num_reductions++;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Is the nullability of the production known or unknown? */
    protected boolean _nullable_known = false;

    /** Is the nullability of the production known or unknown? */
    public boolean nullable_known() {
        return _nullable_known;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Nullability of the production (can it derive the empty string). */
    protected boolean _nullable = false;

    /** Nullability of the production (can it derive the empty string). */
    public boolean nullable() {
        return _nullable;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * First set of the production. This is the set of terminals that could appear
     * at the front of some string derived from this production.
     */
    protected terminal_set _first_set = new terminal_set();

    /**
     * First set of the production. This is the set of terminals that could appear
     * at the front of some string derived from this production.
     */
    public terminal_set first_set() {
        return _first_set;
    }

    /*-----------------------------------------------------------*/
    /*--- Static Methods ----------------------------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Determine if a given character can be a label id starter.
     *
     * @param c the character in question.
     */
    protected static boolean is_id_start(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';

        // later need to handle non-8-bit chars here
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Determine if a character can be in a label id.
     *
     * @param c the character in question.
     */
    protected static boolean is_id_char(char c) {
        return is_id_start(c) || c >= '0' && c <= '9';
    }

    /*-----------------------------------------------------------*/
    /*--- General Methods ---------------------------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Return label declaration code
     *
     * @param labelName  the label name
     * @param stack_type the stack type of label?
     * @param offset symbol's stack offset (from the end).
     * @author frankf, kmar
     */
    protected static String make_declaration(String labelName, String stack_type, int offset) {
        StringBuilder ret = new StringBuilder(256);
        String indent = "              ";
        var symbolName = Main.customSymbolClass;
        ret.append(indent)
            .append("var ")
            .append(labelName)
            .append("Sym = (")
            .append(symbolName)
            .append(") ")
            .append(emit.buildStackSymReader(offset))
            .append(";\n");

        /* Put in the left/right value labels */
        if (emit.lr_values() && Main.ast_format == null) {
            // var xxxLeft = xxxSym.getLeft();
            ret.append(indent)
                .append("var ")
                .append(labelName)
                .append("Left = ")
                .append(labelName)
                .append("Sym.getLeft();\n")
                // var xxxRight = xxxSym.getRight();
                .append(indent)
                .append("var ")
                .append(labelName)
                .append("Right = ")
                .append(labelName)
                .append("Sym.getRight();\n");
        }

        if (!labelName.isEmpty() && (Main.ast_format == null || "start_val".equals(labelName))) {
            // xxx = xxxSym.value();
            ret.append(indent)
                .append(stack_type)
                .append(' ')
                .append(labelName)
                .append(" = ")
                .append(labelName)
                .append("Sym.")
                .append(emit.buildSymGetter(stack_type))
                .append(";\n");
        }

        return ret.toString();
    }
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Declare label names as valid variables within the action string
     *
     * @param rhs          array of RHS parts.
     * @param rhs_len      how much of rhs to consider valid.
     * @param final_action the final action string of the production.
     */
    protected static String declare_labels(production_part[] rhs, int rhs_len, String final_action) {
        StringBuilder declaration = new StringBuilder();

        /* walk down the parts and extract the labels */
        for (int pos = 0; pos < rhs_len; pos++) {
            if (!rhs[pos].is_action()) {
                symbol_part part = (symbol_part) rhs[pos];
                String label;
                /* if it has a label, make declaration! */
                if ((label = part.label()) != null || emit._xmlactions) {
                    if (part.isExistCheck() && !part.the_symbol().isOptBox()) continue;
                    if (label == null)
                        label = part.the_symbol().name() + pos;
                    var type = part.getType();
                    if (type == null) type = part.the_symbol().stack_type();
                    declaration.append(make_declaration(label, type, rhs_len - pos - 1));
                }
            }
        }
        return declaration.toString();
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Helper routine to merge adjacent actions in a set of RHS parts
     *
     * @param rhs_parts array of RHS parts.
     * @param len       amount of that array that is valid.
     * @return remaining valid length.
     */
    protected int merge_adjacent_actions(production_part[] rhs_parts, int len) {
        /* bail out early if we have no work to do */
        if (rhs_parts == null || len == 0)
            return 0;

        int merge_cnt = 0;
        int to_loc = -1;
        for (int from_loc = 0; from_loc < len; from_loc++) {
            /* do we go in the current position or one further */
            if (to_loc < 0 || !rhs_parts[to_loc].is_action() || !rhs_parts[from_loc].is_action()) {
                /* next one */
                to_loc++;

                /* clear the way for it */
                if (to_loc != from_loc)
                    rhs_parts[to_loc] = null;
            }

            /* if this is not trivial? */
            if (to_loc != from_loc) {
                /* do we merge or copy? */
                if (rhs_parts[to_loc] != null && rhs_parts[to_loc].is_action() && rhs_parts[from_loc].is_action()) {
                    /* merge */
                    rhs_parts[to_loc] = new action_part(
                        ((action_part) rhs_parts[to_loc]).code_string() + ((action_part) rhs_parts[from_loc]).code_string());
                    merge_cnt++;
                } else {
                    /* copy */
                    rhs_parts[to_loc] = rhs_parts[from_loc];
                }
            }
        }

        /* return the used length */
        return len - merge_cnt;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Helper routine to strip a trailing action off rhs and return it
     *
     * @param rhs_parts array of RHS parts.
     * @param len       how many of those are valid.
     * @return the removed action part.
     */
    protected action_part strip_trailing_action(production_part[] rhs_parts, int len) {
        /* bail out early if we have nothing to do */
        if (rhs_parts == null || len == 0)
            return null;

        /* see if we have a trailing action */
        if (rhs_parts[len - 1].is_action()) {
            /* snip it out and return it */
            action_part result = (action_part) rhs_parts[len - 1];
            rhs_parts[len - 1] = null;
            return result;
        } else
            return null;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Remove all embedded actions from a production by factoring them out into
     * individual action production using new non terminals. if the original
     * production was:
     *
     * <pre>
     *    A ::= B {action1} C {action2} D
     * </pre>
     *
     * then it will be factored into:
     *
     * <pre>
     *    A ::= B NT$1 C NT$2 D
     *    NT$1 ::= {action1}
     *    NT$2 ::= {action2}
     * </pre>
     *
     * where NT$1 and NT$2 are new system created non terminals.
     */

    /*
     * the declarations added to the parent production are also passed along, as
     * they should be perfectly valid in this code string, since it was originally a
     * code string in the parent, not on its own. frank 6/20/96
     */
    protected void remove_embedded_actions() throws internal_error {
        non_terminal new_nt;
        String declare_str;
        int lastLocation = -1;
        /* walk over the production and process each action */
        for (int act_loc = 0; act_loc < rhs_length(); act_loc++) {
            var part = rhs(act_loc);
            if (part.is_action()) {
                var actionPart = (action_part) part;
                declare_str = declare_labels(_rhs, act_loc, "");
                /* create a new non terminal for the action production */
                new_nt = non_terminal.create_new(null, lhs().the_symbol().stack_type()); // TUM 20060608 embedded actions patch
                new_nt.is_embedded_action = !actionPart.isVirtual(); /* 24-Mar-1998, CSA */

                /* create a new production with just the action */
                new action_production(this, new_nt, null, 0,
                    declare_str + actionPart.code_string(),
                    lastLocation == -1 ? -1 : act_loc - lastLocation);

                /* replace the action with the generated non terminal */
                _rhs[act_loc] = new symbol_part(new_nt);
                lastLocation = act_loc;
            }
        }
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Check if the current expression is an empty expression
     */
    public boolean isNull() throws internal_error {
        for (int pos = 0; pos < rhs_length(); pos++) {
            var part = rhs(pos);
            if (part.is_action()) continue;
            var sym = ((symbol_part) part).the_symbol();
            if (sym.is_non_term()) {
                var nt = (non_terminal) sym;
                for (Production prod : nt.productions()) {
                    if (!prod.isNull()) return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Check to see if the production (now) appears to be nullable. A production is
     * nullable if its RHS could derive the empty string. This results when the RHS
     * is empty or contains only non terminals which themselves are nullable.
     */
    public boolean check_nullable() throws internal_error {
        /* if we already know bail out early */
        if (nullable_known()) return nullable();
        /* if we have a zero size RHS we are directly nullable */
        if (rhs_length() == 0) {
            /* stash and return the result */
            return set_nullable(true);
        }

        /* otherwise we need to test all of our parts */
        for (int pos = 0; pos < rhs_length(); pos++) {
            production_part part = rhs(pos);
            /* only look at non-actions */
            if (!part.is_action()) {
                symbol sym = ((symbol_part) part).the_symbol();
                /* if its a terminal we are definitely not nullable */
                if (!sym.is_non_term()) {
                    return set_nullable(false);
                } else if (!((non_terminal) sym).nullable()) { // its a non-term, is it marked nullable
                    /* this one not (yet) nullable, so we aren't */
                    return false;
                }
            }
        }

        /* if we make it here all parts are nullable */
        return set_nullable(true);
    }

    /** set (and return) nullability */
    boolean set_nullable(boolean v) {
        _nullable_known = true;
        _nullable = v;
        return v;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Update (and return) the first set based on current NT firsts. This assumes
     * that nullability has already been computed for all non terminals and
     * productions.
     */
    public terminal_set check_first_set() throws internal_error {
        /* walk down the right hand side till we get past all nullables */
        for (int part = 0; part < rhs_length(); part++) {
            /* only look at non-actions */
            if (!rhs(part).is_action()) {
                symbol sym = ((symbol_part) rhs(part)).the_symbol();
                /* is it a non-terminal? */
                if (sym.is_non_term()) {
                    /* add in current firsts from that NT */
                    _first_set.add(((non_terminal) sym).first_set());
                    /* if its not nullable, we are done */
                    if (!((non_terminal) sym).nullable())
                        break;
                } else {
                    /* its a terminal -- add that to the set */
                    _first_set.add((terminal) sym);
                    /* we are done */
                    break;
                }
            }
        }

        /* return our updated first set */
        return first_set();
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Equality comparison. */
    public boolean equals(Production other) {
        return other != null && other._index == _index;
    }

    /**
     * Compare whether the label is exactly the same between two productions
     */
    public boolean equalsLabels(Production other) {
        if (other == null) return false;
        if (other._index == _index) return true;
        int thisIndex = 0, thatIndex = 0;
        int thisLen = rhs_length(), thatLen = other.rhs_length();
        while (true) {
            String thisLabel = null, thatLabel = null;
            symbol thisSym = null, thatSym = null;
            while (thisIndex != thisLen) {
                var part = _rhs[thisIndex++];
                var label = part.label();
                if (label == null || part.is_action()) continue;
                thisLabel = label;
                thisSym = ((symbol_part) part).the_symbol();
            }
            while (thatIndex != thatLen) {
                var part = other._rhs[thatIndex++];
                var label = part.label();
                if (label == null || part.is_action()) continue;
                thatLabel = label;
                thatSym = ((symbol_part) part).the_symbol();
            }
            if (thisLabel == null && thatLabel == null) {
                break;
            }
            if (thisLabel == null || thatLabel == null) {
                return false;
            }
            if (!thisSym.equals(thatSym) && !thisLabel.equals(thatLabel)) {
                return false;
            }
        }
        return true;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Generic equality comparison. */
    @Override
    public boolean equals(Object other) {
        return other instanceof Production && equals((Production) other);
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Produce a hash code. */
    @Override
    public int hashCode() {
        /* just use a simple function of the index */
        return _index * 13;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Convert to a string. */
    @Override
    public String toString() {
        StringBuilder result;

        /* catch any internal errors */
        try {
            result = new StringBuilder("production [" + index() + "]: ");
            result.append(lhs() != null ? lhs().toString() : "$$NULL-LHS$$");
            result.append(" :: = ");
            for (int i = 0; i < rhs_length(); i++) {
                result.append(rhs(i)).append(' ');
            }
            result.append(';');
            if (action() != null && action().code_string() != null)
                result.append(" {").append(action().code_string()).append('}');

            if (nullable_known())
                result.append(nullable() ? "[NULLABLE]" : "[NOT NULLABLE]");
        } catch (internal_error e) {
            /*
             * crash on internal error since we can't throw it from here (because superclass
             * does not throw anything.
             */
            e.crash();
            throw new AssertionError();
        }

        return result.toString();
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Convert to a simpler string. */
    public String to_simple_string() throws internal_error {
        StringBuilder result;

        result = new StringBuilder(lhs() != null ? lhs().the_symbol().name() : "NULL_LHS");
        result.append(" ::= ");
        for (int i = 0; i < rhs_length(); i++) {
            if (!rhs(i).is_action()) {
                result.append(((symbol_part) rhs(i)).the_symbol().name()).append(' ');
            }
        }

        return result.toString();
    }

    /*-----------------------------------------------------------*/

    /**
     * A production position finder, allows querying the index of a production by non-terminal and its order within it.
     *
     * @author kmar
     */
    public static final class PositionFinder {

        private final non_terminal nt;
        private final int prodIndexInNt;

        public PositionFinder(non_terminal nt, int prodIndexInNt) {
            this.nt = nt;
            this.prodIndexInNt = prodIndexInNt;
        }

        public int getProdIndex() {
            return getProd().index();
        }

        public Production getProd() {
            var itor = nt.productions().iterator();
            for (int i = 0; i < prodIndexInNt; i++) {
                itor.next();
            }
            return itor.next();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            PositionFinder that = (PositionFinder) o;
            return prodIndexInNt == that.prodIndexInNt && nt.equals(that.nt);
        }

        @Override
        public int hashCode() {
            int result = nt.hashCode();
            result = 31 * result + prodIndexInNt;
            return result;
        }

        public static PositionFinder newInstance(non_terminal nt) {
            return new PositionFinder(nt, nt.num_productions());
        }

    }

}