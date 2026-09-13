import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeTitem extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "titem";
  }

  @Override
  public final String toString() {
    return "titem" + getLocation();
  }

  public boolean hasSt() {
    return false;
  }

  public NodeString getSt() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "st".equals(label) && hasSt();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "st".equals(label) ? getSt() : null;
  }

  public static SgUCKUXxj buildSgUCKUXxj(
    Symbol st
  ) {
    var stNode = new NodeString(st.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) st.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) st.getLocation();
    return new SgUCKUXxj(
      stNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SgUCKUXxj extends NodeTitem {

    private final NodeString st;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SgUCKUXxj(
      NodeString st,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.st = st;
      this.location = location;
    }

    @Override
    public NodeString getSt() {
      return st;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSt() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("st", getSt());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
