package java_cup;

import java_cup.runtime.ArrayStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class handles emitting generated code for the resulting parser. The
 * various parse tables must be constructed, etc. before calling any routines in
 * this class.
 * <p>
 *
 * Three classes are produced by this code:
 * <dl>
 * <dt>symbol constant class
 * <dd>this contains constant declarations for each terminal (and optionally
 * each non-terminal).
 * <dt>action class
 * <dd>this non-public class contains code to invoke all the user actions that
 * were embedded in the parser specification.
 * <dt>parser class
 * <dd>the specialized parser class consisting primarily of some user supplied
 * general and initialization code, and the parse tables.
 * </dl>
 * <p>
 *
 * Three parse tables are created as part of the parser class:
 * <dl>
 * <dt>production table
 * <dd>lists the LHS non terminal number, and the length of the RHS of each
 * production.
 * <dt>action table
 * <dd>for each state of the parse machine, gives the action to be taken (shift,
 * reduce, or error) under each lookahead symbol.<br>
 * <dt>reduce-goto table
 * <dd>when a reduce on a given production is taken, the parse stack is popped
 * back a number of elements corresponding to the RHS of the production. This
 * reveals a prior state, which we transition out of under the LHS non terminal
 * symbol for the production (as if we had seen the LHS symbol rather than all
 * the symbols matching the RHS). This table is indexed by non terminal numbers
 * and indicates how to make these transitions.
 * </dl>
 * <p>
 * 
 * In addition to the method interface, this class maintains a series of public
 * global variables and flags indicating how misc. parts of the code and other
 * output is to be produced, and counting things such as number of conflicts
 * detected (see the source code and public variables below for more details).
 * <p>
 *
 * This class is "static" (contains only static data and methods).
 * <p>
 *
 * @see java_cup.Main
 * @version last update: 11/25/95
 * @author Scott Hudson
 */

/*
 * Major externally callable routines here include: symbols - emit the symbol
 * constant class parser - emit the parser class
 * 
 * In addition the following major internal routines are provided: emit_package
 * - emit a package declaration emit_action_code - emit the class containing the
 * user's actions emit_production_table - emit declaration and init for the
 * production table do_action_table - emit declaration and init for the action
 * table do_reduce_table - emit declaration and init for the reduce-goto table
 * 
 * Finally, this class uses a number of public instance variables to communicate
 * optional parameters and flags used to control how code is generated, as well
 * as to report counts of various things (such as number of conflicts detected).
 * These include:
 * 
 * prefix - a prefix string used to prefix names that would otherwise "pollute"
 * someone else's name space. package_name - name of the package emitted code is
 * placed in (or null for an unnamed package. symbol_const_class_name - name of
 * the class containing symbol constants. parser_class_name - name of the class
 * for the resulting parser. action_code - user supplied declarations and other
 * code to be placed in action class. parser_code - user supplied declarations
 * and other code to be placed in parser class. init_code - user supplied code
 * to be executed as the parser is being initialized. scan_code - user supplied
 * code to get the next Symbol. start_production - the start production for the
 * grammar. import_list - list of imports for use with action class.
 * num_conflicts - number of conflicts detected. nowarn - true if we are not to
 * issue warning messages. not_reduced - count of number of productions that
 * never reduce. unused_term - count of unused terminal symbols. unused_non_term
 * - count of unused non terminal symbols. _time - a series of symbols
 * indicating how long various sub-parts of code generation took (used to
 * produce optional time reports in main).
 */

public class emit {

  /*-----------------------------------------------------------*/
  /*--- Constructor(s) ----------------------------------------*/
  /*-----------------------------------------------------------*/

  /** Only constructor is private so no instances can be created. */
  private emit() {
  }

  /*-----------------------------------------------------------*/
  /*--- Static (Class) Variables ------------------------------*/
  /*-----------------------------------------------------------*/

  /** The prefix placed on names that pollute someone else's name space. */
  public static String prefix = "CUP$";

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Package that the resulting code goes into (null is used for unnamed). */
  public static String package_name = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Name of the generated class for symbol constants. */
  public static String symbol_const_class_name = "sym";

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Name of the generated parser class. */
  public static String parser_class_name = "parser";

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * TUM changes; proposed by Henning Niss 20050628: Type arguments for class
   * declaration
   */
  public static String class_type_argument = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** User declarations for direct inclusion in user action class. */
  public static String action_code = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** User declarations for direct inclusion in parser class. */
  public static String parser_code = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** User code for user_init() which is called during parser initialization. */
  public static String init_code = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** User code for scan() which is called to get the next Symbol. */
  public static String scan_code = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** The start production of the grammar. */
  public static production start_production = null;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** List of imports (Strings containing class names) to go with actions. */
  public static ArrayStack<String> import_list = new ArrayStack<>();

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Number of conflict found while building tables. */
  public static int num_conflicts = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Do we skip warnings? */
  public static boolean nowarn = false;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Count of the number on non-reduced productions found. */
  public static int not_reduced = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Count of unused terminals. */
  public static int unused_term = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /** Count of unused non terminals. */
  public static int unused_non_term = 0;

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /* Timing values used to produce timing report in main. */

  /** Time to produce symbol constant class. */
  public static long symbols_time = 0;

  /** Time to produce parser class. */
  public static long parser_time = 0;

  /** Time to produce action code class. */
  public static long action_code_time = 0;

  /** Time to produce the production table. */
  public static long production_table_time = 0;

  /** Time to produce the action table. */
  public static long action_table_time = 0;

  /** Time to produce the reduce-goto table. */
  public static long goto_table_time = 0;

  /* frankf 6/18/96 */
  protected static boolean _lr_values;
  protected static boolean _locations;
  protected static boolean _xmlactions;
  protected static boolean _genericlabels;

  /** whether or not to emit code for left and right values */
  public static boolean lr_values() {
    return _lr_values;
  }

  public static boolean locations() {
    return _locations;
  }

  protected static void set_lr_values(boolean b) {
    _lr_values = b;
  }

  protected static void set_locations(boolean b) {
    _locations = b;
  }

  protected static void set_genericlabels(boolean b) {
    _genericlabels = b;
  }

  protected static void set_xmlactions(boolean b) {
    _xmlactions = b;
    if (!b)
      return;
    _locations = true;
    _lr_values = true;
  }

  // Hm Added clear to clear all static fields
  public static void clear() {
    _genericlabels = false;
    _xmlactions = false;
    _locations = false;
    _lr_values = true;
    action_code = null;
    import_list = new ArrayStack<>();
    init_code = null;
    not_reduced = 0;
    num_conflicts = 0;
    package_name = null;
    parser_class_name = "parser";
    parser_code = null;
    scan_code = null;
    start_production = null;
    symbol_const_class_name = "sym";
    unused_non_term = 0;
    unused_term = 0;
  }

  /*-----------------------------------------------------------*/
  /*--- General Methods ---------------------------------------*/
  /*-----------------------------------------------------------*/

  /**
   * Build a string with the standard prefix.
   * 
   * @param str string to prefix.
   */
  protected static String pre(String str) {
    return prefix + parser_class_name + "$" + str;
  }

  /**
   * This function analyzes the label name to determine if it follows common naming patterns
   * for boolean flags that represent whether a symbol exists (e.g., "isXxx", "is0", "is_xxx").
   *
   * @param label The name of the label to check.
   */
  protected static boolean isExistenceVar(String label) {
    if (label.length() < 3 || !label.startsWith("is")) return false;
    char third = label.charAt(2);
    if (third == '_') {
      return label.length() != 3;
    }
    return Character.isUpperCase(third) || Character.isDigit(third);
  }

  private static final Pattern _stPattern = Pattern.compile(
    "[A-Z]{2,}(?=[A-Z][a-z]|\\b)|" +
      "[A-Z]?[a-z]+|" +
      "[A-Z]+"
  );

  /**
   * Format variable names in uppercase camel case
   */
  protected static String castToStName(String name) {
    if (name == null || name.isEmpty()) return name;

    String[] parts = name.split("_");
    List<String> words = new ArrayList<>();
    for (String part : parts) {
      var matcher = _stPattern.matcher(part);
      while (matcher.find()) {
        words.add(matcher.group());
      }
    }

    StringBuilder result = new StringBuilder();
    for (String word : words) {
      if (word.isEmpty()) continue;
      result.append(Character.toUpperCase(word.charAt(0)));
      if (word.length() > 1) {
        result.append(word.substring(1).toLowerCase());
      }
    }
    return result.toString();
  }

  protected static String joinName(String prefix, String name) {
    if (prefix == null || prefix.isEmpty()) return name;
    StringBuilder sb = new StringBuilder(prefix.length() + name.length());
    sb.append(prefix);
    sb.append(Character.toUpperCase(name.charAt(0)));
    for (int i = 1; i < name.length(); i++) {
      sb.append(name.charAt(i));
    }
    return sb.toString();
  }

  /**
   * TUM changes; proposed by Henning Niss 20050628 Build a string with the
   * specified type arguments, if present, otherwise an empty string.
   */
  protected static String typeArgument() {
    return class_type_argument == null ? "" : "<" + class_type_argument + ">";
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit a package spec if the user wants one.
   * 
   * @param out stream to produce output on.
   */
  protected static void emit_package(PrintWriter out) {
    /* generate a package spec if we have a name for one */
    if (package_name != null) {
      out.println("package " + package_name + ";");
      out.println();
    }
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit code for the symbol constant class, optionally including non terms, if
   * they have been requested.
   * 
   * @param out            stream to produce output on.
   * @param emit_non_terms do we emit constants for non terminals?
   * @param sym_interface  should we emit an interface, rather than a class?
   */
  public static void symbols(PrintWriter out, boolean emit_non_terms, boolean sym_interface) {
    String class_or_interface = sym_interface ? "interface" : "class";

    long start_time = System.currentTimeMillis();

    /* top of file */
    out.println();
    out.println("//----------------------------------------------------");
    out.println("// The following code was generated by " + version.title_str);
    out.println("//----------------------------------------------------");
    out.println();
    emit_package(out);

    /* class header */
    out.println("/** CUP generated " + class_or_interface + " containing symbol constants. */");
    out.println("public " + class_or_interface + " " + symbol_const_class_name + " {");

    out.println("  /* terminals */");

    /* walk over the terminals */ /* later might sort these */
    for (terminal term : terminal.all()) {
      /* output a constant decl for the terminal */
      out.println("  public static final int " + term.name() + " = " + term.index() + ";");
    }

    /* Emit names of terminals */
    out.println("  public static final String[] terminalNames = new String[] {");
    for (int i = 0; i < terminal.number(); i++) {
      out.print("  \"");
      out.print(terminal.find(i).name());
      out.print("\"");
      if (i < terminal.number() - 1) {
        out.print(",");
      }
      out.println();
    }
    out.println("  };");

    /* do the non terminals if they want them (parser doesn't need them) */
    if (emit_non_terms) {
      out.println();
      out.println("  /* non terminals */");

      /* walk over the non terminals */ /* later might sort these */
      for (non_terminal nt : non_terminal.all()) {

        // ****
        // TUM Comment: here we could add a typesafe enumeration
        // ****

        /* output a constant decl for the terminal */
        out.println("  static final int " + nt.name() + " = " + nt.index() + ";");
      }
    }

    /* end of class */
    out.println("}");
    out.println();

    symbols_time = System.currentTimeMillis() - start_time;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  static final int UPPERLIMIT = 300;

  /**
   * Emit code for the non-public class holding the actual action code.
   * 
   * @param out        stream to produce output on.
   * @param start_prod the start production of the grammar.
   */
  protected static void emit_action_code(PrintWriter out, production start_prod) throws internal_error {
    production prod;

    long start_time = System.currentTimeMillis();

    /* class header */
    out.println();
    out.println("/** Cup generated class to encapsulate user supplied action code.*/");
    out.println(
      "@SuppressWarnings({\"UnnecessaryLocalVariable\", \"SpellCheckingInspection\", " +
      "\"UnusedAssignment\", \"RedundantTypeArguments\", " +
      "\"rawtypes\", \"RedundantCast\", \"unchecked\"})"
    );
    /* TUM changes; proposed by Henning Niss 20050628: added type arguement */
    out.println("protected class " + pre("actions") + typeArgument() + " {");
    /* user supplied code */
    if (action_code != null) {
      out.println();
      out.println(action_code);
    }

    /* field for parser object */
    /* TUM changes; proposed by Henning Niss 20050628: added typeArgument */
    out.println("  private final " + parser_class_name + typeArgument() + " parser;");

    /* constructor */
    out.println();
    out.println("  /** Constructor */");
    /* TUM changes; proposed by Henning Niss 20050628: added typeArgument */
    out.println("  " + pre("actions") + "(" + parser_class_name + typeArgument() + " parser) {");
    out.println("    this.parser = parser;");
    out.println("  }");

    out.println();
    for (int instancecounter = 0; instancecounter <= production.number() / UPPERLIMIT; instancecounter++) {
      out.println("  /** Method " + instancecounter + " with the actual generated action code for actions "
          + (instancecounter * UPPERLIMIT) + " to " + ((instancecounter + 1) * UPPERLIMIT) + ". */");
      out.println("  public final java_cup.runtime.Symbol " + pre("do_action_part")
          + String.format("%08d", instancecounter) + "(");
      out.println("    int                        " + pre("act_num,"));
      out.println("    java_cup.runtime.lr_parser " + pre("parser,"));
      out.println("    java_cup.runtime.ArrayStack<java_cup.runtime.Symbol>    " + pre("stack,"));
      out.println("    int                        " + pre("top)"));
      out.println("    throws java.lang.Exception");
      out.println("    {");
      out.println("      /* Symbol object for return from actions */");
      out.println("      java_cup.runtime.Symbol " + pre("result") + ";");
      out.println();
      out.println("      /* select the action based on the action number */");
      out.println("      switch (" + pre("act_num") + ")");
      out.println("        {");
      // START Switch
      /* emit action code for each production as a separate case */
      int proditeration = instancecounter * UPPERLIMIT;
      prod = production.find(proditeration);
      for (; proditeration < Math.min((instancecounter + 1) * UPPERLIMIT,
          production.number()); prod = production.find(++proditeration)) {
        /* case label */
        out.println("          /*. . . . . . . . . . . . . . . . . . . .*/");
        out.println("          case " + prod.index() + ": // " + prod.to_simple_string());

        /* give them their own block to work in */
        out.println("            {");
        out.println("              String " + pre("name") + " = \"" + prod.lhs().the_symbol().name() + "\";");

        /*
          TUM 20060608 intermediate result patch
         */
        String result = "null";
        if (prod instanceof action_production) {
          int lastResult = ((action_production) prod).getIndexOfIntermediateResult();
          if (lastResult != -1) {
            result = emit.pre("stack") +((lastResult == 1) ? ".peek()" : (".elementAt(" + emit.pre("top") + "-" + (lastResult - 1) + ")"))
                + ".<"+prod.lhs().the_symbol().stack_type()+">value()";
          }
        }

        /* create the result symbol */
        /*
         * make the variable RESULT which will point to the new Symbol (see below) and
         * be changed by action code 6/13/96 frankf
         */
        out.println("              " + prod.lhs().the_symbol().stack_type() + " RESULT =" + result + ";");

        /*
         * Add code to propagate RESULT assignments that occur in action code embedded
         * in a production (ie, non-rightmost action code). 24-Mar-1998 CSA
         */
        for (int i = prod.rhs_length() - 1; i >= 0; i--) {
          // only interested in non-terminal symbols.
          if (!(prod.rhs(i) instanceof symbol_part))
            continue;
          symbol s = ((symbol_part) prod.rhs(i)).the_symbol();
          if (!(s instanceof non_terminal))
            continue;
          // skip this non-terminal unless it corresponds to
          // an embedded action production.
          if (!((non_terminal) s).is_embedded_action)
            continue;
          // OK, it fits. Make a conditional assignment to RESULT.
          int index = prod.rhs_length() - i - 1; // last rhs is on top.
          // set comment to inform about where the intermediate result came from
          out.println("              " + "// propagate RESULT from " + s.name());
          // // look out, whether the intermediate result is null or not
          // out.println(" " + "if ( " +
          // "((java_cup.runtime.Symbol) " + emit.pre("stack") +
          // // TUM 20050917
          // ((index==0)?".peek()":(".elementAt(" + emit.pre("top") + "-" + index + ")"))+
          // ").value != null )");

          // TUM 20060608: even when its null: who cares?

          // store the intermediate result into RESULT
          out.println("                " + "RESULT = " + emit.pre("stack") +
              ((index == 0) ? ".peek()" : (".elementAt(" + emit.pre("top") + "-" + index + ")")) + 
              ".<"+prod.lhs().the_symbol().stack_type()+">value();");
          break;
        }

        /* if there is an action string, emit it */
        if (prod.action() != null && prod.action().code_string() != null)
          out.println(prod.action().code_string());

        /*
         * here we have the left and right values being propagated. must make this a
         * command line option. frankf 6/18/96
         */

        /*
         * Create the code that assigns the left and right values of the new Symbol that
         * the production is reducing to
         */
        if (emit.lr_values()) {
          int loffset;
          String leftstring, rightstring;
          rightstring = emit.pre("stack") + ".peek()" ;
          if (prod.rhs_length() == 0)
            leftstring = rightstring;
          else {
            loffset = prod.rhs_length() - 1;
            leftstring = emit.pre("stack") + ((loffset == 0) ? (".peek()") : (".elementAt(" + emit.pre("top") + "-" + loffset + ")"));
          }
          out.println("              " + pre("result") + " = parser.getSymbolFactory().newSymbol("
            + pre("name") + ", " + prod.lhs().the_symbol().index() + ", " + leftstring
            + ((prod.rhs_length() == 0) ? ("") : (", " + rightstring)) + ", RESULT);");
        } else {
          out.println("              " + pre("result") + " = parser.getSymbolFactory().newSymbol("
            + pre("name") + ", " + prod.lhs().the_symbol().index() + ", RESULT);");
        }

        /* end of their block */
        out.println("            }");

        /* if this was the start production, do action for accept */
        if (prod == start_prod) {
          out.println("          /* ACCEPT */");
          out.println("          " + pre("parser") + ".done_parsing();");
        }

        /* code to return lhs symbol */
        out.println("          return " + pre("result") + ";");
        out.println();
      }

      // END Switch
      out.println("          /* . . . . . .*/");
      out.println("          default:");
      out.println("            throw new Exception(");
      out.println(
          "               \"Invalid action number \"+" + pre("act_num") + "+\"found in " + "internal parse table\");");
      out.println();
      out.println("        }");
      out.println("    } /* end of method */");
    }

    /* action method head */
    out.println();
    out.println("  /** Method splitting the generated action code into several parts. */");
    out.println("  public final java_cup.runtime.Symbol " + pre("do_action") + "(");
    out.println("    int                        " + pre("act_num,"));
    out.println("    java_cup.runtime.lr_parser " + pre("parser,"));
    out.println("    java_cup.runtime.ArrayStack<java_cup.runtime.Symbol>     " + pre("stack,"));
    out.println("    int                        " + pre("top)"));
    out.println("    throws java.lang.Exception");
    out.println("    {");

    if (production.number() < UPPERLIMIT) { // Make it simple for the optimizer to inline!
      out.println("              return " + pre("do_action_part") + String.format("%08d", 0) + "(");
      out.println("                               " + pre("act_num,"));
      out.println("                               " + pre("parser,"));
      out.println("                               " + pre("stack,"));
      out.println("                               " + pre("top);"));
      out.println("    }");

      /* end of class */
      out.println("}");
      out.println();

      action_code_time = System.currentTimeMillis() - start_time;
      return;
    }

    /* switch top */
    out.println("      /* select the action handler based on the action number */");
    out.println("      switch (" + pre("act_num") + "/" + UPPERLIMIT + ")");
    out.println("        {");

    /* emit action code for each production as a separate case */
    for (int instancecounter = 0; instancecounter <= production.number() / UPPERLIMIT; instancecounter++) {
      /* case label */
      out.println("          /*. . . . . . . . " + (instancecounter * UPPERLIMIT) + " < #action < "
          + ((instancecounter + 1) * UPPERLIMIT) + ". . . . . . . . . . . .*/");
      out.println("          case " + instancecounter + ": ");
      out.println("              return " + pre("do_action_part")
          + String.format("%08d", instancecounter) + "(");
      out.println("                               " + pre("act_num,"));
      out.println("                               " + pre("parser,"));
      out.println("                               " + pre("stack,"));
      out.println("                               " + pre("top);"));
    }

    out.println("          /* . . . no valid action number: . . .*/");
    out.println("          default:");
    out.println("            throw new Exception(\"Invalid action number found in internal parse table\");");
    out.println();
    out.println("        }      /* end of switch */");

    /* end of method */
    out.println("    }");

    /* end of class */
    out.println("}");
    out.println();

    action_code_time = System.currentTimeMillis() - start_time;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit the production table.
   * 
   * @param out stream to produce output on.
   */
  protected static void emit_production_table(PrintWriter out) {
    production[] all_prods;

    long start_time = System.currentTimeMillis();

    /* collect up the productions in order */
    all_prods = new production[production.number()];
    for (var prod:production.all()){
      all_prods[prod.index()] = prod;
    }

    // make short[][]
    short[][] prod_table = new short[production.number()][2];
    for (int i = 0; i < production.number(); i++) {
      var prod = all_prods[i];
      // { lhs symbol , rhs size }
      prod_table[i][0] = (short) prod.lhs().the_symbol().index();
      prod_table[i][1] = (short) prod.rhs_length();
    }
    /* do the top of the table */
    out.println();
    out.println("  /** Production table. */");
    out.println("  protected static final short[][] _production_table = ");
    out.print("    unpackFromStrings(");
    do_table_as_string(out, prod_table);
    out.println(");");

    /* do the public accessor method */
    out.println();
    out.println("  /** Access to production table. */");
    out.println("  @Override");
    out.println("  public short[][] production_table() " + "{return _production_table;}");

    production_table_time = System.currentTimeMillis() - start_time;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit the action table.
   * 
   * @param out             stream to produce output on.
   * @param act_tab         the internal representation of the action table.
   * @param compact_reduces do we use the most frequent reduce as default?
   */
  protected static void do_action_table(PrintWriter out, parse_action_table act_tab, boolean compact_reduces)
      throws internal_error {
    parse_action_row row;
    parse_action act;
    int red;

    long start_time = System.currentTimeMillis();

    /* collect values for the action table */
    short[][] action_table = new short[act_tab.num_states()][];
    /* do each state (row) of the action table */
    for (int i = 0; i < act_tab.num_states(); i++) {
      /* get the row */
      row = act_tab.under_state[i];

      /* determine the default for the row */
      if (compact_reduces)
        row.compute_default();
      else
        row.default_reduce = -1;

      /* make temporary table for the row. */
      short[] temp_table = new short[2 * parse_action_row.size()];
      int nentries = 0;

      /* do each column */
      for (int j = 0; j < parse_action_row.size(); j++) {
        /* extract the action from the table */
        act = row.under_term[j];

        /* skip error entries these are all defaulted out */
        if (act.kind() != parse_action.ERROR) {
          /* first put in the symbol index, then the actual entry */

          /* shifts get positive entries of state number + 1 */
          if (act.kind() == parse_action.SHIFT) {
            /* make entry */
            temp_table[nentries++] = (short) j;
            temp_table[nentries++] = (short) (((shift_action) act).shift_to().index() + 1);
          }

          /* reduce actions get negated entries of production# + 1 */
          else if (act.kind() == parse_action.REDUCE) {
            /* if its the default entry let it get defaulted out */
            red = ((reduce_action) act).reduce_with().index();
            if (red != row.default_reduce) {
              /* make entry */
              temp_table[nentries++] = (short) j;
              temp_table[nentries++] = (short) -(red + 1);
            }
          } else if (act.kind() == parse_action.NONASSOC) {
            /* do nothing, since we just want a syntax error */
          }
          /* shouldn't be anything else */
          else
            throw new internal_error("Unrecognized action code " + act.kind() + " found in parse table");
        }
      }

      /* now we know how big to make the row */
      action_table[i] = new short[nentries + 2];
      System.arraycopy(temp_table, 0, action_table[i], 0, nentries);

      /* finish off the row with a default entry */
      action_table[i][nentries++] = -1;
      if (row.default_reduce != -1)
        action_table[i][nentries] = (short) -(row.default_reduce + 1);
      else
        action_table[i][nentries] = 0;
    }

    /* finish off the init of the table */
    out.println();
    out.println("  /** Parse-action table. */");
    out.println("  protected static final short[][] _action_table = ");
    out.print("    unpackFromStrings(");
    do_table_as_string(out, action_table);
    out.println(");");

    /* do the public accessor method */
    out.println();
    out.println("  /** Access to parse-action table. */");
    out.println("  @Override");
    out.println("  public short[][] action_table() {return _action_table;}");

    action_table_time = System.currentTimeMillis() - start_time;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit the reduce-goto table.
   * 
   * @param out     stream to produce output on.
   * @param red_tab the internal representation of the reduce-goto table.
   */
  protected static void do_reduce_table(PrintWriter out, parse_reduce_table red_tab) {
    lalr_state goto_st;

    long start_time = System.currentTimeMillis();

    /* collect values for reduce-goto table */
    short[][] reduce_goto_table = new short[red_tab.num_states()][];
    /* do each row of the reduce-goto table */
    for (int i = 0; i < red_tab.num_states(); i++) {
      /* make temporary table for the row. */
      short[] temp_table = new short[2 * parse_reduce_row.size()];
      int nentries = 0;
      /* do each entry in the row */
      for (int j = 0; j < parse_reduce_row.size(); j++) {
        /* get the entry */
        goto_st = red_tab.under_state[i].under_non_term[j];

        /* if we have none, skip it */
        if (goto_st != null) {
          /* make entries for the index and the value */
          temp_table[nentries++] = (short) j;
          temp_table[nentries++] = (short) goto_st.index();
        }
      }
      /* now we know how big to make the row. */
      reduce_goto_table[i] = new short[nentries + 2];
      System.arraycopy(temp_table, 0, reduce_goto_table[i], 0, nentries);

      /* end row with default value */
      reduce_goto_table[i][nentries++] = -1;
      reduce_goto_table[i][nentries] = -1;
    }

    /* emit the table. */
    out.println();
    out.println("  /** <code>reduce_goto</code> table. */");
    out.println("  protected static final short[][] _reduce_table = ");
    out.print("    unpackFromStrings(");
    do_table_as_string(out, reduce_goto_table);
    out.println(");");

    /* do the public accessor method */
    out.println();
    out.println("  /** Access to <code>reduce_goto</code> table. */");
    out.println("  @Override");
    out.println("  public short[][] reduce_table() {return _reduce_table;}");
    out.println();

    goto_table_time = System.currentTimeMillis() - start_time;
  }

  // print a string array encoding the given short[][] array.
  protected static void do_table_as_string(PrintWriter out, short[][] sa) {
    out.println("new String[] {");
    out.print("    \"");
    int nchar = 0, nbytes = 0;
    nbytes += do_escaped(out, (char) (sa.length >> 16));
    nchar = do_newline(out, nchar, nbytes);
    nbytes += do_escaped(out, (char) (sa.length & 0xFFFF));
    nchar = do_newline(out, nchar, nbytes);
    for (var element:sa) {
      nbytes += do_escaped(out, (char) (element.length >> 16));
      nchar = do_newline(out, nchar, nbytes);
      nbytes += do_escaped(out, (char) (element.length & 0xFFFF));
      nchar = do_newline(out, nchar, nbytes);
      for (var element2 : element) {
        // contents of string are (value+2) to allow for common -1, 0 cases
        // (UTF-8 encoding is most efficient for 0<c<0x80)
        nbytes += do_escaped(out, (char) (2 + element2));
        nchar = do_newline(out, nchar, nbytes);
      }
    }
    out.print("\" }");
  }

  // split string if it is very long; start new line occasionally for neatness
  protected static int do_newline(PrintWriter out, int nchar, int nbytes) {
    if (nbytes > 65500) {
      out.println("\", ");
      out.print("    \"");
    } else if (nchar > 11) {
      out.println("\" +");
      out.print("    \"");
    } else
      return nchar + 1;
    return 0;
  }

  // output an escape sequence for the given character code.
  protected static int do_escaped(PrintWriter out, char c) {
    StringBuilder escape = new StringBuilder();
    if (c <= 0xFF) {
      escape.append(Integer.toOctalString(c));
      while (escape.length() < 3)
        escape.insert(0, '0');
    } else {
      escape.append(Integer.toHexString(c));
      while (escape.length() < 4)
        escape.insert(0, '0');
      escape.insert(0, 'u');
    }
    escape.insert(0, '\\');
    out.print(escape);

    // return number of bytes this takes up in UTF-8 encoding.
    if (c == 0)
      return 2;
    if (c <= 0x7F)
      return 1;
    if (c <= 0x7FF)
      return 2;
    return 3;
  }

  /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

  /**
   * Emit the parser subclass with embedded tables.
   * 
   * @param out              stream to produce output on.
   * @param action_table     internal representation of the action table.
   * @param reduce_table     internal representation of the reduce-goto table.
   * @param start_st         start state of the parse machine.
   * @param start_prod       start production of the grammar.
   * @param compact_reduces  do we use most frequent reduce as default?
   * @param suppress_scanner should scanner be suppressed for compatibility?
   */
  public static void parser(PrintWriter out, parse_action_table action_table, parse_reduce_table reduce_table,
      int start_st, production start_prod, boolean compact_reduces, boolean suppress_scanner) throws internal_error {
    long start_time = System.currentTimeMillis();

    /* top of file */
    out.println();
    out.println("//----------------------------------------------------");
    out.println("// The following code was generated by " + version.title_str);
    out.println("//----------------------------------------------------");
    out.println();
    emit_package(out);

    out.println("import java.util.*;");
    /* user supplied imports */
    for (String s : import_list) {
      out.println("import " + s + ";");
    }
    if (locations())
      out.println("import java_cup.runtime.ComplexSymbolFactory.Location;");
    out.println("import java_cup.runtime.XMLElement;");

    /* class header */
    out.println();
    out.println("/** " + version.title_str + " generated parser.");
    out.println("  */");
    /* TUM changes; proposed by Henning Niss 20050628: added typeArgument */
    out.println("@SuppressWarnings({\"unused\", \"UnnecessaryUnicodeEscape\"})");
    out.println("public class " + parser_class_name + typeArgument() + " extends java_cup.runtime.lr_parser {");

    out.println();
    out.println(" @Override");
    out.println(" public final Class<?> getSymbolContainer() {");
    out.println("    return " + symbol_const_class_name + ".class;");
    out.println("}");

    /* constructors [CSA/davidm, 24-jul-99] */
    out.println();
    out.println("  /** Default constructor. */");
    out.println("  @Deprecated");
    out.println("  public " + parser_class_name + "() {super();}");
    if (!suppress_scanner) {
      out.println();
      out.println("  /** Constructor which sets the default scanner. */");
      out.println("  @Deprecated");
      out.println("  public " + parser_class_name + "(java_cup.runtime.Scanner s) {super(s);}");
      // TUM 20060327 added SymbolFactory aware constructor
      out.println();
      out.println("  /** Constructor which sets the default scanner. */");
      out.println("  public " + parser_class_name
          + "(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}");
    }

    /* emit the various tables */
    emit_production_table(out);
    do_action_table(out, action_table, compact_reduces);
    do_reduce_table(out, reduce_table);

    /* instance of the action encapsulation class */
    out.println("  /** Instance of action encapsulation class. */");
    out.println("  protected " + pre("actions") + " action_obj;");
    out.println();

    /* action object initializer */
    out.println("  /** Action encapsulation object initializer. */");
    out.println("  @Override");
    out.println("  protected void init_actions()");
    out.println("    {");
    /* TUM changes; proposed by Henning Niss 20050628: added typeArgument */
    out.println("      action_obj = new " + pre("actions") + typeArgument() + "(this);");
    out.println("    }");
    out.println();

    /* access to action code */
    out.println("  /** Invoke a user supplied parse action. */");
    out.println("  @Override");
    out.println("  public java_cup.runtime.Symbol do_action(");
    out.println("    int                        act_num,");
    out.println("    java_cup.runtime.lr_parser parser,");
    out.println("    java_cup.runtime.ArrayStack<java_cup.runtime.Symbol>    stack,");
    out.println("    int                        top)");
    out.println("    throws java.lang.Exception");
    out.println("  {");
    out.println("    /* call code in generated class */");
    out.println("    return action_obj." + pre("do_action(") + "act_num, parser, stack, top);");
    out.println("  }");
    out.println("");

    /* method to tell the parser about the start state */
    out.println("  /** Indicates start state. */");
    out.println("  @Override");
    out.println("  public int start_state() {return " + start_st + ";}");

    /* method to indicate start production */
    out.println("  /** Indicates start production. */");
    out.println("  @Override");
    out.println("  public int start_production() {return " + start_production.index() + ";}");
    out.println();

    /* methods to indicate EOF and error symbol indexes */
    out.println("  /** <code>EOF</code> Symbol index. */");
    out.println("  @Override");
    out.println("  public int EOF_sym() {return " + terminal.EOF.index() + ";}");
    out.println();
    out.println("  /** <code>error</code> Symbol index. */");
    out.println("  @Override");
    out.println("  public int error_sym() {return " + terminal.error.index() + ";}");
    out.println();

    /* user supplied code for user_init() */
    if (init_code != null) {
      out.println();
      out.println("  /** User initialization code. */");
      out.println("  public void user_init() throws java.lang.Exception");
      out.println("    {");
      out.println(init_code);
      out.println("    }");
    }

    /* user supplied code for scan */
    if (scan_code != null) {
      out.println();
      out.println("  /** Scan to get the next Symbol. */");
      out.println("  @Override");
      out.println("  public java_cup.runtime.Symbol scan()");
      out.println("    throws java.lang.Exception");
      out.println("    {");
      out.println(scan_code);
      out.println("    }");
    }

    /* user supplied code */
    if (parser_code != null) {
      out.println();
      out.println(parser_code);
    }

    /* put out the action code class */
    if (!_xmlactions)
      emit_action_code(out, start_prod);
    else
      emit_xmlaction_code(out, start_prod);

    /* end of class */
    out.println("}");

    parser_time = System.currentTimeMillis() - start_time;
  }

  public static void node_classes(File dir) throws internal_error, IOException {
    for (non_terminal nt : non_terminal.all()) {
      if (!nt.isListExpr() && !nt.isEmptySymbol()) {
        node_class(dir, nt);
      }
    }
  }

  private static void node_class(File dir, non_terminal nt) throws internal_error, IOException {
    String className = symbol.getNodeClassName(nt.name());
    try (
      BufferedWriter writer = Files.newBufferedWriter(new File(dir, className + ".java").toPath());
      PrintWriter out = new PrintWriter(writer)
    ) {
      emit_package(out);
      out.println("import java.util.*;");
      out.println("import java_cup.runtime.IAstNode;");
      out.println();
      out.println("@SuppressWarnings({");
      out.println("  \"UnnecessaryLocalVariable\",");
      out.println("  \"EnhancedSwitchMigration\",");
      out.println("  \"SwitchStatementWithTooFewBranches\",");
      out.println("  \"unchecked\",");
      out.println("  \"RedundantSuppression\"");
      out.println("})");
      out.println("public class " + className + " implements IAstNode {");
      out.println();
      int _index = 0;
      Map<String, String> label2TypeMap = new HashMap<>();
      Map<String, Integer> labelIndexMap = new HashMap<>();
      List<List<String>> states = new ArrayList<>(nt.num_productions());
      for (production production : nt.productions()) {
        int length = production.rhs_length();
        if (length == 0) continue;
        List<String> state = new ArrayList<>((length + 1) / 2);
        states.add(state);
        for (var entry : production.getLabel2SymbolPartMap().entrySet()) {
          var label = entry.getKey();
          var symbol = entry.getValue().the_symbol();
          _index = handleVar(label2TypeMap, labelIndexMap, state, _index, label, symbol, null);
        }
      }
      var bestAssignment = SymbolStateCompression.assignNumbers(states);
      boolean isOnlyValue = bestAssignment.idCount == 1;
      boolean isIntMask = labelIndexMap.size() <= 32;
      boolean noMask = labelIndexMap.size() == bestAssignment.idCount;
      if (!bestAssignment.isEmpty()) {
        if (isOnlyValue) out.println("  private Object value = null;");
        else out.println("  private final Object[] values = new Object[" + bestAssignment.idCount + "];");
      }
      if (!label2TypeMap.isEmpty() && !noMask) {
        if (isIntMask) out.println("  private int validMask;");
        else out.println("  private BitSet validMask;");
      }
      out.println();
      StringBuilder getterBuilder = new StringBuilder(256);
      StringBuilder existsBuilder = new StringBuilder(256);
      getterBuilder.append("  @Override\n")
              .append("  public Object getByLabel(String label) {\n")
              .append("    switch (label) {\n");
      existsBuilder.append("  @Override\n")
              .append("  public boolean hasLabel(String label) {\n")
              .append("    switch (label) {\n");
      for (Map.Entry<String, String> entry : label2TypeMap.entrySet()) {
        String label = entry.getKey();
        String type = entry.getValue();
        Integer id = bestAssignment.get(label);
        if (!labelIndexMap.containsKey(label)) continue; // skip inline var
        int index = labelIndexMap.get(label);
        String varName = castToStName(label);
        // Generate getter and checker
        if (type.isEmpty()) {
          existsBuilder.append("      case \"")
                  .append(label)
                  .append("\": return ")
                  .append(label)
                  .append("();\n");
        } else {
          getterBuilder.append("      case \"")
                  .append(label)
                  .append("\": return get")
                  .append(varName)
                  .append("();\n");
          existsBuilder.append("      case \"")
                  .append(label)
                  .append("\": return has")
                  .append(varName)
                  .append("();\n");
        }
        if (!type.isEmpty()) {
          // Generate getters for variables of non-marked existence types
          out.println("  public " + type + " get" + varName + "() {");
          if (!noMask) {
            out.println("    if (!has" + varName + "()) return null;");
          }
          if (isOnlyValue) out.println("    return (" + type + ") value;");
          else out.println("    return (" + type + ") values[" + id + "];");
          out.println("  }");
          out.println();
        }
        // Generate methods to check existence for all types
        if (type.isEmpty()) {
          out.println("  public boolean " + label + "() {");
        } else {
          out.println("  public boolean has" + varName + "() {");
        }
        if (noMask) {
          if (isOnlyValue) out.println("    return value != null;");
          else out.println("    return values[" + id + "] != null;");
        } else {
          if (isIntMask) out.println("    return (validMask & 0x" + Integer.toHexString(1 << index) + ") != 0;");
          else out.println("    return validMask.get(" + index + ");");
        }
        out.println("  }");
        out.println();
      }
      getterBuilder.append("      default: return null;\n    }\n  }\n");
      existsBuilder.append("      default: return false;\n    }\n  }\n");
      out.println(getterBuilder);
      out.println(existsBuilder);
      static_builders(out, className, nt, label2TypeMap, labelIndexMap, bestAssignment);
      out.println('}');
    }
  }

  private static int handleVar(
    Map<String, String> label2TypeMap,
    Map<String, Integer> labelIndexMap,
    List<String> state,
    int index,
    String label,
    symbol sym,
    String prefix
  ) throws internal_error {
    boolean isExistenceVar = isExistenceVar(label);
    String inlineVarName = isExistenceVar ? null : Main.ast_flatten.getInlineName(label);
    String type = isExistenceVar ? "" : sym.astClassName();
    label = joinName(prefix, label);
    label2TypeMap.put(label, type);
    if (inlineVarName != null && sym.is_non_term()) {
      var subMap = ((non_terminal) sym).getInlineExpr();
      for (var subEntry : subMap.entrySet()) {
        index = handleVar(
          label2TypeMap, labelIndexMap, state, index,
          subEntry.getKey(), subEntry.getValue(), inlineVarName
        );
      }
    } else if (!isExistenceVar) {
      state.add(label);
      labelIndexMap.put(label, index++);
    } else {
      labelIndexMap.put(label, index++);
    }
    return index;
  }

  private static void static_builders(
    PrintWriter out, String className, non_terminal nt,
    Map<String, String> typeMap,
    Map<String, Integer> labelIndexMap,
    SymbolStateCompression bestAssignment
  ) throws internal_error {
    Set<String> record = new TreeSet<>();
    for (production production : nt.productions()) {
      var hashName = production.getHashName();
      if (!record.add(hashName)) continue;
      out.print("  static " + className + ' ' + hashName + "(");
      var map = production.getLabel2SymbolPartMap();
      boolean isFirst = true;
      for (var label : map.keySet()) {
        var type = typeMap.get(label);
        if (type.isEmpty()) continue;
        if (isFirst) {
          out.println();
          isFirst = false;
        } else out.println(",");
        out.print("    " + type + " _" + label);
      }
      if (!isFirst) {
        out.println();
        out.print("  ");
      }
      out.println(") {");
      out.println("    " + className + " result = new " + className + "();");
      BitSet flag = new BitSet(map.size());
      Map<String, String> inlineExistence = new HashMap<>();
      boolean isOnlyValue = bestAssignment.idCount == 1;
      for (var entry : map.entrySet()) {
        var label = entry.getKey();
        var sym = entry.getValue().the_symbol();
        var inlineName = Main.ast_flatten.getInlineName(label);
        if (sym.is_non_term() && inlineName != null) {
          for (String subLabel : ((non_terminal) sym).getInlineExpr().keySet()) {
            var subLabelKey = joinName(inlineName, subLabel);
            var type = typeMap.get(subLabelKey);
            if (type.isEmpty()) {
              inlineExistence.put(subLabelKey, '_' + label + '.' + subLabel + "()");
            } else {
              if (isOnlyValue) out.println("    result.value = _" + label + ".get" + castToStName(subLabel) + "();");
              else {
                Integer id = bestAssignment.get(subLabelKey);
                var stName = castToStName(subLabel);
                out.println("    if (_" + label + ".has" + stName + "())");
                out.println("      result.values[" + id + "] = " + '_' + label + ".get" + stName + "();");
              }
            }
          }
        } else {
          int index = labelIndexMap.get(label);
          flag.set(index);
          var type = typeMap.get(label);
          if (!type.isEmpty()) {
            if (isOnlyValue) out.println("    result.value = " + '_' + label + ';');
            else {
              Integer id = bestAssignment.get(label);
              out.println("    result.values[" + id + "] = " + '_' + label + ';');
            }
          }
        }
      }
      boolean hasMask = !flag.isEmpty() && labelIndexMap.size() != bestAssignment.idCount;
      if (hasMask) {
        if (labelIndexMap.size() <= 32) {
          out.println("    int validMask = 0x" + Integer.toHexString((int) flag.toLongArray()[0]) + ';');
        } else {
          out.println("    BitSet validMask = new BitSet();");
          for (int k = 0; k < flag.length(); k++) {
            if (flag.get(k)) {
              out.println("    validMask.set(" + k + ");");
            }
          }
        }
      }
      if (!inlineExistence.isEmpty()) {
        if (!hasMask) {
          if (labelIndexMap.size() <= 32) {
            out.println("    int validMask = 0x0;");
          } else {
            out.println("    BitSet validMask = new BitSet();");
          }
        }
        if (labelIndexMap.size() <= 32) {
          var expr = inlineExistence.entrySet().stream()
            .map(entry -> {
              var label = entry.getKey();
              int k = labelIndexMap.get(label);
              return "(" + entry.getValue() + " ? 0x" + Integer.toHexString(1 << k) + " : 0)";
            }).collect(Collectors.joining("\n      | "));
          out.println("    validMask |= " + expr + ';');
        } else {
          for (var entry : inlineExistence.entrySet()) {
            var label = entry.getKey();
            int k = labelIndexMap.get(label);
            out.println("    validMask.set(" + k + ", " + entry.getValue() + ");");
          }
        }
      }
      if (hasMask || !inlineExistence.isEmpty()) {
        out.println("    result.validMask = validMask;");
      }
      out.println("    return result;");
      out.println("  }");
      out.println();
    }
  }

  /**
   * Emit code for generic XML parsetree output.
   * 
   * @param out        stream to produce output on.
   * @param start_prod the start production of the grammar.
   */
  protected static void emit_xmlaction_code(PrintWriter out, production start_prod) throws internal_error {
    production prod;

    long start_time = System.currentTimeMillis();

    /* class header */
    out.println();
    out.println("/** Cup generated class to encapsulate user supplied action code.*/");
    out.println("class " + pre("actions") + typeArgument() + " {");
    /* user supplied code */
    if (action_code != null) {
      out.println();
      out.println(action_code);
    }

    /* field for parser object */
    out.println("  private final " + parser_class_name + typeArgument() + " parser;");

    /* constructor */
    out.println();
    out.println("  /** Constructor */");
    out.println("  " + pre("actions") + "(" + parser_class_name + typeArgument() + " parser) {");
    out.println("    this.parser = parser;");
    out.println("  }");

    out.println();
    for (int instancecounter = 0; instancecounter <= production.number() / UPPERLIMIT; instancecounter++) {
      out.println("  /** Method " + instancecounter + " with the actual generated action code for actions "
          + (instancecounter * UPPERLIMIT) + " to " + ((instancecounter + 1) * UPPERLIMIT) + ". */");
      out.println("  public final java_cup.runtime.Symbol " + pre("do_action_part")
          + String.format("%08d", instancecounter) + "(");
      out.println("    int                        " + pre("act_num,"));
      out.println("    java_cup.runtime.lr_parser " + pre("parser,"));
      out.println("    java_cup.runtime.ArrayStack<java_cup.runtime.Symbol>    " + pre("stack,"));
      out.println("    int                        " + pre("top)"));
      out.println("    throws java.lang.Exception");
      out.println("    {");
      out.println("      /* Symbol object for return from actions */");
      out.println("      java_cup.runtime.Symbol " + pre("result") + ";");
      out.println();
      out.println("      /* select the action based on the action number */");
      out.println("      switch (" + pre("act_num") + ")");
      out.println("        {");
      // START Switch
      /* emit action code for each production as a separate case */
      int proditeration = instancecounter * UPPERLIMIT;
      prod = production.find(proditeration);
      for (; proditeration < Math.min((instancecounter + 1) * UPPERLIMIT,
          production.number()); prod = production.find(++proditeration)) {
        /* case label */
        out.println("          /*. . . . . . . . . . . . . . . . . . . .*/");
        out.println("          case " + prod.index() + ": // " + prod.to_simple_string());

        /* give them their own block to work in */
        out.println("            {");

        out.println("                XMLElement RESULT;");

        // Generate the XML Output
        StringBuilder nested = new StringBuilder();
        for (int rhsi = 0; rhsi < prod.rhs_length(); rhsi++) {
          if (!(prod.rhs(rhsi) instanceof symbol_part))
            continue;
          String label = prod.rhs(rhsi).label();
          symbol_part sym = (symbol_part) prod.rhs(rhsi);
          if (label == null) {
            if (!_genericlabels)
              continue;
            label = sym.the_symbol().name() + rhsi;
          }
          if (sym.the_symbol().is_non_term())
            nested.append(",(XMLElement)").append(label);
          else
            nested.append(",new XMLElement.Terminal(")
                    .append(label)
                    .append("xleft,\"")
                    .append(label)
                    .append("\",")
                    .append(label)
                    .append(",")
                    .append(label)
                    .append("xright)");
        }

        if (prod.action() != null && prod.action().code_string() != null)
          out.println(prod.action().code_string());

        // determine the variant:
        int variant = 0;
        for (int i = 0; i < proditeration; i++)
          if (production.find(i).lhs().equals(prod.lhs()))
            variant++;

        String lhsname = prod.lhs().the_symbol().name().replace('$', '_');
        out.println(
            "                RESULT = new XMLElement.NonTerminal(\"" + lhsname + "\"," + variant + nested + ");");

        /*
         * Create the code that assigns the left and right values of the new Symbol that
         * the production is reducing to
         */
        if (emit.lr_values()) {
          int loffset;
          String leftstring, rightstring;
          rightstring = emit.pre("stack") + ".peek()";
          if (prod.rhs_length() == 0)
            leftstring = rightstring;
          else {
            loffset = prod.rhs_length() - 1;
            leftstring = emit.pre("stack")
                + ((loffset == 0) ? (".peek()") : (".elementAt(" + emit.pre("top") + "-" + loffset + ")"));
          }
          out.println("              " + pre("result") + " = parser.getSymbolFactory().newSymbol(" + "\""
              + prod.lhs().the_symbol().name() + "\"," + prod.lhs().the_symbol().index() + ", " + leftstring + ", "
              + rightstring + ", RESULT);");
        } else {
          out.println("              " + pre("result") + " = parser.getSymbolFactory().newSymbol(" + "\""
              + prod.lhs().the_symbol().name() + "\"," + prod.lhs().the_symbol().index() + ", RESULT);");
        }

        /* end of their block */
        out.println("            }");

        /* if this was the start production, do action for accept */
        if (prod == start_prod) {
          out.println("          /* ACCEPT */");
          out.println("          " + pre("parser") + ".done_parsing();");
        }

        /* code to return lhs symbol */
        out.println("          return " + pre("result") + ";");
        out.println();
      }

      // END Switch
      out.println("          /* . . . . . .*/");
      out.println("          default:");
      out.println("            throw new Exception(");
      out.println(
          "               \"Invalid action number \"+" + pre("act_num") + "+\"found in " + "internal parse table\");");
      out.println();
      out.println("        }");
      out.println("    } /* end of method */");
    }

    /* action method head */
    out.println();
    out.println("  /** Method splitting the generated action code into several parts. */");
    out.println("  public final java_cup.runtime.Symbol " + pre("do_action") + "(");
    out.println("    int                        " + pre("act_num,"));
    out.println("    java_cup.runtime.lr_parser " + pre("parser,"));
    out.println("    java_cup.runtime.ArrayStack<java_cup.runtime.Symbol>    " + pre("stack,"));
    out.println("    int                        " + pre("top)"));
    out.println("    throws java.lang.Exception");
    out.println("    {");

    if (production.number() < UPPERLIMIT) { // Make it simple for the optimizer to inline!
      out.println("              return " + pre("do_action_part") + String.format("%08d", 0) + "(");
      out.println("                               " + pre("act_num,"));
      out.println("                               " + pre("parser,"));
      out.println("                               " + pre("stack,"));
      out.println("                               " + pre("top);"));
      out.println("    }");

      /* end of class */
      out.println("}");
      out.println();

      action_code_time = System.currentTimeMillis() - start_time;
      return;
    }

    /* switch top */
    out.println("      /* select the action handler based on the action number */");
    out.println("      switch (" + pre("act_num") + "/" + UPPERLIMIT + ")");
    out.println("        {");

    /* emit action code for each production as a separate case */
    for (int instancecounter = 0; instancecounter <= production.number() / UPPERLIMIT; instancecounter++) {
      /* case label */
      out.println("          /*. . . . . . . . " + (instancecounter * UPPERLIMIT) + " < #action < "
          + ((instancecounter + 1) * UPPERLIMIT) + ". . . . . . . . . . . .*/");
      out.println("          case " + instancecounter + ": ");
      out.println("              return " + pre("do_action_part")
          + String.format("%08d", instancecounter) + "(");
      out.println("                               " + pre("act_num,"));
      out.println("                               " + pre("parser,"));
      out.println("                               " + pre("stack,"));
      out.println("                               " + pre("top);"));
    }

    out.println("          /* . . . no valid action number: . . .*/");
    out.println("          default:");
    out.println("            throw new Exception(\"Invalid action number found in internal parse table\");");
    out.println();
    out.println("        }      /* end of switch */");

    /* end of method */
    out.println("    }");

    /* end of class */
    out.println("}");
    out.println();

    action_code_time = System.currentTimeMillis() - start_time;
  }

  /*-----------------------------------------------------------*/
}