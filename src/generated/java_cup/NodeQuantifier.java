package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeQuantifier extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "quantifier";
  }

  @Override
  public final String toString() {
    return "quantifier" + getLocation();
  }

  public boolean hasQ() {
    return false;
  }

  public boolean hasSeps() {
    return false;
  }

  public boolean hasQe() {
    return false;
  }

  public boolean hasSp() {
    return false;
  }

  public NodeObject getQ() {
    return null;
  }

  public NodeListNodeSymbolId getSeps() {
    return null;
  }

  public NodeString getQe() {
    return null;
  }

  public NodeString getSp() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "q": return hasQ();
      case "seps": return hasSeps();
      case "qe": return hasQe();
      case "sp": return hasSp();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "q": return getQ();
      case "seps": return getSeps();
      case "qe": return getQe();
      case "sp": return getSp();
      default: return null;
    }
  }

  public static SzNzZhHVW buildSzNzZhHVW(
    Symbol q
  ) {
    var qNode = new NodeObject(q.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) q.getLocation());
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) q.getLocation();
    return new SzNzZhHVW(
      qNode,
      CUP$CupParser$pos
    );
  }

  public static S20yimO52 buildS20yimO52(
    Symbol seps,
    Symbol qe,
    Symbol sp
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var sepsNode = new NodeListNodeSymbolId(seps.<List<NodeSymbolId>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) seps.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) seps.getLocation();
    var qeNode = new NodeString(qe.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) qe.getLocation());
    var spNode = new NodeString(sp.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) sp.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) sp.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new S20yimO52(
      sepsNode,
      qeNode,
      spNode,
      CUP$CupParser$pos
    );
  }

  public static SGT3fosEj buildSGT3fosEj(
    Symbol sp
  ) {
    var spNode = new NodeString(sp.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) sp.getLocation());
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) sp.getLocation();
    return new SGT3fosEj(
      spNode,
      CUP$CupParser$pos
    );
  }


  public static final class SzNzZhHVW extends NodeQuantifier {

    private final NodeObject q;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SzNzZhHVW(
      NodeObject q,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.q = q;
      this.location = location;
    }

    @Override
    public NodeObject getQ() {
      return q;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasQ() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("q", getQ());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class S20yimO52 extends NodeQuantifier {

    private final NodeListNodeSymbolId seps;
    private final NodeString qe;
    private final NodeString sp;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  S20yimO52(
      NodeListNodeSymbolId seps,
      NodeString qe,
      NodeString sp,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.seps = seps;
      this.qe = qe;
      this.sp = sp;
      this.location = location;
    }

    @Override
    public NodeListNodeSymbolId getSeps() {
      return seps;
    }

    @Override
    public NodeString getQe() {
      return qe;
    }

    @Override
    public NodeString getSp() {
      return sp;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSeps() {
      return true;
    }

    @Override
    public boolean hasQe() {
      return true;
    }

    @Override
    public boolean hasSp() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("seps", getSeps());
        case 1: return new AbstractMap.SimpleEntry<>("qe", getQe());
        case 2: return new AbstractMap.SimpleEntry<>("sp", getSp());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class SGT3fosEj extends NodeQuantifier {

    private final NodeString sp;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SGT3fosEj(
      NodeString sp,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.sp = sp;
      this.location = location;
    }

    @Override
    public NodeString getSp() {
      return sp;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSp() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("sp", getSp());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
