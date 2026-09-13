import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeSitem extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "sitem";
  }

  @Override
  public final String toString() {
    return "sitem" + getLocation();
  }

  public boolean hasN() {
    return false;
  }

  public NodeString getN() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "n".equals(label) && hasN();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "n".equals(label) ? getN() : null;
  }

  public static SgtZNA6HJ buildSgtZNA6HJ(
    Symbol n
  ) {
    var nNode = new NodeString(n.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) n.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) n.getLocation();
    return new SgtZNA6HJ(
      nNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SgtZNA6HJ extends NodeSitem {

    private final NodeString n;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SgtZNA6HJ(
      NodeString n,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.n = n;
      this.location = location;
    }

    @Override
    public NodeString getN() {
      return n;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasN() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("n", getN());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
