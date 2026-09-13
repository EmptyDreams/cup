import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeItem extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "item";
  }

  @Override
  public final String toString() {
    return "item" + getLocation();
  }

  public boolean hasId() {
    return false;
  }

  public NodeString getId() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "id".equals(label) && hasId();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "id".equals(label) ? getId() : null;
  }

  public static SAZktGFKL buildSAZktGFKL(
    Symbol id
  ) {
    var idNode = new NodeString(id.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) id.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) id.getLocation();
    return new SAZktGFKL(
      idNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SAZktGFKL extends NodeItem {

    private final NodeString id;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SAZktGFKL(
      NodeString id,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.id = id;
      this.location = location;
    }

    @Override
    public NodeString getId() {
      return id;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasId() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("id", getId());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
