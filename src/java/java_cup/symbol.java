package java_cup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class serves as the base class for grammar symbols (i.e., both
 * terminals and non-terminals). Each symbol has a name string, and a string
 * giving the type of object that the symbol will be represented by on the
 * runtime parse stack. In addition, each symbol maintains a use count in order
 * to detect symbols that are declared but never used, and an index number that
 * indicates where it appears in parse tables (index numbers are unique within
 * terminals or non terminals, but not across both).
 *
 * @author Frank Flannery
 * @version last updated: 7/3/96
 * @see java_cup.terminal
 * @see java_cup.non_terminal
 */
public abstract class symbol {

    /*-----------------------------------------------------------*/
    /*--- Constructor(s) ----------------------------------------*/
    /*-----------------------------------------------------------*/

    /**
     * Full constructor.
     *
     * @param nm the name of the symbol.
     * @param tp a string with the type name.
     */
    public symbol(String nm, String tp) {
        /* sanity check */
        if (nm == null)
            nm = "";
        /* apply default if no type given */
        if (tp == null)
            tp = "Object";

        _name = nm;
        _stack_type = tp;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Constructor with default type.
     *
     * @param nm the name of the symbol.
     */
    public symbol(String nm) {
        this(nm, null);
    }

    /*-----------------------------------------------------------*/
    /*--- (Access to) Instance Variables ------------------------*/
    /*-----------------------------------------------------------*/

    /** String for the human readable name of the symbol. */
    protected String _name;

    /** String for the human readable name of the symbol. */
    public String name() {
        return _name;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** String for the type of object used for the symbol on the parse stack. */
    protected String _stack_type;

    /** String for the type of object used for the symbol on the parse stack. */
    public String stack_type() {
        return _stack_type;
    }

    public String astClassName() {
        String type = stack_type();
        if (type.equals("IAstNode")) {
            return symbol.getNtNodeClassName(name());
        }
        return type;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Count of how many times the symbol appears in productions. */
    protected int _use_count = 0;

    /** Count of how many times the symbol appears in productions. */
    public int use_count() {
        return _use_count;
    }

    /** Increment the use count. */
    public void note_use() {
        _use_count++;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Index of this symbol (terminal or non terminal) in the parse tables. Note:
     * indexes are unique among terminals and unique among non terminals, however, a
     * terminal may have the same index as a non-terminal, etc.
     */
    protected int _index;

    /**
     * Index of this symbol (terminal or non terminal) in the parse tables. Note:
     * indexes are unique among terminals and unique among non terminals, however, a
     * terminal may have the same index as a non-terminal, etc.
     */
    public int index() {
        return _index;
    }

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /**
     * Indicate if this is a non-terminal. Here in the base class we don't know, so
     * this is abstract.
     */
    public abstract boolean is_non_term();

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    /** Convert to a string. */
    @Override
    public String toString() {
        return name();
    }

    /*-----------------------------------------------------------*/

    private non_terminal _optBox = null;

    /**
     * Check if the current symbol is {@code ?} The nullable symbol produced by the operator.
     */
    public boolean isOptBox() {
        return _optBox == this;
    }

    /**
     * Creates a new non_terminal for the optional box.
     * <p>
     * By default, the new nonterminal return null if the match is null
     *
     * @param symbols All symbols map
     * @param emptyAction The action to be executed if the match is null, allow null
     */
    public final non_terminal createOptBox(
        Map<String, production_part> symbols, String emptyAction
    ) throws internal_error {
        if (_optBox != null) return _optBox;
        var newNt = non_terminal.create_new("_EBNF_OPT_", stack_type());
        newNt._isAnno = true;
        new production(
            newNt,
            new production_part[]{new symbol_part(this)},
            1,
            "RESULT = " + emit.buildStackValueReader(stack_type(), 0) + ';'
        );
        if (Main.ast_format != null && (emptyAction == null || emptyAction.isEmpty())) {
            emptyAction = "";
        }
        new production(newNt, new production_part[0], 0, emptyAction);
        _optBox = newNt;
        ((symbol) newNt)._optBox = newNt;
        symbols.put(newNt.name(), new symbol_part(newNt));
        return newNt;
    }

    private final Map<List<production_part>, ObjectPair<non_terminal, non_terminal>> _listBoxCache = new HashMap<>();

    /**
     * Creates a new non_terminal for the list box.
     * <p>
     * The new nonterminal returns a {@code List<Type>}, where {@code Type} is the type of the nonterminal
     * (or, if the nonterminal type is a primitive type, a wrapper) and an empty list,
     * if allowed to be empty.
     * <p>
     * The new nonterminal that never returns null.
     *
     * @param symbols All symbols map
     * @param sepList The separator for a list
     * @param allowTail Whether to allow an extra separator at the end of a list
     * @param allowEmpty Whether a list is allowed to be empty
     */
    public final non_terminal createListBox(
        Map<String, production_part> symbols,
        List<production_part> sepList, boolean allowTail, boolean allowEmpty
    ) throws internal_error {
        boolean isAstNode = Main.ast_format != null;
        var type = isAstNode ? astClassName() : _stack_type;
        var listType = emit.buildListExpr(type);
        // convert primitive types to their wrapper (because the jvm does not support primitive generics)
        switch (type) {
            case "char":
                type = "Character";
                break;
            case "boolean":
            case "byte":
            case "short":
            case "int":
            case "long":
            case "float":
            case "double":
                type = type.substring(0, 1).toUpperCase() + type.substring(1);
                break;
        }
        String finalType = type;
        var cache = _listBoxCache.computeIfAbsent(sepList, k -> {
            try {
                var newNt = non_terminal.create_new("_EBNF_LIST_", listType);
                newNt._isAnno = true;
                new production(
                    newNt,
                    new production_part[]{new symbol_part(this)},
                    1,
                    isAstNode ? null : "var list = new ArrayList<" + finalType + ">();\n"
                        + "list.add(" + emit.buildStackValueReader(finalType, 0) + ");\n"
                        + "RESULT = list;"
                );
                var listProdPart = new production_part[2 + sepList.size()];
                listProdPart[0] = new symbol_part(newNt);
                for (int i = 0; i < sepList.size(); i++) {
                    listProdPart[i + 1] = sepList.get(i);
                }
                listProdPart[listProdPart.length - 1] = new symbol_part(this);
                new production(
                    newNt,
                    listProdPart,
                    listProdPart.length,
                    listType + " list = "
                        + emit.buildStackValueReader(listType, listProdPart.length - 1) + ";\n"
                        + "list.add(" + emit.buildStackValueReader(finalType, 0) + ");\n"
                        + "RESULT = list;"
                );
                symbols.put(newNt.name(), new symbol_part(newNt));
                return new ObjectPair<>(newNt, null);
            } catch (internal_error e) {
                e.crash();
                throw new AssertionError();
            }
        });
        if (!allowTail) {
            if (!allowEmpty) return cache.getFirst();
            return cache.getFirst().createOptBox(
                symbols,
                "RESULT = java.util.Collections.<" + type + ">emptyList();"
            );
        }
        if (cache.getSecond() == null) {
            var tailNt = createListTailBox(cache.getFirst(), sepList, type);
            symbols.put(tailNt.name(), new symbol_part(tailNt));
            cache = cache.modifySecond(tailNt);
        }
        if (!allowEmpty) return cache.getSecond();
        return cache.getSecond().createOptBox(
            symbols,
            "RESULT = java.util.Collections.<" + type + ">emptyList();"
        );
    }

    private static non_terminal createListTailBox(
        non_terminal nt, List<production_part> split, String type
    ) throws internal_error {
        var listType = emit.buildListExpr(type);
        var newNt = non_terminal.create_new("_EBNF_LIST_TAIL_", listType);
        newNt._isAnno = true;
        new production(
            newNt,
            new production_part[]{new symbol_part(nt)},
            1,
            "RESULT = " + emit.buildStackValueReader(listType, 0) + ";"
        );
        var splitPart = new production_part[1 + split.size()];
        splitPart[0] = new symbol_part(nt);
        for (int i = 0; i < split.size(); i++) {
            splitPart[i + 1] = split.get(i);
        }
        new production(
            newNt,
            splitPart,
            splitPart.length,
            "RESULT = " + emit.buildStackValueReader(listType, splitPart.length - 1) + ";"
        );
        return newNt;
    }

    /*-----------------------------------------------------------*/

    public static String getNtNodeClassName(String name) {
        String format = Main.ast_format;
        StringBuilder sb = new StringBuilder(name.length() + format.length());
        for (int i = 0; i < format.length(); i++) {
            char fc = format.charAt(i);
            if (fc == '%') {
                char nextChar = format.charAt(++i);
                if (nextChar == 's') {
                    writeNameS(sb, name);
                } else if (nextChar == 'p') {
                    writeNameP(sb, name);
                } else {
                    throw new AssertionError();
                }
            } else {
                sb.append(fc);
            }
        }
        return sb.toString();
    }

    private static void writeNameS(StringBuilder sb, String name) {
        boolean toUpper = true;
        for (int i = name.indexOf('_') + 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                toUpper = true;
            } else if (toUpper) {
                sb.append(Character.toUpperCase(c));
                toUpper = false;
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
    }

    private static void writeNameP(StringBuilder sb, String name) {
        int endIndex = name.indexOf('_');
        for (int i = 0; i < endIndex; i++) {
            char c = name.charAt(i);
            sb.append(i == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
        }
    }

}