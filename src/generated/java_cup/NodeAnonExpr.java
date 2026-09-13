package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeAnonExpr extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "anon_expr";
  }

  @Override
  public final String toString() {
    return "anon_expr" + getLocation();
  }

  public boolean hasLp() {
    return false;
  }

  public boolean hasType() {
    return false;
  }

  public boolean hasBranches() {
    return false;
  }

  public NodeObject getLp() {
    return null;
  }

  public NodeString getType() {
    return null;
  }

  public NodeListNodeRhs getBranches() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "lp": return hasLp();
      case "type": return hasType();
      case "branches": return hasBranches();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "lp": return getLp();
      case "type": return getType();
      case "branches": return getBranches();
      default: return null;
    }
  }

  public static SG3r5zv2S buildSG3r5zv2S(
    Symbol lp,
    Symbol type,
    Symbol branches
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var lpNode = new NodeObject(lp.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) lp.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lp.getLocation();
    var typeNode = new NodeString(type.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) type.getLocation());
    var branchesNode = new NodeListNodeRhs(branches.<List<NodeRhs>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) branches.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) branches.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SG3r5zv2S(
      lpNode,
      typeNode,
      branchesNode,
      CUP$CupParser$pos
    );
  }


  public static final class SG3r5zv2S extends NodeAnonExpr {

    private final NodeObject lp;
    private final NodeString type;
    private final NodeListNodeRhs branches;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SG3r5zv2S(
      NodeObject lp,
      NodeString type,
      NodeListNodeRhs branches,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.lp = lp;
      this.type = type;
      this.branches = branches;
      this.location = location;
    }

    @Override
    public NodeObject getLp() {
      return lp;
    }

    @Override
    public NodeString getType() {
      return type;
    }

    @Override
    public NodeListNodeRhs getBranches() {
      return branches;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLp() {
      return true;
    }

    @Override
    public boolean hasType() {
      return true;
    }

    @Override
    public boolean hasBranches() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("lp", getLp());
        case 1: return new AbstractMap.SimpleEntry<>("type", getType());
        case 2: return new AbstractMap.SimpleEntry<>("branches", getBranches());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }

}
