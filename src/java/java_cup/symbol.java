package java_cup;

/**
 * This abstract class serves as the base class for grammar symbols (i.e., both
 * terminals and non-terminals). Each symbol has a name string, and a string
 * giving the type of object that the symbol will be represented by on the
 * runtime parse stack. In addition, each symbol maintains a use count in order
 * to detect symbols that are declared but never used, and an index number that
 * indicates where it appears in parse tables (index numbers are unique within
 * terminals or non terminals, but not across both).
 *
 * @see java_cup.terminal
 * @see java_cup.non_terminal
 * @version last updated: 7/3/96
 * @author Frank Flannery
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

  public String astClassName() throws internal_error {
    String type = stack_type();
    switch (type) {
      case "IAstNode":
        return symbol.getNtNodeClassName(name());
      case "List":
        if (is_non_term()) {
          var symbol = (non_terminal) this;
          String itemType = symbol.getListItemType();
          if (itemType != null) return "List<" + itemType + '>';
        }
        break;
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
   * Creates a new non_terminal for the optional box.
   *
   * @return non cache
   */
  public final non_terminal createOptBox() throws internal_error {
    if (_optBox != null) return _optBox;
    var newNt = non_terminal.create_new("_BENF_OPT_", _stack_type);
    newNt._isInline= true;
    boolean isAstNode = Main.ast_format != null;
    var itemProd = new production(
      newNt,
      new production_part[]{new symbol_part(this, isAstNode ? "item" : null)},
      1,
      isAstNode ? null : "RESULT = " + emit.pre("stack") + ".peek();"
    );
    var emptyProd = new production(newNt, new production_part[0], 0);
    newNt.add_production(itemProd);
    newNt.add_production(emptyProd);
    _optBox = newNt;
    return newNt;
  }

  private non_terminal _listBox = null;

  public final non_terminal createListBox() throws internal_error {
    if (_listBox != null) return _listBox;
    boolean isAstNode = Main.ast_format != null;
    var type = isAstNode ? astClassName() : _stack_type;
    var newNt = non_terminal.create_new("_BENF_LIST_", "List<" + type + '>');
    newNt._isInline = true;
    var getterName = getNtValueGetterName();
    var itemProd = new production(
      newNt,
      new production_part[]{new symbol_part(this, isAstNode ? "item" : null)},
      1,
      isAstNode ? null : "var list = new ArrayList<" + type + ">();\n"
                                + "list.add(" + emit.pre("stack") + ".peek()." + getterName +"());\n"
                                + "RESULT = list;"
    );
    var listProd = new production(
      newNt,
      new production_part[] {
        new symbol_part(newNt),
        new symbol_part(this, isAstNode ? "item" : null)
      },
      2,
      isAstNode ? null : "List<" + type + "> list = " + emit.pre("stack")
                                    + ".get(" + emit.pre("top") + " - 1).value();\n"
                                + "list.add(" + emit.pre("stack") + ".peek()." + getterName + "());\n"
                                + "RESULT = list;"
    );
    newNt.add_production(listProd);
    newNt.add_production(itemProd);
    _listBox = newNt;
    return newNt;
  }

  private String getNtValueGetterName() {
      switch (_stack_type) {
        case "byte": return "getAsByte";
        case "short": return "getAsShort";
        case "int": return "getAsInt";
        case "long": return "getAsLong";
        case "float": return "getAsFloat";
        case "double": return "getAsDouble";
        case "char": return "getAsChar";
        case "boolean": return "getAsBoolean";
        default: return "value";
      }
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