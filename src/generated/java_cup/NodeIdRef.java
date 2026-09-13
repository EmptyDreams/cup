package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeIdRef extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "id_ref";
  }

  @Override
  public final String toString() {
    return "id_ref" + getLocation();
  }

  public boolean hasTheId() {
    return false;
  }

  public NodeString getTheId() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "the_id".equals(label) && hasTheId();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "the_id".equals(label) ? getTheId() : null;
  }

  public static SHA4oJhCM buildSHA4oJhCM(
    Symbol the_id
  ) {
    var the_idNode = new NodeString(the_id.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) the_id.getLocation());
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) the_id.getLocation();
    return new SHA4oJhCM(
      the_idNode,
      CUP$CupParser$pos
    );
  }


  public static final class SHA4oJhCM extends NodeIdRef {

    private final NodeString the_id;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SHA4oJhCM(
      NodeString the_id,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.the_id = the_id;
      this.location = location;
    }

    @Override
    public NodeString getTheId() {
      return the_id;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTheId() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("the_id", getTheId());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
