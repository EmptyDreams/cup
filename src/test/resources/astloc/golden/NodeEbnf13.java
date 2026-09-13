import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeEbnf13 extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "_EBNF_13";
  }

  @Override
  public final String toString() {
    return "_EBNF_13" + getLocation();
  }

  public boolean hasFirst() {
    return false;
  }

  public boolean hasSecond() {
    return false;
  }

  public boolean hasOne() {
    return false;
  }

  public NodeSitem getFirst() {
    return null;
  }

  public NodeItem getSecond() {
    return null;
  }

  public NodeItem getOne() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "first": return hasFirst();
      case "second": return hasSecond();
      case "one": return hasOne();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "first": return getFirst();
      case "second": return getSecond();
      case "one": return getOne();
      default: return null;
    }
  }

  public static SYJaaQUoI buildSYJaaQUoI(
    Symbol first,
    Symbol second
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    var firstNode = first.<NodeSitem>value();
    CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) first.getLocation();
    var secondNode = second.<NodeItem>value();
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) second.getLocation();
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SYJaaQUoI(
      firstNode,
      secondNode,
      CUP$AstLocParser$pos
    );
  }

  public static S9xAmQloq buildS9xAmQloq(
    Symbol one
  ) {
    var oneNode = one.<NodeItem>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) one.getLocation();
    return new S9xAmQloq(
      oneNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class SYJaaQUoI extends NodeEbnf13 {

    private final NodeSitem first;
    private final NodeItem second;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SYJaaQUoI(
      NodeSitem first,
      NodeItem second,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.first = first;
      this.second = second;
      this.location = location;
    }

    @Override
    public NodeSitem getFirst() {
      return first;
    }

    @Override
    public NodeItem getSecond() {
      return second;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasFirst() {
      return true;
    }

    @Override
    public boolean hasSecond() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("first", getFirst());
        case 1: return new AbstractMap.SimpleEntry<>("second", getSecond());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class S9xAmQloq extends NodeEbnf13 {

    private final NodeItem one;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S9xAmQloq(
      NodeItem one,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.one = one;
      this.location = location;
    }

    @Override
    public NodeItem getOne() {
      return one;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasOne() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("one", getOne());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
