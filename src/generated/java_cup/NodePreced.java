package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodePreced extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "preced";
  }

  @Override
  public final String toString() {
    return "preced" + getLocation();
  }

  public boolean hasR() {
    return false;
  }

  public boolean hasRefs() {
    return false;
  }

  public boolean hasN() {
    return false;
  }

  public boolean hasL() {
    return false;
  }

  public NodeObject getR() {
    return null;
  }

  public NodeListNodeSymbolId getRefs() {
    return null;
  }

  public NodeObject getN() {
    return null;
  }

  public NodeObject getL() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "r": return hasR();
      case "refs": return hasRefs();
      case "n": return hasN();
      case "l": return hasL();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "r": return getR();
      case "refs": return getRefs();
      case "n": return getN();
      case "l": return getL();
      default: return null;
    }
  }

  public static SGUmIHTa7 buildSGUmIHTa7(
    Symbol r,
    Symbol refs
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var rNode = new NodeObject(r.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) r.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) r.getLocation();
    var refsNode = new NodeListNodeSymbolId(refs.<List<NodeSymbolId>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SGUmIHTa7(
      rNode,
      refsNode,
      CUP$CupParser$pos
    );
  }

  public static SxeqI5YAV buildSxeqI5YAV(
    Symbol n,
    Symbol refs
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var nNode = new NodeObject(n.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) n.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) n.getLocation();
    var refsNode = new NodeListNodeSymbolId(refs.<List<NodeSymbolId>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SxeqI5YAV(
      nNode,
      refsNode,
      CUP$CupParser$pos
    );
  }

  public static Sfa12DIYO buildSfa12DIYO(
    Symbol l,
    Symbol refs
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var lNode = new NodeObject(l.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) l.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) l.getLocation();
    var refsNode = new NodeListNodeSymbolId(refs.<List<NodeSymbolId>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) refs.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new Sfa12DIYO(
      lNode,
      refsNode,
      CUP$CupParser$pos
    );
  }


  public static final class SGUmIHTa7 extends NodePreced {

    private final NodeObject r;
    private final NodeListNodeSymbolId refs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SGUmIHTa7(
      NodeObject r,
      NodeListNodeSymbolId refs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.r = r;
      this.refs = refs;
      this.location = location;
    }

    @Override
    public NodeObject getR() {
      return r;
    }

    @Override
    public NodeListNodeSymbolId getRefs() {
      return refs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasR() {
      return true;
    }

    @Override
    public boolean hasRefs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("r", getR());
        case 1: return new AbstractMap.SimpleEntry<>("refs", getRefs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SxeqI5YAV extends NodePreced {

    private final NodeObject n;
    private final NodeListNodeSymbolId refs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SxeqI5YAV(
      NodeObject n,
      NodeListNodeSymbolId refs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.n = n;
      this.refs = refs;
      this.location = location;
    }

    @Override
    public NodeObject getN() {
      return n;
    }

    @Override
    public NodeListNodeSymbolId getRefs() {
      return refs;
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
    public boolean hasRefs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("n", getN());
        case 1: return new AbstractMap.SimpleEntry<>("refs", getRefs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class Sfa12DIYO extends NodePreced {

    private final NodeObject l;
    private final NodeListNodeSymbolId refs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  Sfa12DIYO(
      NodeObject l,
      NodeListNodeSymbolId refs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.l = l;
      this.refs = refs;
      this.location = location;
    }

    @Override
    public NodeObject getL() {
      return l;
    }

    @Override
    public NodeListNodeSymbolId getRefs() {
      return refs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasL() {
      return true;
    }

    @Override
    public boolean hasRefs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("l", getL());
        case 1: return new AbstractMap.SimpleEntry<>("refs", getRefs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
