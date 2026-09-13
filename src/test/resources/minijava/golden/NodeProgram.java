import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeProgram extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "program";
  }

  @Override
  public final String toString() {
    return "program" + getLocation();
  }

  public boolean hasDeclList() {
    return false;
  }

  public boolean hasStmtList() {
    return false;
  }

  public NodeListNodeDecl getDeclList() {
    return null;
  }

  public NodeListNodeStmt getStmtList() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "declList": return hasDeclList();
      case "stmtList": return hasStmtList();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "declList": return getDeclList();
      case "stmtList": return getStmtList();
      default: return null;
    }
  }

  public static SEjaGf5T0 buildSEjaGf5T0(
    Symbol declList,
    Symbol stmtList
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$MiniJavaParser$right = null;
    NodeListNodeDecl declListNode = null;
    if (!declList.isNull()) {
      declListNode = new NodeListNodeDecl(declList.<List<NodeDecl>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) declList.getLocation());
      CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) declList.getLocation();
      CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) declList.getLocation();
    }
    NodeListNodeStmt stmtListNode = null;
    if (!stmtList.isNull()) {
      stmtListNode = new NodeListNodeStmt(stmtList.<List<NodeStmt>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation());
      if (CUP$MiniJavaParser$left == null)
        CUP$MiniJavaParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation();
      CUP$MiniJavaParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation();
    }
    var CUP$MiniJavaParser$pos = (CUP$MiniJavaParser$left != null ? CUP$MiniJavaParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$MiniJavaParser$right != null ? CUP$MiniJavaParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SEjaGf5T0(
      declListNode,
      stmtListNode,
      CUP$MiniJavaParser$pos
    );
  }


  public static final class SEjaGf5T0 extends NodeProgram {

    private final NodeListNodeDecl declList;
    private final NodeListNodeStmt stmtList;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SEjaGf5T0(
      NodeListNodeDecl declList,
      NodeListNodeStmt stmtList,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.declList = declList;
      this.stmtList = stmtList;
      this.location = location;
    }

    @Override
    public NodeListNodeDecl getDeclList() {
      return declList;
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
    public boolean hasDeclList() {
      return declList != null;
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
        case 0: return getDeclList() == null ? null : new AbstractMap.SimpleEntry<>("declList", getDeclList());
        case 1: return getStmtList() == null ? null : new AbstractMap.SimpleEntry<>("stmtList", getStmtList());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }

}
