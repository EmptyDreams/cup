import java_cup.runtime.AstNode;
import java_cup.runtime.SymbolFactory;

import java.util.Iterator;
import java.util.Map;

public final class $ClassName$ extends AstNode {

    private final $type$ value;
    private final java_cup.runtime.symbol.Location location;

    public $ClassName$($type$ value, java_cup.runtime.symbol.Location location) {
        this.value = value;
        this.location = location;
    }

    public $type$ get$Type$() {
        return value;
    }

    @Override
    public java_cup.runtime.symbol.Location getLocation() {
        return location;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public AstNode getByLabel(String label) {
        return null;
    }

    @Override
    public String getNodeName(SymbolFactory factory) {
        return $nodeName$;
    }

    @Override
    protected Map.Entry<String, AstNode> getByIndex(int index) {
        $getByIndex$
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
        return $iterator$;
    }

    @Override
    public int hashCode() {
        return value + location.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        var that = ($ClassName$) o;
        return value == that.value && location.equals(that.location);
    }

    @Override
    public String toString() {
        return "$Type$(" + value + ")@" + location;
    }

}