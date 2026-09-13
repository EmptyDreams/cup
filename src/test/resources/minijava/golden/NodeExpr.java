import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeExpr extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "expr";
  }

  @Override
  public final String toString() {
    return "expr" + getLocation();
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

  public boolean hasExpr() {
    return false;
  }

  public boolean hasConstant() {
    return false;
  }

  public boolean hasIdentifier() {
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

  public NodeExpr getExpr() {
    return null;
  }

  public NodeObject getConstant() {
    return null;
  }

  public NodeObject getIdentifier() {
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
      case "expr": return hasExpr();
      case "constant": return hasConstant();
      case "identifier": return hasIdentifier();
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
      case "expr": return getExpr();
      case "constant": return getConstant();
      case "identifier": return getIdentifier();
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

  public static SIohrTWro buildSIohrTWro(
    Symbol expr
  ) {
    var exprNode = expr.<NodeExpr>value();
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) expr.getLocation();
    return new SIohrTWro(
      exprNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SDOVntjSs buildSDOVntjSs(
    Symbol constant
  ) {
    var constantNode = new NodeObject(constant.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) constant.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) constant.getLocation();
    return new SDOVntjSs(
      constantNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SVQCxBP4u buildSVQCxBP4u(
    Symbol identifier
  ) {
    var identifierNode = new NodeObject(identifier.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) identifier.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) identifier.getLocation();
    return new SVQCxBP4u(
      identifierNode,
      CUP$MiniJavaParser$pos
    );
  }


  public static final class Scn0k69Xc extends NodeExpr {

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
  public static final class SIohrTWro extends NodeExpr {

    private final NodeExpr expr;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SIohrTWro(
      NodeExpr expr,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.expr = expr;
      this.location = location;
    }

    @Override
    public NodeExpr getExpr() {
      return expr;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasExpr() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("expr", getExpr());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SDOVntjSs extends NodeExpr {

    private final NodeObject constant;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SDOVntjSs(
      NodeObject constant,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.constant = constant;
      this.location = location;
    }

    @Override
    public NodeObject getConstant() {
      return constant;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasConstant() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("constant", getConstant());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SVQCxBP4u extends NodeExpr {

    private final NodeObject identifier;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SVQCxBP4u(
      NodeObject identifier,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.identifier = identifier;
      this.location = location;
    }

    @Override
    public NodeObject getIdentifier() {
      return identifier;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasIdentifier() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("identifier", getIdentifier());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
