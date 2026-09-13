package java_cup;

import java_cup.runtime.AstNode;
import java_cup.runtime.SymbolFactory;

import java.util.*;

public final class NodeString extends AstNode {

    private final String value;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public NodeString(String value, java_cup.runtime.symbol.complex.ComplexLocation location) {
        this.value = value;
        this.location = location;
    }

    public String getValue() {
        return value;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
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
        return true;
    }

    @Override
    public String getNodeName() {
        return "String";
    }

    @Override
    protected Map.Entry<String, AstNode> getByIndex(int index) {
        return super.getByIndex(index);
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
        return super.iterator();
    }

    @Override
    public String toString() {
        return "String" + '(' + value + ')' + location;
    }

}
