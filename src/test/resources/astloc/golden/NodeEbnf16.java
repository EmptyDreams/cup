import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeEbnf16 extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "_EBNF_16";
  }

  @Override
  public final String toString() {
    return "_EBNF_16" + getLocation();
  }

  public boolean hasS() {
    return false;
  }

  public NodeString getS() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "s".equals(label) && hasS();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "s".equals(label) ? getS() : null;
  }

  public static SZhGODnF3 buildSZhGODnF3(
    Symbol s
  ) {
    var sNode = new NodeString(s.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) s.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) s.getLocation();
    return new SZhGODnF3(
      sNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SZhGODnF3 extends NodeEbnf16 {

    private final NodeString s;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SZhGODnF3(
      NodeString s,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.s = s;
      this.location = location;
    }

    @Override
    public NodeString getS() {
      return s;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasS() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("s", getS());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
