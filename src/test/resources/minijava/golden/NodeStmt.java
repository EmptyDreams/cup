import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeStmt extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "stmt";
  }

  @Override
  public final String toString() {
    return "stmt" + getLocation();
  }

  public boolean hasStringConst() {
    return false;
  }

  public boolean hasStmtList() {
    return false;
  }

  public boolean hasCond() {
    return false;
  }

  public boolean hasStmt() {
    return false;
  }

  public boolean hasLhs() {
    return false;
  }

  public boolean hasRhs() {
    return false;
  }

  public boolean hasElseStmt() {
    return false;
  }

  public boolean hasExpr() {
    return false;
  }

  public NodeObject getStringConst() {
    return null;
  }

  public NodeListNodeStmt getStmtList() {
    return null;
  }

  public NodeCond getCond() {
    return null;
  }

  public NodeStmt getStmt() {
    return null;
  }

  public NodeObject getLhs() {
    return null;
  }

  public NodeExpr getRhs() {
    return null;
  }

  public NodeStmt getElseStmt() {
    return null;
  }

  public NodeExpr getExpr() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "stringConst": return hasStringConst();
      case "stmtList": return hasStmtList();
      case "cond": return hasCond();
      case "stmt": return hasStmt();
      case "lhs": return hasLhs();
      case "rhs": return hasRhs();
      case "elseStmt": return hasElseStmt();
      case "expr": return hasExpr();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "stringConst": return getStringConst();
      case "stmtList": return getStmtList();
      case "cond": return getCond();
      case "stmt": return getStmt();
      case "lhs": return getLhs();
      case "rhs": return getRhs();
      case "elseStmt": return getElseStmt();
      case "expr": return getExpr();
      default: return null;
    }
  }

  public static S9N8IPnwS buildS9N8IPnwS(
    Symbol stringConst
  ) {
    var stringConstNode = new NodeObject(stringConst.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) stringConst.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) stringConst.getLocation();
    return new S9N8IPnwS(
      stringConstNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static S5c39NvkS buildS5c39NvkS(
    Symbol stmtList
  ) {
    NodeListNodeStmt stmtListNode = null;
    if (!stmtList.isNull()) {
      stmtListNode = new NodeListNodeStmt(stmtList.<List<NodeStmt>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation());
    }
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation();
    return new S5c39NvkS(
      stmtListNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SHbm0IR1q buildSHbm0IR1q(
    Symbol cond,
    Symbol stmt
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var condNode = cond.<NodeCond>value();
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) cond.getLocation();
    var stmtNode = stmt.<NodeStmt>value();
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) stmt.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SHbm0IR1q(
      condNode,
      stmtNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SFhUafJ1b buildSFhUafJ1b(
    Symbol lhs
  ) {
    var lhsNode = new NodeObject(lhs.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation();
    return new SFhUafJ1b(
      lhsNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SyUzCIp5O buildSyUzCIp5O(
    Symbol lhs,
    Symbol rhs
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var lhsNode = new NodeObject(lhs.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation());
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation();
    var rhsNode = rhs.<NodeExpr>value();
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) rhs.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SyUzCIp5O(
      lhsNode,
      rhsNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static S0HIxPMha buildS0HIxPMha(
    Symbol cond,
    Symbol stmt,
    Symbol elseStmt
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var condNode = cond.<NodeCond>value();
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) cond.getLocation();
    var stmtNode = stmt.<NodeStmt>value();
    var elseStmtNode = elseStmt.<NodeStmt>value();
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) elseStmt.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new S0HIxPMha(
      condNode,
      stmtNode,
      elseStmtNode,
      CUP$MiniJavaParser$pos
    );
  }

  public static SYTvDvEfc buildSYTvDvEfc(
    Symbol lhs,
    Symbol stringConst
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    var lhsNode = new NodeObject(lhs.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation());
    CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lhs.getLocation();
    var stringConstNode = new NodeObject(stringConst.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) stringConst.getLocation());
    CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) stringConst.getLocation();
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SYTvDvEfc(
      lhsNode,
      stringConstNode,
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


  public static final class S9N8IPnwS extends NodeStmt {

    private final NodeObject stringConst;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S9N8IPnwS(
      NodeObject stringConst,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.stringConst = stringConst;
      this.location = location;
    }

    @Override
    public NodeObject getStringConst() {
      return stringConst;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasStringConst() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("stringConst", getStringConst());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class S5c39NvkS extends NodeStmt {

    private final NodeListNodeStmt stmtList;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S5c39NvkS(
      NodeListNodeStmt stmtList,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.stmtList = stmtList;
      this.location = location;
    }

    @Override
    public NodeListNodeStmt getStmtList() {
      return stmtList;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasStmtList() {
      return stmtList != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getStmtList() == null ? null : new AbstractMap.SimpleEntry<>("stmtList", getStmtList());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SHbm0IR1q extends NodeStmt {

    private final NodeCond cond;
    private final NodeStmt stmt;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SHbm0IR1q(
      NodeCond cond,
      NodeStmt stmt,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.cond = cond;
      this.stmt = stmt;
      this.location = location;
    }

    @Override
    public NodeCond getCond() {
      return cond;
    }

    @Override
    public NodeStmt getStmt() {
      return stmt;
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
    public boolean hasStmt() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("cond", getCond());
        case 1: return new AbstractMap.SimpleEntry<>("stmt", getStmt());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SFhUafJ1b extends NodeStmt {

    private final NodeObject lhs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SFhUafJ1b(
      NodeObject lhs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.lhs = lhs;
      this.location = location;
    }

    @Override
    public NodeObject getLhs() {
      return lhs;
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
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("lhs", getLhs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SyUzCIp5O extends NodeStmt {

    private final NodeObject lhs;
    private final NodeExpr rhs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SyUzCIp5O(
      NodeObject lhs,
      NodeExpr rhs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.lhs = lhs;
      this.rhs = rhs;
      this.location = location;
    }

    @Override
    public NodeObject getLhs() {
      return lhs;
    }

    @Override
    public NodeExpr getRhs() {
      return rhs;
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
    public boolean hasRhs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("lhs", getLhs());
        case 1: return new AbstractMap.SimpleEntry<>("rhs", getRhs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class S0HIxPMha extends NodeStmt {

    private final NodeCond cond;
    private final NodeStmt stmt;
    private final NodeStmt elseStmt;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S0HIxPMha(
      NodeCond cond,
      NodeStmt stmt,
      NodeStmt elseStmt,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.cond = cond;
      this.stmt = stmt;
      this.elseStmt = elseStmt;
      this.location = location;
    }

    @Override
    public NodeCond getCond() {
      return cond;
    }

    @Override
    public NodeStmt getStmt() {
      return stmt;
    }

    @Override
    public NodeStmt getElseStmt() {
      return elseStmt;
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
    public boolean hasStmt() {
      return true;
    }

    @Override
    public boolean hasElseStmt() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("cond", getCond());
        case 1: return new AbstractMap.SimpleEntry<>("stmt", getStmt());
        case 2: return new AbstractMap.SimpleEntry<>("elseStmt", getElseStmt());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class SYTvDvEfc extends NodeStmt {

    private final NodeObject lhs;
    private final NodeObject stringConst;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SYTvDvEfc(
      NodeObject lhs,
      NodeObject stringConst,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.lhs = lhs;
      this.stringConst = stringConst;
      this.location = location;
    }

    @Override
    public NodeObject getLhs() {
      return lhs;
    }

    @Override
    public NodeObject getStringConst() {
      return stringConst;
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
    public boolean hasStringConst() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("lhs", getLhs());
        case 1: return new AbstractMap.SimpleEntry<>("stringConst", getStringConst());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SIohrTWro extends NodeStmt {

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

}
