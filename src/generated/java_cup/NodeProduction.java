package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeProduction extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "production";
  }

  @Override
  public final String toString() {
    return "production" + getLocation();
  }

  public boolean hasLhs() {
    return false;
  }

  public boolean hasAlts() {
    return false;
  }

  public NodeString getLhs() {
    return null;
  }

  public NodeListNodeRhs getAlts() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "lhs": return hasLhs();
      case "alts": return hasAlts();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "lhs": return getLhs();
      case "alts": return getAlts();
      default: return null;
    }
  }

  public static SiKkWaoMM buildSiKkWaoMM(
    Symbol lhs,
    Symbol alts
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var lhsNode = new NodeString(lhs.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation();
    var altsNode = new NodeListNodeRhs(alts.<List<NodeRhs>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) alts.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) alts.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SiKkWaoMM(
      lhsNode,
      altsNode,
      CUP$CupParser$pos
    );
  }


  public static final class SiKkWaoMM extends NodeProduction {

    private final NodeString lhs;
    private final NodeListNodeRhs alts;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SiKkWaoMM(
      NodeString lhs,
      NodeListNodeRhs alts,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.lhs = lhs;
      this.alts = alts;
      this.location = location;
    }

    @Override
    public NodeString getLhs() {
      return lhs;
    }

    @Override
    public NodeListNodeRhs getAlts() {
      return alts;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLhs() {
      return true;
    }

    @Override
    public boolean hasAlts() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("lhs", getLhs());
        case 1: return new AbstractMap.SimpleEntry<>("alts", getAlts());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
