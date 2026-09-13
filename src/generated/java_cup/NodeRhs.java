package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeRhs extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "rhs";
  }

  @Override
  public final String toString() {
    return "rhs" + getLocation();
  }

  public boolean hasParts() {
    return false;
  }

  public boolean hasPrec() {
    return false;
  }

  public boolean hasNamer() {
    return false;
  }

  public NodeListNodeProdPart getParts() {
    return null;
  }

  public NodeSymbolId getPrec() {
    return null;
  }

  public NodeString getNamer() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "parts": return hasParts();
      case "prec": return hasPrec();
      case "namer": return hasNamer();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "parts": return getParts();
      case "prec": return getPrec();
      case "namer": return getNamer();
      default: return null;
    }
  }

  public static SAAikLpo2 buildSAAikLpo2(
    Symbol parts
  ) {
    NodeListNodeProdPart partsNode = null;
    if (!parts.isNull()) {
      partsNode = new NodeListNodeProdPart(parts.<List<NodeProdPart>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation());
    }
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation();
    return new SAAikLpo2(
      partsNode,
      CUP$CupParser$pos
    );
  }

  public static Sd4cptMn7 buildSd4cptMn7(
    Symbol parts,
    Symbol prec,
    Symbol namer
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    NodeListNodeProdPart partsNode = null;
    if (!parts.isNull()) {
      partsNode = new NodeListNodeProdPart(parts.<List<NodeProdPart>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation());
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation();
    }
    var precNode = prec.<NodeSymbolId>value();
    if (CUP$CupParser$left == null)
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) prec.getLocation();
    var namerNode = new NodeString(namer.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) namer.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) namer.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new Sd4cptMn7(
      partsNode,
      precNode,
      namerNode,
      CUP$CupParser$pos
    );
  }

  public static Sv8zucSg0 buildSv8zucSg0(
    Symbol parts,
    Symbol namer
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    NodeListNodeProdPart partsNode = null;
    if (!parts.isNull()) {
      partsNode = new NodeListNodeProdPart(parts.<List<NodeProdPart>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation());
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation();
    }
    var namerNode = new NodeString(namer.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) namer.getLocation());
    if (CUP$CupParser$left == null)
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) namer.getLocation();
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) namer.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new Sv8zucSg0(
      partsNode,
      namerNode,
      CUP$CupParser$pos
    );
  }

  public static SCTU3YRlq buildSCTU3YRlq(
    Symbol parts,
    Symbol prec
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    NodeListNodeProdPart partsNode = null;
    if (!parts.isNull()) {
      partsNode = new NodeListNodeProdPart(parts.<List<NodeProdPart>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation());
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) parts.getLocation();
    }
    var precNode = prec.<NodeSymbolId>value();
    if (CUP$CupParser$left == null)
      CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) prec.getLocation();
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) prec.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SCTU3YRlq(
      partsNode,
      precNode,
      CUP$CupParser$pos
    );
  }


  public static final class SAAikLpo2 extends NodeRhs {

    private final NodeListNodeProdPart parts;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SAAikLpo2(
      NodeListNodeProdPart parts,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.parts = parts;
      this.location = location;
    }

    @Override
    public NodeListNodeProdPart getParts() {
      return parts;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasParts() {
      return parts != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getParts() == null ? null : new AbstractMap.SimpleEntry<>("parts", getParts());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class Sd4cptMn7 extends NodeRhs {

    private final NodeListNodeProdPart parts;
    private final NodeSymbolId prec;
    private final NodeString namer;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  Sd4cptMn7(
      NodeListNodeProdPart parts,
      NodeSymbolId prec,
      NodeString namer,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.parts = parts;
      this.prec = prec;
      this.namer = namer;
      this.location = location;
    }

    @Override
    public NodeListNodeProdPart getParts() {
      return parts;
    }

    @Override
    public NodeSymbolId getPrec() {
      return prec;
    }

    @Override
    public NodeString getNamer() {
      return namer;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasParts() {
      return parts != null;
    }

    @Override
    public boolean hasPrec() {
      return true;
    }

    @Override
    public boolean hasNamer() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getParts() == null ? null : new AbstractMap.SimpleEntry<>("parts", getParts());
        case 1: return new AbstractMap.SimpleEntry<>("prec", getPrec());
        case 2: return new AbstractMap.SimpleEntry<>("namer", getNamer());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class Sv8zucSg0 extends NodeRhs {

    private final NodeListNodeProdPart parts;
    private final NodeString namer;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  Sv8zucSg0(
      NodeListNodeProdPart parts,
      NodeString namer,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.parts = parts;
      this.namer = namer;
      this.location = location;
    }

    @Override
    public NodeListNodeProdPart getParts() {
      return parts;
    }

    @Override
    public NodeString getNamer() {
      return namer;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasParts() {
      return parts != null;
    }

    @Override
    public boolean hasNamer() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getParts() == null ? null : new AbstractMap.SimpleEntry<>("parts", getParts());
        case 1: return new AbstractMap.SimpleEntry<>("namer", getNamer());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SCTU3YRlq extends NodeRhs {

    private final NodeListNodeProdPart parts;
    private final NodeSymbolId prec;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SCTU3YRlq(
      NodeListNodeProdPart parts,
      NodeSymbolId prec,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.parts = parts;
      this.prec = prec;
      this.location = location;
    }

    @Override
    public NodeListNodeProdPart getParts() {
      return parts;
    }

    @Override
    public NodeSymbolId getPrec() {
      return prec;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasParts() {
      return parts != null;
    }

    @Override
    public boolean hasPrec() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getParts() == null ? null : new AbstractMap.SimpleEntry<>("parts", getParts());
        case 1: return new AbstractMap.SimpleEntry<>("prec", getPrec());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
