package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeImportSpec extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "import_spec";
  }

  @Override
  public final String toString() {
    return "import_spec" + getLocation();
  }

  public boolean hasTarget() {
    return false;
  }

  public boolean hasSt() {
    return false;
  }

  public NodeString getTarget() {
    return null;
  }

  public NodeObject getSt() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "target": return hasTarget();
      case "st": return hasSt();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "target": return getTarget();
      case "st": return getSt();
      default: return null;
    }
  }

  public static STSaJNIJy buildSTSaJNIJy(
    Symbol target
  ) {
    var targetNode = new NodeString(target.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) target.getLocation());
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) target.getLocation();
    return new STSaJNIJy(
      targetNode,
      CUP$CupParser$pos
    );
  }

  public static S8TEhbwzn buildS8TEhbwzn(
    Symbol st,
    Symbol target
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var stNode = new NodeObject(st.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) st.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) st.getLocation();
    var targetNode = new NodeString(target.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) target.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) target.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new S8TEhbwzn(
      stNode,
      targetNode,
      CUP$CupParser$pos
    );
  }


  public static final class STSaJNIJy extends NodeImportSpec {

    private final NodeString target;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  STSaJNIJy(
      NodeString target,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.target = target;
      this.location = location;
    }

    @Override
    public NodeString getTarget() {
      return target;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTarget() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("target", getTarget());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class S8TEhbwzn extends NodeImportSpec {

    private final NodeObject st;
    private final NodeString target;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S8TEhbwzn(
      NodeObject st,
      NodeString target,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.st = st;
      this.target = target;
      this.location = location;
    }

    @Override
    public NodeObject getSt() {
      return st;
    }

    @Override
    public NodeString getTarget() {
      return target;
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
    public boolean hasTarget() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("st", getSt());
        case 1: return new AbstractMap.SimpleEntry<>("target", getTarget());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
