import java_cup.runtime.*;
import java_cup.runtime.symbol.complex.ComplexSymbolFactory;
import java_cup.runtime.symbol.complex.ComplexLocation;

import java.io.IOException;
import java.io.Reader;

/** Hand-written scanner for the astloc grammar. Produces 1-based real
 *  positions via ComplexLocation.ofInclusive; the EOF token carries the
 *  position where the input ends. */
public class scanner implements Scanner {

    private final SymbolFactory sf;
    private final Reader in;

    /** line/column of {@link #nextChar}, both 1-based */
    private int line = 1, column = 1;
    private int nextChar;

    public scanner(Reader in, SymbolFactory sf) throws IOException {
        this.in = in;
        this.sf = sf;
        this.nextChar = in.read();
    }

    private void advance() throws IOException {
        int old = nextChar;
        nextChar = in.read();
        if (old == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
    }

    private ComplexLocation loc(int startLine, int startColumn, int endLine, int endColumn) {
        return ComplexLocation.ofInclusive(startLine, startColumn, endLine, endColumn);
    }

    @Override
    public Symbol next_token() throws IOException {
        while (nextChar == ' ' || nextChar == '\t' || nextChar == '\r' || nextChar == '\n') {
            advance();
        }
        int startLine = line, startColumn = column;
        if (nextChar == -1) {
            return sf.newSymbol(AstLocParserSym.EOF, loc(line, column, line, column));
        }
        if (nextChar == ';') {
            advance();
            return sf.newSymbol(AstLocParserSym.SEMI, loc(startLine, startColumn, startLine, startColumn));
        }
        if (nextChar == ',') {
            advance();
            return sf.newSymbol(AstLocParserSym.COMMA, loc(startLine, startColumn, startLine, startColumn));
        }
        if (nextChar == '*') {
            advance();
            return sf.newSymbol(AstLocParserSym.STAR,
                loc(startLine, startColumn, startLine, startColumn), "*");
        }
        if (Character.isDigit(nextChar)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(nextChar)) {
                sb.append((char) nextChar);
                advance();
            }
            return sf.newSymbol(AstLocParserSym.NUM,
                loc(startLine, startColumn, line, column - 1), sb.toString());
        }
        if (nextChar == '"') {
            advance();
            StringBuilder sb = new StringBuilder();
            while (nextChar != '"') {
                if (nextChar == -1 || nextChar == '\n') {
                    throw new IOException("unterminated string at " + startLine + ":" + startColumn);
                }
                sb.append((char) nextChar);
                advance();
            }
            advance();
            return sf.newSymbol(AstLocParserSym.STRING,
                loc(startLine, startColumn, line, column - 1), sb.toString());
        }
        if (Character.isJavaIdentifierStart(nextChar)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isJavaIdentifierPart(nextChar)) {
                sb.append((char) nextChar);
                advance();
            }
            return keywordOrId(sb.toString(), loc(startLine, startColumn, line, column - 1));
        }
        throw new IOException("unexpected character '" + (char) nextChar + "' at " + line + ":" + column);
    }

    private Symbol keywordOrId(String word, ComplexLocation location) {
        switch (word) {
            case "trail":   return sf.newSymbol(AstLocParserSym.TRAIL, location);
            case "lead":    return sf.newSymbol(AstLocParserSym.LEAD, location);
            case "both":    return sf.newSymbol(AstLocParserSym.BOTH, location);
            case "mix":     return sf.newSymbol(AstLocParserSym.MIX, location);
            case "list":    return sf.newSymbol(AstLocParserSym.LIST, location);
            case "plus":    return sf.newSymbol(AstLocParserSym.PLUS, location);
            case "seplist": return sf.newSymbol(AstLocParserSym.SEPLIST, location);
            case "tailsep": return sf.newSymbol(AstLocParserSym.TAILSEP, location);
            case "multi":   return sf.newSymbol(AstLocParserSym.MULTI, location);
            case "flat":    return sf.newSymbol(AstLocParserSym.FLAT, location);
            case "spread":  return sf.newSymbol(AstLocParserSym.SPREAD, location);
            case "anon":    return sf.newSymbol(AstLocParserSym.ANON, location);
            case "qanon":   return sf.newSymbol(AstLocParserSym.QANON, location);
            case "typed":   return sf.newSymbol(AstLocParserSym.TYPED, location);
            default:        return sf.newSymbol(AstLocParserSym.ID, location, word);
        }
    }
}
