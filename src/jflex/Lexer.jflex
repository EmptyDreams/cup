package java_cup;
import java_cup.runtime.symbol.complex.*;
import java_cup.runtime.Symbol;
import java.lang.Error;
import java.io.IOException;
import java.io.InputStreamReader;

%%

%class Lexer
%implements GrammarSymConstants
%public
%unicode
%line
%column
%cup
%{
    public Lexer(ComplexSymbolFactory sf) {
        this(new InputStreamReader(System.in));
        symbolFactory = sf;
    }

    private StringBuilder sb;
    private ComplexSymbolFactory symbolFactory;
    private int csline,cscolumn;

    public Symbol symbol(int code){
	    return symbolFactory.newSymbol(code,new Location(yyline+1,yycolumn+1-yylength()),new Location(yyline+1,yycolumn+1));
    }
    public Symbol symbol(int code, String lexem){
	    return symbolFactory.newSymbol(code, new Location(yyline+1, yycolumn +1), new Location(yyline+1,yycolumn+yylength()), lexem);
    }
    protected void emit_warning(String message){
	    ErrorManager.getManager().emit_warning("Scanner at " + (yyline+1) + "(" + (yycolumn+1) + "): " + message);
    }
    protected void emit_error(String message){
	    ErrorManager.getManager().emit_error("Scanner at " + (yyline+1) + "(" + (yycolumn+1) +  "): " + message);
    }
%}

Newline = \r | \n | \r\n
Whitespace = [ \t\f] | {Newline}

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" {CommentContent} \*+ "/"
EndOfLineComment = "//" [^\r\n]* {Newline}
CommentContent = ( [^*] | \*+[^*/] )*

ident = ([:jletter:] | "_" ) ([:jletterdigit:] | [:jletter:] | "_" )*


%eofval{
    return symbolFactory.newSymbol(GrammarSymConstants.EOF);
%eofval}

%state CODESEG

%%  

<YYINITIAL> {

  {Whitespace}  {                                       }
  "?"           { return symbol(QUESTION);              }
  "!"           { return symbol(EXCLAMATION);           }
  ";"           { return symbol(SEMI);                  }
  ","           { return symbol(COMMA);                 }
  "*"           { return symbol(STAR);                  }
  "."           { return symbol(DOT);                   }
  "+"           { return symbol(PLUS);                  }
  "|"           { return symbol(BAR);                   }
  "("           { return symbol(LPAREN);                }
  ")"           { return symbol(RPAREN);                }
  "["           { return symbol(LBRACK);                }
  "]"           { return symbol(RBRACK);                }
  ":"           { return symbol(COLON);                 }
  "::"          { return symbol(COLON_COLON);           }
  "::="         { return symbol(COLON_COLON_EQUALS);    }
  "%prec"       { return symbol(PERCENT_PREC);          }
  "%namer"      { return symbol(PERCENT_NAMER);         }
  ">"           { return symbol(GT);                    }
  "<"           { return symbol(LT);                    }
  "..."         { return symbol(SPREAD);                }
  {Comment}     {                                       }
  "{:"          { sb = new StringBuilder(); csline=yyline+1; cscolumn=yycolumn+1; yybegin(CODESEG);    }
  "package"     { return symbol(PACKAGE);       }
  "import"      { return symbol(IMPORT);	    }
  "static"      { return symbol(STATIC);	    }
  "class"       { return symbol(CLASS); 	    }
  "code"        { return symbol(CODE);		    }
  "action"      { return symbol(ACTION);	    }
  "parser"      { return symbol(PARSER);	    }
  "terminal"    { return symbol(TERMINAL);	    }
  "non"         { return symbol(NON);		    }
  "nonterminal" { return symbol(NONTERMINAL);   }
  "init"        { return symbol(INIT);		    }
  "scan"        { return symbol(SCAN);		    }
  "with"        { return symbol(WITH);		    }
  "start"       { return symbol(START);		    }
  "precedence"  { return symbol(PRECEDENCE);    }
  "left"        { return symbol(LEFT);		    }
  "right"       { return symbol(RIGHT);		    }
  "nonassoc"    { return symbol(NONASSOC);      }
  "extends"     { return symbol(EXTENDS);       }
  "super"       { return symbol(SUPER);         }
  {ident}       { return symbol(ID,yytext());   }
  
}

<CODESEG> {
  ":}"         { yybegin(YYINITIAL); return symbolFactory.newSymbol(CODE_STRING, new Location(csline, cscolumn),new Location(yyline+1,yycolumn+1+yylength()), sb.toString()); }
  .|\n            { sb.append(yytext()); }
}

// error fallback
.|\n          { emit_warning("Unrecognized character '" +yytext()+"' -- ignored"); }