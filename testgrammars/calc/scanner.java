import java_cup.runtime.*;
import java_cup.runtime.symbol.complex.ComplexSymbolFactory;
import static java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;

class Main {

    public static void main(String[] argv) throws Exception {
        System.out.println("Please type your arithmethic expression:");
        SymbolFactory sf = new ComplexSymbolFactory(CalcSymbols.TERMINAL_NAMES, null);
        CalcParser p = new CalcParser(new scanner(sf), sf);
        p.parse();
    }

}

public class scanner implements Scanner {

    private SymbolFactory sf;

    public scanner(SymbolFactory sf) {
        this.sf = sf;
    }

    /* single lookahead character */
    protected static int next_char;

    /* advance input by one character */
    protected void advance() throws java.io.IOException {
        next_char = System.in.read();
    }

    /* initialize the scanner */
    public void init() throws java.io.IOException {
        advance();
    }

    /* recognize and return the next complete token */
    @Override
    public Symbol next_token() throws java.io.IOException {
        for (; ; ) {
            switch (next_char) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    /* parse a decimal integer */
                    int i_val = 0;
                    do {
                        i_val = i_val * 10 + (next_char - '0');
                        advance();
                    } while (next_char >= '0' && next_char <= '9');
                    return sf.newSymbol(CalcSymbols.NUMBER, NO_LOCATION, i_val);

                case ';':
                    advance();
                    return sf.newSymbol(CalcSymbols.SEMI, NO_LOCATION);
                case '+':
                    advance();
                    return sf.newSymbol(CalcSymbols.PLUS, NO_LOCATION);
                case '-':
                    advance();
                    return sf.newSymbol(CalcSymbols.MINUS, NO_LOCATION);
                case '*':
                    advance();
                    return sf.newSymbol(CalcSymbols.TIMES, NO_LOCATION);
                case '(':
                    advance();
                    return sf.newSymbol(CalcSymbols.LPAREN, NO_LOCATION);
                case ')':
                    advance();
                    return sf.newSymbol(CalcSymbols.RPAREN, NO_LOCATION);

                case -1:
                    return sf.newSymbol(CalcSymbols.EOF, NO_LOCATION);

                default:
                    /* in this simple scanner we just ignore everything else */
                    advance();
                    break;
            }
        }
    }

};