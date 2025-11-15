import java.io.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.*;
import java_cup.runtime.symbol.complex.*;
import java.util.*;

%%

%class Lexer
%cup
%line
%char
%column
%implements CSymbols

%{

    ComplexSymbolFactory symbolFactory;
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
	    this(in);
	    symbolFactory = sf;
    }
  
    private Symbol symbol(int sym) {
        var location = ComplexLocation.of(yyline + 1, yycolumn + 1, yyline + 1, yycolumn + yylength());
        return symbolFactory.newSymbol(sym, location);
    }

    private Symbol symbol(int sym, Object val) {
        var location = ComplexLocation.of(yyline + 1, yycolumn + 1, yyline + 1, yycolumn + yylength());
        return symbolFactory.newSymbol(sym, location, val);
    }

    private Symbol symbol(int sym, Object val,int buflength) {
        var location = ComplexLocation.of(
            yyline + 1, yycolumn + yylength() - buflength,
            yyline + 1, yycolumn + yylength()
        );
        return symbolFactory.newSymbol(sym, location, val);
    }
    
  //    static TreeSet typeset = new TreeSet();
    
    private int typecheck(String s){

	if (Parser.lookupType(s.trim())) {
	    return TYPE_NAME;
	}

	else {
	    return IDENTIFIER;
	}
	
    }
    
%}

%eofval{
    var location = ComplexLocation.of(yyline + 1, yycolumn + 1, yyline + 1, yycolumn + 1);
    return symbolFactory.newSymbol(EOF, location);
%eofval}

D		=	[0-9]
L		=	[a-zA-Z_]
H		=	[a-fA-F0-9]
E		=	[Ee][+-]?{D}+
FS		=	(f|F|l|L)
IS		=	(u|U|l|L)*
TC              =       "/*" [^*] ~"*/" | "/*" "*"+ "/"
EC              =       "//" [^\r\n]* {new_line}    
new_line        =       \r|\n|\r\n
white_space     =       {new_line} | [ \t\f]

%%
{EC}                    { }
{TC}                    { }

"auto"			{ return symbol(AUTO,yytext()); }
"break"			{ return symbol(BREAK,yytext()); }
"case"			{ return symbol(CASE,yytext()); }
"char"			{ return symbol(CHAR,yytext()); }
"const"			{ return symbol(CONST,yytext()); }
"continue"		{ return symbol(CONTINUE,yytext()); }
"default"		{ return symbol(DEFAULT,yytext()); }
"do"			{ return symbol(DO,yytext()); }
"double"		{ return symbol(DOUBLE,yytext()); }
"else"			{ return symbol(ELSE,yytext()); }
"enum"			{ return symbol(ENUM,yytext()); }
"extern"		{ return symbol(EXTERN,yytext()); }
"float"			{ return symbol(FLOAT,yytext()); }
"for"			{ return symbol(FOR,yytext()); }
"goto"			{ return symbol(GOTO,yytext()); }
"if"			{ return symbol(IF,yytext()); }
"int"			{ return symbol(INT,yytext()); }
"long"			{ return symbol(LONG,yytext()); }
"register"		{ return symbol(REGISTER,yytext()); }
"return"		{ return symbol(RETURN,yytext()); }
"short"			{ return symbol(SHORT,yytext()); }
"signed"		{ return symbol(SIGNED,yytext()); }
"sizeof"		{ return symbol(SIZEOF,yytext()); }
"static"		{ return symbol(STATIC,yytext()); }
"struct"		{ return symbol(STRUCT,yytext()); }
"switch"		{ return symbol(SWITCH,yytext()); }
"typedef"		{ return symbol(TYPEDEF,yytext()); }
"union"			{ return symbol(UNION,yytext()); }
"unsigned"		{ return symbol(UNSIGNED,yytext()); }
"void"			{ return symbol(VOID,yytext()); }
"volatile"		{ return symbol(VOLATILE,yytext()); }
"while"			{ return symbol(WHILE,yytext()); }

{L}({L}|{D})*		{ return symbol(typecheck(yytext()), yytext()); }

0[xX]{H}+{IS}?		{ return symbol(CONSTANT,yytext()); }
0{D}+{IS}?		{ return symbol(CONSTANT,yytext()); }
{D}+{IS}?		{ return symbol(CONSTANT,yytext()); }
L?'(\\.|[^\\'])+'	{ return symbol(CONSTANT,yytext()); }

{D}+{E}{FS}?		{ return symbol(CONSTANT,yytext()); }
{D}*"."{D}+({E})?{FS}?	{ return symbol(CONSTANT,yytext()); }
{D}+"."{D}*({E})?{FS}?	{ return symbol(CONSTANT,yytext()); }

L?\"(\\.|[^\\\"])*\"	{ return symbol(STRING_LITERAL,yytext()); }

"..."			{ return symbol(ELLIPSIS); }
">>="			{ return symbol(RIGHT_ASSIGN); }
"<<="			{ return symbol(LEFT_ASSIGN); }
"+="			{ return symbol(ADD_ASSIGN); }
"-="			{ return symbol(SUB_ASSIGN); }
"*="			{ return symbol(MUL_ASSIGN); }
"/="			{ return symbol(DIV_ASSIGN); }
"%="			{ return symbol(MOD_ASSIGN); }
"&="			{ return symbol(AND_ASSIGN); }
"^="			{ return symbol(XOR_ASSIGN); }
"|="			{ return symbol(OR_ASSIGN); }
">>"			{ return symbol(RIGHT_OP); }
"<<"			{ return symbol(LEFT_OP); }
"++"			{ return symbol(INC_OP,"++"); }
"--"			{ return symbol(DEC_OP,"--"); }
"->"			{ return symbol(PTR_OP); }
"&&"			{ return symbol(AND_OP,"&&"); }
"||"			{ return symbol(OR_OP,"||"); }
"<="			{ return symbol(LE_OP,"<="); }
">="			{ return symbol(GE_OP,">="); }
"=="			{ return symbol(EQ_OP,"=="); }
"!="			{ return symbol(NE_OP,"!="); }
";"			{ return symbol(SEMI); }
("{"|"<%")		{ return symbol(CURLYL); }
("}"|"%>")		{ return symbol(CURLYR); }
","			{ return symbol(COMMA); }
":"			{ return symbol(COLON); }
"="			{ return symbol(ASSIGN); }
"("			{ return symbol(PARAL); }
")"			{ return symbol(PARAR); }
("["|"<:")		{ return symbol(SQUAREDL); }
("]"|":>")		{ return symbol(SQUAREDR); }
"."			{ return symbol(POINT); }
"&"			{ return symbol(ADRESS); }
"!"			{ return symbol(NOT,"!"); }
"~"			{ return symbol(TILDE); }
"-"			{ return symbol(MINUS); }
"+"			{ return symbol(PLUS,"+"); }
"*"			{ return symbol(MUL,"*"); }
"/"			{ return symbol(DIVIDE,"/"); }
"%"			{ return symbol(MODULUS,"%"); }
"<"			{ return symbol(LESS,"<"); }
">"			{ return symbol(GREATER,">"); }
"^"			{ return symbol(XOR); }
"|"			{ return symbol(OR); }
"?"			{ return symbol(COND); }

{white_space}		{ /* ignore bad characters */ }
.|\n			{ System.err.println("Fehler: unbekanntes Zeichen:"+yytext()+" "+(yyline+1)+"/"+(yycolumn+1)); }