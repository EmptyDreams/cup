package java_cup;

import java_cup.runtime.AstNode;
import java_cup.runtime.SymbolFactory;

import java.util.*;

public final class NodeListNodeProdPart extends AstNode {

    private final List<NodeProdPart> value;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public NodeListNodeProdPart(List<NodeProdPart> value, java_cup.runtime.symbol.complex.ComplexLocation location) {
        this.value = value;
        this.location = location;
    }

    public List<NodeProdPart> getValue() {
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
        return false;
    }

    @Override
    public String getNodeName() {
        return "List<prod_part>";
    }

    @Override
    protected Map.Entry<String, AstNode> getByIndex(int index) {
        return new AbstractMap.SimpleEntry<>(String.valueOf(index), value.get(index));
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
        return new NodeListIterator(value);
    }

    @Override
    public String toString() {
        return "List<prod_part>" + '{' + value.size() + '}' + location;
    }

}
