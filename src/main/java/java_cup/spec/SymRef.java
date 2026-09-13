package java_cup.spec;

/**
 * A reference to a grammar symbol: a plain name ({@link NamedRefNode}) or an
 * anonymous expression ({@link AnonExprNode}).
 *
 * <p>Every {@code symbol_id} consumer position -- a part of a right-hand side,
 * a quantifier separator, a {@code %prec} target, a precedence declaration
 * entry -- accepts both shapes, so all of them are typed as SymRef and lowered
 * at their point of use.</p>
 */
public abstract class SymRef {
}
