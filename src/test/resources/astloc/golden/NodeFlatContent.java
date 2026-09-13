import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeFlatContent extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "flatContent";
  }

  @Override
  public final String toString() {
    return "flatContent" + getLocation();
  }

  public boolean hasX() {
    return false;
  }

  public boolean hasY() {
    return false;
  }

  public NodeString getX() {
    return null;
  }

  public NodeString getY() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "x": return hasX();
      case "y": return hasY();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "x": return getX();
      case "y": return getY();
      default: return null;
    }
  }

  public static Scu1iXLeh buildScu1iXLeh(
    Symbol x,
    Symbol y
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    var xNode = new NodeString(x.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) x.getLocation());
    CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) x.getLocation();
    var yNode = new NodeString(y.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) y.getLocation());
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) y.getLocation();
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new Scu1iXLeh(
      xNode,
      yNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class Scu1iXLeh extends NodeFlatContent {

    private final NodeString x;
    private final NodeString y;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  Scu1iXLeh(
      NodeString x,
      NodeString y,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.x = x;
      this.y = y;
      this.location = location;
    }

    @Override
    public NodeString getX() {
      return x;
    }

    @Override
    public NodeString getY() {
      return y;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasX() {
      return true;
    }

    @Override
    public boolean hasY() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("x", getX());
        case 1: return new AbstractMap.SimpleEntry<>("y", getY());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
