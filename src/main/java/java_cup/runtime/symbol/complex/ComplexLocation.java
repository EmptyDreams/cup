package java_cup.runtime.symbol.complex;

import java_cup.runtime.symbol.Location;

/**
 * Store the location of the symbol in the source file, you can inherit this class for more precise positioning.
 *
 * @author Michael Petter, kmar
 */
public class ComplexLocation implements Location {

    public static final ComplexLocation NO_LOCATION = new ComplexLocation(0, 0, 0, 0);

    /**
     * Creates a {@code ComplexLocation} representing a half-open interval:
     * the start position is <em>inclusive</em>, and the end position is <em>exclusive</em>.
     *
     * <p>The location includes all characters from
     * {@code (startLine, startColumn)} up to, but not including,
     * {@code (endLine, endColumn)}.
     *
     * <p><b>Coordinate convention:</b>
     * For real source code, line and column numbers are <b>1-based</b>
     * (i.e., the first character in a file is at line&nbsp;1, column&nbsp;1).
     * Zero or negative values are reserved for synthetic or compiler-generated code
     * and should not be used to represent actual source positions.
     *
     * <p>This method performs <em>no validation</em> on the input coordinates.
     * It is intended for use cases where the caller controls the semantics of the coordinates,
     * such as external tools working with half-open intervals or internal compiler logic
     * that may use non-positive values for synthetic nodes.
     *
     * @param startLine    the starting line number (inclusive); ≥1 for real source code
     * @param startColumn  the starting column number (inclusive); ≥1 for real source code
     * @param endLine      the ending line number (exclusive); ≥1 for real source code
     * @param endColumn    the ending column number (exclusive); ≥1 for real source code
     * @return a new {@code ComplexLocation} with the given half-open boundaries
     */
    public static ComplexLocation of(int startLine, int startColumn, int endLine, int endColumn) {
        return new ComplexLocation(startLine, startColumn, endLine, endColumn);
    }

    /**
     * Creates a {@code ComplexLocation} from an <em>inclusive</em> end position for real source code.
     *
     * <p>The resulting location includes all characters from
     * {@code (startLine, startColumn)} through
     * {@code (endLineInclude, endColumnInclude)}, inclusive.
     * Internally, this is stored as a half-open interval by incrementing the end coordinates.
     *
     * <p><b>Coordinate convention:</b> Line and column numbers are <b>1-based</b>.
     * Values less than 1 (zero or negative) are reserved for synthetic/compiler-generated code
     * and are <em>not allowed</em> in this method.
     *
     * <p>Example: {@code ofInclusive(5, 3, 5, 3)} represents exactly one character
     * at line 5, column 3 (the first character on that line has column 1).
     *
     * @param startLine          the starting line number (inclusive), must be ≥ 1
     * @param startColumn        the starting column number (inclusive), must be ≥ 1
     * @param endLineInclude     the ending line number (inclusive), must be ≥ 1
     * @param endColumnInclude   the ending column number (inclusive), must be ≥ 1
     * @return a new {@code ComplexLocation} covering the closed interval for real source code
     * @throws IllegalArgumentException if any coordinate is less than 1
     */
    @SuppressWarnings("unused")
    public static ComplexLocation ofInclusive(
        int startLine, int startColumn, int endLineInclude, int endColumnInclude
    ) {
        if (startLine < 1 || startColumn < 1 || endLineInclude < 1 || endColumnInclude < 1) {
            throw new IllegalArgumentException(
                "Location coordinates must be positive (>=1), but got: " +
                    startLine + ":" + startColumn + " - " + endLineInclude + ":" + endColumnInclude
            );
        }
        return new ComplexLocation(startLine, startColumn, endLineInclude + 1, endColumnInclude + 1);
    }

    private final int startLine, startColumn;
    private final int endLine, endColumn;

    private ComplexLocation(int startLine, int startColumn, int endLine, int endColumn) {
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    @Override
    public ComplexLocation span(Location o) {
        if (o.isNoLocation()) {
            return this;
        }
        var other = (ComplexLocation) o;
        int newStartLine = Math.min(startLine, other.getStartLine());
        int newStartColumn = Math.min(startColumn, other.getStartColumn());
        int newEndLine = Math.max(endLine, other.getEndLine());
        int newEndColumn = Math.max(endColumn, other.getEndColumn());
        return of(newStartLine, newStartColumn, newEndLine, newEndColumn);
    }

    @Override
    public boolean isEmpty() {
        return startLine == endLine && startColumn == endColumn;
    }

    @Override
    public boolean isNoLocation() {
        return this == NO_LOCATION;
    }

    @Override
    public boolean isSynthetic() {
        return startLine < 0;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public int getEndColumn() {
        return endColumn;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[" + startLine + ":" + startColumn + "]";
        return "[" + startLine + ":" + startColumn + " - " + endLine + ":" + endColumn + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ComplexLocation that = (ComplexLocation) o;
        return startLine == that.startLine &&
            startColumn == that.startColumn &&
            endLine == that.endLine &&
            endColumn == that.endColumn;
    }

    @Override
    public int hashCode() {
        int result = startLine;
        result = 31 * result + startColumn;
        result = 31 * result + endLine;
        result = 31 * result + endColumn;
        return result;
    }

}