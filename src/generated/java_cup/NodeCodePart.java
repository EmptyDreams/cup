package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeCodePart extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "code_part";
  }

  @Override
  public final String toString() {
    return "code_part" + getLocation();
  }

  public boolean hasSc() {
    return false;
  }

  public boolean hasCode() {
    return false;
  }

  public boolean hasP() {
    return false;
  }

  public boolean hasA() {
    return false;
  }

  public boolean hasI() {
    return false;
  }

  public NodeObject getSc() {
    return null;
  }

  public NodeString getCode() {
    return null;
  }

  public NodeObject getP() {
    return null;
  }

  public NodeObject getA() {
    return null;
  }

  public NodeObject getI() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "sc": return hasSc();
      case "code": return hasCode();
      case "p": return hasP();
      case "a": return hasA();
      case "i": return hasI();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "sc": return getSc();
      case "code": return getCode();
      case "p": return getP();
      case "a": return getA();
      case "i": return getI();
      default: return null;
    }
  }

  public static SC4kdk1eM buildSC4kdk1eM(
    Symbol sc,
    Symbol code
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var scNode = new NodeObject(sc.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) sc.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) sc.getLocation();
    var codeNode = new NodeString(code.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SC4kdk1eM(
      scNode,
      codeNode,
      CUP$CupParser$pos
    );
  }

  public static SDUKf9l5I buildSDUKf9l5I(
    Symbol p,
    Symbol code
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var pNode = new NodeObject(p.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) p.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) p.getLocation();
    var codeNode = new NodeString(code.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SDUKf9l5I(
      pNode,
      codeNode,
      CUP$CupParser$pos
    );
  }

  public static SyPTwF0rI buildSyPTwF0rI(
    Symbol a,
    Symbol code
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var aNode = new NodeObject(a.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) a.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) a.getLocation();
    var codeNode = new NodeString(code.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SyPTwF0rI(
      aNode,
      codeNode,
      CUP$CupParser$pos
    );
  }

  public static SrqYp4yKA buildSrqYp4yKA(
    Symbol i,
    Symbol code
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var iNode = new NodeObject(i.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) i.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) i.getLocation();
    var codeNode = new NodeString(code.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) code.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SrqYp4yKA(
      iNode,
      codeNode,
      CUP$CupParser$pos
    );
  }


  public static final class SC4kdk1eM extends NodeCodePart {

    private final NodeObject sc;
    private final NodeString code;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SC4kdk1eM(
      NodeObject sc,
      NodeString code,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.sc = sc;
      this.code = code;
      this.location = location;
    }

    @Override
    public NodeObject getSc() {
      return sc;
    }

    @Override
    public NodeString getCode() {
      return code;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSc() {
      return true;
    }

    @Override
    public boolean hasCode() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("sc", getSc());
        case 1: return new AbstractMap.SimpleEntry<>("code", getCode());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SDUKf9l5I extends NodeCodePart {

    private final NodeObject p;
    private final NodeString code;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SDUKf9l5I(
      NodeObject p,
      NodeString code,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.p = p;
      this.code = code;
      this.location = location;
    }

    @Override
    public NodeObject getP() {
      return p;
    }

    @Override
    public NodeString getCode() {
      return code;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasP() {
      return true;
    }

    @Override
    public boolean hasCode() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("p", getP());
        case 1: return new AbstractMap.SimpleEntry<>("code", getCode());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SyPTwF0rI extends NodeCodePart {

    private final NodeObject a;
    private final NodeString code;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SyPTwF0rI(
      NodeObject a,
      NodeString code,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.a = a;
      this.code = code;
      this.location = location;
    }

    @Override
    public NodeObject getA() {
      return a;
    }

    @Override
    public NodeString getCode() {
      return code;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasA() {
      return true;
    }

    @Override
    public boolean hasCode() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("a", getA());
        case 1: return new AbstractMap.SimpleEntry<>("code", getCode());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SrqYp4yKA extends NodeCodePart {

    private final NodeObject i;
    private final NodeString code;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SrqYp4yKA(
      NodeObject i,
      NodeString code,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.i = i;
      this.code = code;
      this.location = location;
    }

    @Override
    public NodeObject getI() {
      return i;
    }

    @Override
    public NodeString getCode() {
      return code;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasI() {
      return true;
    }

    @Override
    public boolean hasCode() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("i", getI());
        case 1: return new AbstractMap.SimpleEntry<>("code", getCode());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
