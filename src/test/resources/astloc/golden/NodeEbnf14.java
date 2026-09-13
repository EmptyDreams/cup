import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeEbnf14 extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "_EBNF_14";
  }

  @Override
  public final String toString() {
    return "_EBNF_14" + getLocation();
  }

  public boolean hasN() {
    return false;
  }

  public boolean hasT() {
    return false;
  }

  public NodeSitem getN() {
    return null;
  }

  public NodeTitem getT() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "n": return hasN();
      case "t": return hasT();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "n": return getN();
      case "t": return getT();
      default: return null;
    }
  }

  public static SgtZNA6HJ buildSgtZNA6HJ(
    Symbol n
  ) {
    var nNode = n.<NodeSitem>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) n.getLocation();
    return new SgtZNA6HJ(
      nNode,
      CUP$AstLocParser$pos
    );
  }

  public static SatMWwbU7 buildSatMWwbU7(
    Symbol t
  ) {
    var tNode = t.<NodeTitem>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) t.getLocation();
    return new SatMWwbU7(
      tNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SgtZNA6HJ extends NodeEbnf14 {

    private final NodeSitem n;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SgtZNA6HJ(
      NodeSitem n,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.n = n;
      this.location = location;
    }

    @Override
    public NodeSitem getN() {
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
  public static final class SatMWwbU7 extends NodeEbnf14 {

    private final NodeTitem t;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SatMWwbU7(
      NodeTitem t,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.t = t;
      this.location = location;
    }

    @Override
    public NodeTitem getT() {
      return t;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasT() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("t", getT());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
