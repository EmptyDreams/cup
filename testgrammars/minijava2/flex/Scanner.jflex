package miniparser;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%

%public
%class Lexer
%cup
%implements sym, minijava.Constants
%char
%line
%column

%{
    StringBuilder string = new StringBuilder();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
	this(in);
	symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;

  private Symbol symbol(String name, int sym) {
      return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+yylength(),yychar+yylength()));
  }
  
  private Symbol symbol(String name, int sym, Object val) {
      Location left = new Location(yyline+1,yycolumn+1,yychar);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  } 
  private Symbol symbol(String name, int sym, Object val,int buflength) {
      Location left = new Location(yyline+1,yycolumn+yylength()-buflength,yychar+yylength()-buflength);
      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
      return symbolFactory.newSymbol(name, sym, left, right,val);
  }       
  private void error(String message) {
    System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
  }
%} 

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline+1,yycolumn+1,yychar), new Location(yyline+1,yycolumn+1,yychar+1));
%eofval}


Ident = [a-zA-Z$_] [a-zA-Z0-9$_]*

IntLiteral = 0 | [1-9][0-9]*

BoolLiteral = true | false

new_line = \r|\n|\r\n;

white_space = {new_line} | [ \t\f]

%state STRING

%%

<YYINITIAL>{
/* keywords */
"int"             { return symbol("int",TYPE, Integer.valueOf( INTTYPE ) ); }
"if"              { return symbol("if",IF); }
"else"            { return symbol("else",ELSE); }
"while"           { return symbol("while",WHILE); }
"read"            { return symbol("read",READ); }
"write"           { return symbol("write",WRITE); }

/* names */
{Ident}           { return symbol("Identifier",IDENT, yytext()); }
  
/* string literals */

/* char literal */

/* bool literal */
{BoolLiteral} { return symbol("Boolconst",BOOLCONST, new Boolean(Boolean.parseBool(yytext()))); }

/* literals */
{IntLiteral} { return symbol("Intconst",INTCONST, Integer.valueOf(Integer.parseInt(yytext()))); }



/* separators */
  \"              { string.setLength(0); yybegin(STRING); }
";"               { return symbol("semicolon",SEMICOLON); }
","               { return symbol("comma",COMMA); }
"("               { return symbol("(",LPAR); }
")"               { return symbol(")",RPAR); }
"{"               { return symbol("{",BEGIN); }
"}"               { return symbol("}",END); }
"="               { return symbol("=",ASSIGN); }
"+"               { return symbol("plus",BINOP, Integer.valueOf( PLUS ) ); }
"-"               { return symbol("minus",BINOP, Integer.valueOf( MINUS ) ); }
"*"               { return symbol("mult",BINOP, Integer.valueOf( MULT ) ); }
"/"               { return symbol("div",BINOP, Integer.valueOf( DIV ) ); }
"%"               { return symbol("mod",BINOP, Integer.valueOf( MOD ) ); }
"<="              { return symbol("leq",COMP,  Integer.valueOf( LEQ ) ); }
">="              { return symbol("gtq",COMP,  Integer.valueOf( GTQ ) ); }
"=="              { return symbol("eq",COMP,  Integer.valueOf( EQ  ) ); }
"!="              { return symbol("neq",COMP,  Integer.valueOf( NEQ ) ); }
"<"               { return symbol("le",COMP,  Integer.valueOf( LE  ) ); }
">"               { return symbol("gt",COMP,  Integer.valueOf( GT  ) ); }
"&&"              { return symbol("and",BBINOP,Integer.valueOf( AND ) ); }
"||"              { return symbol("or",BBINOP,Integer.valueOf( OR  ) ); }
"!"               { return symbol("not",BUNOP); }



{white_space}     { /* ignore */ }

}

<STRING> {
  \"                             { yybegin(YYINITIAL); 
      return symbol("StringConst",STRINGCONST,string.toString(),string.length()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}


/* error fallback */
.|\n              {  /* throw new Error("Illegal character <"+ yytext()+">");*/
		    error("Illegal character <"+ yytext()+">");
                  }