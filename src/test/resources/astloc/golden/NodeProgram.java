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

  public boolean hasStmtList() {
    return false;
  }

  public NodeListNodeStmt getStmtList() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "stmtList".equals(label) && hasStmtList();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "stmtList".equals(label) ? getStmtList() : null;
  }

  public static S5c39NvkS buildS5c39NvkS(
    Symbol stmtList
  ) {
    NodeListNodeStmt stmtListNode = null;
    if (!stmtList.isNull()) {
      stmtListNode = new NodeListNodeStmt(stmtList.<List<NodeStmt>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation());
    }
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) stmtList.getLocation();
    return new S5c39NvkS(
      stmtListNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class S5c39NvkS extends NodeProgram {

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

}
