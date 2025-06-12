package java_cup.runtime.symbol.complex;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Store the location of the symbol in the source file, you can inherit this class for more precise positioning.
 *
 * @author Michael Petter, kmar
 */
public class Location {

    public static final Location EMPTY = new Location(-1, -1);

    private int line, column;

    /**
     * Copy Constructor for other ComplexSymbolFactory based Locations
     */
    public Location(Location other) {
        this(other.line, other.column);
    }

    /**
     * Location Object stores line and column
     *
     * @param line   line number
     * @param column column number
     */
    public Location(int line, int column) {
        this.line = line;
        this.column = column;
    }

    /**
     * getColumn
     *
     * @return column if known, else -1
     */
    public int getColumn() {
        return column;
    }

    /**
     * getLine
     *
     * @return line if known, else -1
     */
    public int getLine() {
        return line;
    }

    /**
     * move moves this Location by the given differences.
     */
    public void move(int lineDiff, int colDiff) {
        if (line >= 0)
            line += lineDiff;
        if (column >= 0)
            column += colDiff;
    }

    /**
     * getLine
     *
     * @return line if known, else -1
     */
    @Override
    public String toString() {
        return "line=" + getLine() + ", column=" + getColumn();
    }

    /**
     * Writes the location information directly into an XML document
     *
     * @param writer      the destination XML Document
     * @param orientation adds details about the orientation of this location as an
     *                    attribute; often used with the strings "left" or "right"
     */
    @SuppressWarnings("SpellCheckingInspection")
    public void toXML(XMLStreamWriter writer, String orientation) throws XMLStreamException {
        writer.writeStartElement("location");
        writer.writeAttribute("orientation", orientation);
        writer.writeAttribute("linenumber", line + "");
        writer.writeAttribute("columnnumber", column + "");
        writer.writeEndElement();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;
        return line == location.line && column == location.column;
    }

    @Override
    public int hashCode() {
        int result = line;
        result = 31 * result + column;
        return result;
    }

}