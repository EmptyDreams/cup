import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeCond extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "cond";
  }

  @Override
  public final String toString() {
    return "cond" + getLocation();
  }

  public boolean hasLeftExpr() {
    return false;
  }

  public boolean hasOp() {
    return false;
  }

  public boolean hasRightExpr() {
    return false;
  }

  public boolean hasCond() {
    return false;
  }

  public boolean hasBoolConst() {
    return false;
  }

  public boolean hasLeftCond() {
    return false;
  }

  public boolean hasRightCond() {
    return false;
  }

  public NodeExpr getLeftExpr() {
    return null;
  }

  public NodeObject getOp() {
    return null;
  }

  public NodeExpr getRightExpr() {
    return null;
  }

  public NodeCond getCond() {
    return null;
  }

  public NodeObject getBoolConst() {
    return null;
  }

  public NodeCond getLeftCond() {
    return null;
  }

  public NodeCond getRightCond() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "leftExpr": return hasLeftExpr();
      case "op": return hasOp();
      case "rightExpr": return hasRightExpr();
      case "cond": return hasCond();
      case "boolConst": return hasBoolConst();
      case "leftCond": return hasLeftCond();
      case "rightCond": return hasRightCond();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "leftExpr": return getLeftExpr();
      case "op": return getOp();
      case "rightExpr": return getRightExpr();
      case "cond": return getCond();
      case "boolConst": return getBoolConst();
      case "leftCond": return getLeftCond();
      case "rightCond": return getRightCond();
      default: return null;
    }
  }

  public static Scn0k69Xc buildScn0k69Xc(
    Symbol leftExpr,
    Symbol op,
    Symbol rightExpr
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var leftExprNode = leftExpr.<NodeExpr>value();
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) leftExpr.getLocation();
    var opNode = new NodeObject(op.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) op.getLocation());
    var rightExprNode = rightExpr.<NodeExpr>value();
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) rightExpr.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new Scn0k69Xc(
      leftExprNode,
      opNode,
      rightExprNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static S1ZIx8Nyq buildS1ZIx8Nyq(
    Symbol cond
  ) {
    var condNode = cond.<NodeCond>value();
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) cond.getLocation();
    return new S1ZIx8Nyq(
      condNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static S22HtvRLC buildS22HtvRLC(
    Symbol boolConst
  ) {
    var boolConstNode = new NodeObject(boolConst.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) boolConst.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) boolConst.getLocation();
    return new S22HtvRLC(
      boolConstNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SjMNsve7Y buildSjMNsve7Y(
    Symbol leftCond,
    Symbol op,
    Symbol rightCond
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var leftCondNode = leftCond.<NodeCond>value();
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) leftCond.getLocation();
    var opNode = new NodeObject(op.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) op.getLocation());
    var rightCondNode = rightCond.<NodeCond>value();
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) rightCond.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SjMNsve7Y(
      leftCondNode,
      opNode,
      rightCondNode,
      CUP$MiniJavaParser$pos
    );
  }


  public static final class Scn0k69Xc extends NodeCond {

    private final NodeExpr leftExpr;
    private final NodeObject op;
    private final NodeExpr rightExpr;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  Scn0k69Xc(
      NodeExpr leftExpr,
      NodeObject op,
      NodeExpr rightExpr,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.leftExpr = leftExpr;
      this.op = op;
      this.rightExpr = rightExpr;
      this.location = location;
    }

    @Override
    public NodeExpr getLeftExpr() {
      return leftExpr;
    }

    @Override
    public NodeObject getOp() {
      return op;
    }

    @Override
    public NodeExpr getRightExpr() {
      return rightExpr;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLeftExpr() {
      return true;
    }

    @Override
    public boolean hasOp() {
      return true;
    }

    @Override
    public boolean hasRightExpr() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("leftExpr", getLeftExpr());
        case 1: return new AbstractMap.SimpleEntry<>("op", getOp());
        case 2: return new AbstractMap.SimpleEntry<>("rightExpr", getRightExpr());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class S1ZIx8Nyq extends NodeCond {

    private final NodeCond cond;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S1ZIx8Nyq(
      NodeCond cond,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.cond = cond;
      this.location = location;
    }

    @Override
    public NodeCond getCond() {
      return cond;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasCond() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("cond", getCond());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class S22HtvRLC extends NodeCond {

    private final NodeObject boolConst;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S22HtvRLC(
      NodeObject boolConst,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.boolConst = boolConst;
      this.location = location;
    }

    @Override
    public NodeObject getBoolConst() {
      return boolConst;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasBoolConst() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("boolConst", getBoolConst());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SjMNsve7Y extends NodeCond {

    private final NodeCond leftCond;
    private final NodeObject op;
    private final NodeCond rightCond;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SjMNsve7Y(
      NodeCond leftCond,
      NodeObject op,
      NodeCond rightCond,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.leftCond = leftCond;
      this.op = op;
      this.rightCond = rightCond;
      this.location = location;
    }

    @Override
    public NodeCond getLeftCond() {
      return leftCond;
    }

    @Override
    public NodeObject getOp() {
      return op;
    }

    @Override
    public NodeCond getRightCond() {
      return rightCond;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLeftCond() {
      return true;
    }

    @Override
    public boolean hasOp() {
      return true;
    }

    @Override
    public boolean hasRightCond() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("leftCond", getLeftCond());
        case 1: return new AbstractMap.SimpleEntry<>("op", getOp());
        case 2: return new AbstractMap.SimpleEntry<>("rightCond", getRightCond());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }

}
