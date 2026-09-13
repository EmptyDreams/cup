package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeSymbolId extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "symbol_id";
  }

  @Override
  public final String toString() {
    return "symbol_id" + getLocation();
  }

  public boolean hasTheId() {
    return false;
  }

  public boolean hasAnon() {
    return false;
  }

  public NodeString getTheId() {
    return null;
  }

  public NodeAnonExpr getAnon() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "the_id": return hasTheId();
      case "anon": return hasAnon();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "the_id": return getTheId();
      case "anon": return getAnon();
      default: return null;
    }
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

  public static S8ywhTF8L buildS8ywhTF8L(
    Symbol anon
  ) {
    var anonNode = anon.<NodeAnonExpr>value();
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) anon.getLocation();
    return new S8ywhTF8L(
      anonNode,
      CUP$CupParser$pos
    );
  }


  public static final class SHA4oJhCM extends NodeSymbolId {

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
  public static final class S8ywhTF8L extends NodeSymbolId {

    private final NodeAnonExpr anon;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S8ywhTF8L(
      NodeAnonExpr anon,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.anon = anon;
      this.location = location;
    }

    @Override
    public NodeAnonExpr getAnon() {
      return anon;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasAnon() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("anon", getAnon());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
