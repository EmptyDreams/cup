import java_cup.runtime.AstNode;
import java_cup.runtime.SymbolFactory;

import java.util.*;

public final class $ClassName$ extends AstNode {

    private final $type$ value;
    private final java_cup.runtime.symbol.Location location;

    public $ClassName$($type$ value, java_cup.runtime.symbol.Location location) {
        this.value = value;
        this.location = location;
    }

    public $type$ getValue() {
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
    public boolean isTerminal() {
        return $terminal$;
    }

    @Override
    public String getNodeName() {
        return "$nodeName$";
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
    public String toString() {
        return "$nodeName$" + $value$ + location;
    }

}