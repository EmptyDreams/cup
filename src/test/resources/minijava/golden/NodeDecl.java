import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeDecl extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "decl";
  }

  @Override
  public final String toString() {
    return "decl" + getLocation();
  }

  public boolean hasIdList() {
    return false;
  }

  public NodeListNodeObject getIdList() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    return "idList".equals(label) && hasIdList();
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    return "idList".equals(label) ? getIdList() : null;
  }

  public static S84QJX3Z9 buildS84QJX3Z9(
    Symbol idList
  ) {
    var idListNode = new NodeListNodeObject(idList.<List<NodeObject>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) idList.getLocation());
    var CUP$MiniJavaParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) idList.getLocation();
    return new S84QJX3Z9(
      idListNode,
      CUP$MiniJavaParser$pos
    );
  }


  public static final class S84QJX3Z9 extends NodeDecl {

    private final NodeListNodeObject idList;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S84QJX3Z9(
      NodeListNodeObject idList,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.idList = idList;
      this.location = location;
    }

    @Override
    public NodeListNodeObject getIdList() {
      return idList;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasIdList() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("idList", getIdList());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
