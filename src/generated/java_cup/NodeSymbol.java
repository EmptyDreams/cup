package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeSymbol extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "symbol";
  }

  @Override
  public final String toString() {
    return "symbol" + getLocation();
  }

  public boolean hasNtkw() {
    return false;
  }

  public boolean hasTy() {
    return false;
  }

  public boolean hasNames() {
    return false;
  }

  public boolean hasTkw() {
    return false;
  }

  public NodeString getNtkw() {
    return null;
  }

  public NodeString getTy() {
    return null;
  }

  public NodeListNodeIdRef getNames() {
    return null;
  }

  public NodeObject getTkw() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "ntkw": return hasNtkw();
      case "ty": return hasTy();
      case "names": return hasNames();
      case "tkw": return hasTkw();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "ntkw": return getNtkw();
      case "ty": return getTy();
      case "names": return getNames();
      case "tkw": return getTkw();
      default: return null;
    }
  }

  public static SGnxuabgE buildSGnxuabgE(
    Symbol ntkw,
    Symbol ty,
    Symbol names
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var ntkwNode = new NodeString(ntkw.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) ntkw.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ntkw.getLocation();
    var tyNode = new NodeString(ty.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) ty.getLocation());
    var namesNode = new NodeListNodeIdRef(names.<List<NodeIdRef>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SGnxuabgE(
      ntkwNode,
      tyNode,
      namesNode,
      CUP$CupParser$pos
    );
  }

  public static SnmZxdqDZ buildSnmZxdqDZ(
    Symbol tkw,
    Symbol ty,
    Symbol names
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var tkwNode = new NodeObject(tkw.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) tkw.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) tkw.getLocation();
    var tyNode = new NodeString(ty.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) ty.getLocation());
    var namesNode = new NodeListNodeIdRef(names.<List<NodeIdRef>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SnmZxdqDZ(
      tkwNode,
      tyNode,
      namesNode,
      CUP$CupParser$pos
    );
  }

  public static SdbyHXVBK buildSdbyHXVBK(
    Symbol tkw,
    Symbol names
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var tkwNode = new NodeObject(tkw.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) tkw.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) tkw.getLocation();
    var namesNode = new NodeListNodeIdRef(names.<List<NodeIdRef>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SdbyHXVBK(
      tkwNode,
      namesNode,
      CUP$CupParser$pos
    );
  }

  public static SAoojkyc7 buildSAoojkyc7(
    Symbol ntkw,
    Symbol names
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var ntkwNode = new NodeString(ntkw.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) ntkw.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ntkw.getLocation();
    var namesNode = new NodeListNodeIdRef(names.<List<NodeIdRef>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) names.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SAoojkyc7(
      ntkwNode,
      namesNode,
      CUP$CupParser$pos
    );
  }


  public static final class SGnxuabgE extends NodeSymbol {

    private final NodeString ntkw;
    private final NodeString ty;
    private final NodeListNodeIdRef names;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SGnxuabgE(
      NodeString ntkw,
      NodeString ty,
      NodeListNodeIdRef names,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ntkw = ntkw;
      this.ty = ty;
      this.names = names;
      this.location = location;
    }

    @Override
    public NodeString getNtkw() {
      return ntkw;
    }

    @Override
    public NodeString getTy() {
      return ty;
    }

    @Override
    public NodeListNodeIdRef getNames() {
      return names;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasNtkw() {
      return true;
    }

    @Override
    public boolean hasTy() {
      return true;
    }

    @Override
    public boolean hasNames() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("ntkw", getNtkw());
        case 1: return new AbstractMap.SimpleEntry<>("ty", getTy());
        case 2: return new AbstractMap.SimpleEntry<>("names", getNames());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class SnmZxdqDZ extends NodeSymbol {

    private final NodeObject tkw;
    private final NodeString ty;
    private final NodeListNodeIdRef names;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SnmZxdqDZ(
      NodeObject tkw,
      NodeString ty,
      NodeListNodeIdRef names,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.tkw = tkw;
      this.ty = ty;
      this.names = names;
      this.location = location;
    }

    @Override
    public NodeObject getTkw() {
      return tkw;
    }

    @Override
    public NodeString getTy() {
      return ty;
    }

    @Override
    public NodeListNodeIdRef getNames() {
      return names;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTkw() {
      return true;
    }

    @Override
    public boolean hasTy() {
      return true;
    }

    @Override
    public boolean hasNames() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("tkw", getTkw());
        case 1: return new AbstractMap.SimpleEntry<>("ty", getTy());
        case 2: return new AbstractMap.SimpleEntry<>("names", getNames());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class SdbyHXVBK extends NodeSymbol {

    private final NodeObject tkw;
    private final NodeListNodeIdRef names;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SdbyHXVBK(
      NodeObject tkw,
      NodeListNodeIdRef names,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.tkw = tkw;
      this.names = names;
      this.location = location;
    }

    @Override
    public NodeObject getTkw() {
      return tkw;
    }

    @Override
    public NodeListNodeIdRef getNames() {
      return names;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTkw() {
      return true;
    }

    @Override
    public boolean hasNames() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("tkw", getTkw());
        case 1: return new AbstractMap.SimpleEntry<>("names", getNames());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SAoojkyc7 extends NodeSymbol {

    private final NodeString ntkw;
    private final NodeListNodeIdRef names;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SAoojkyc7(
      NodeString ntkw,
      NodeListNodeIdRef names,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ntkw = ntkw;
      this.names = names;
      this.location = location;
    }

    @Override
    public NodeString getNtkw() {
      return ntkw;
    }

    @Override
    public NodeListNodeIdRef getNames() {
      return names;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasNtkw() {
      return true;
    }

    @Override
    public boolean hasNames() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("ntkw", getNtkw());
        case 1: return new AbstractMap.SimpleEntry<>("names", getNames());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
