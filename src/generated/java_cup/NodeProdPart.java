package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeProdPart extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "prod_part";
  }

  @Override
  public final String toString() {
    return "prod_part" + getLocation();
  }

  public boolean hasCodeStr() {
    return false;
  }

  public boolean hasS() {
    return false;
  }

  public boolean hasSymid() {
    return false;
  }

  public boolean hasQuant() {
    return false;
  }

  public boolean hasLabid() {
    return false;
  }

  public NodeString getCodeStr() {
    return null;
  }

  public NodeObject getS() {
    return null;
  }

  public NodeSymbolId getSymid() {
    return null;
  }

  public NodeQuantifier getQuant() {
    return null;
  }

  public NodeString getLabid() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "code_str": return hasCodeStr();
      case "s": return hasS();
      case "symid": return hasSymid();
      case "quant": return hasQuant();
      case "labid": return hasLabid();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "code_str": return getCodeStr();
      case "s": return getS();
      case "symid": return getSymid();
      case "quant": return getQuant();
      case "labid": return getLabid();
      default: return null;
    }
  }

  public static SdqXPtYi7 buildSdqXPtYi7(
    Symbol code_str
  ) {
    var code_strNode = new NodeString(code_str.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) code_str.getLocation());
    var CUP$CupParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) code_str.getLocation();
    return new SdqXPtYi7(
      code_strNode,
      CUP$CupParser$pos
    );
  }

  public static SHMB7RGMj buildSHMB7RGMj(
    Symbol s,
    Symbol symid,
    Symbol quant,
    Symbol labid
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var sNode = new NodeObject(s.<Object>value(), (java_cup.runtime.symbol.complex.ComplexLocation) s.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) s.getLocation();
    var symidNode = symid.<NodeSymbolId>value();
    NodeQuantifier quantNode = null;
    if (!quant.isNull()) {
      quantNode = quant.<NodeQuantifier>value();
    }
    var labidNode = new NodeString(labid.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) labid.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) labid.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SHMB7RGMj(
      sNode,
      symidNode,
      quantNode,
      labidNode,
      CUP$CupParser$pos
    );
  }

  public static SFyIqfVF9 buildSFyIqfVF9(
    Symbol symid,
    Symbol quant,
    Symbol labid
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var symidNode = symid.<NodeSymbolId>value();
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) symid.getLocation();
    NodeQuantifier quantNode = null;
    if (!quant.isNull()) {
      quantNode = quant.<NodeQuantifier>value();
    }
    var labidNode = new NodeString(labid.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) labid.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) labid.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SFyIqfVF9(
      symidNode,
      quantNode,
      labidNode,
      CUP$CupParser$pos
    );
  }


  public static final class SdqXPtYi7 extends NodeProdPart {

    private final NodeString code_str;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SdqXPtYi7(
      NodeString code_str,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.code_str = code_str;
      this.location = location;
    }

    @Override
    public NodeString getCodeStr() {
      return code_str;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasCodeStr() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("code_str", getCodeStr());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class SHMB7RGMj extends NodeProdPart {

    private final NodeObject s;
    private final NodeSymbolId symid;
    private final NodeQuantifier quant;
    private final NodeString labid;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SHMB7RGMj(
      NodeObject s,
      NodeSymbolId symid,
      NodeQuantifier quant,
      NodeString labid,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.s = s;
      this.symid = symid;
      this.quant = quant;
      this.labid = labid;
      this.location = location;
    }

    @Override
    public NodeObject getS() {
      return s;
    }

    @Override
    public NodeSymbolId getSymid() {
      return symid;
    }

    @Override
    public NodeQuantifier getQuant() {
      return quant;
    }

    @Override
    public NodeString getLabid() {
      return labid;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasS() {
      return true;
    }

    @Override
    public boolean hasSymid() {
      return true;
    }

    @Override
    public boolean hasQuant() {
      return quant != null;
    }

    @Override
    public boolean hasLabid() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("s", getS());
        case 1: return new AbstractMap.SimpleEntry<>("symid", getSymid());
        case 2: return getQuant() == null ? null : new AbstractMap.SimpleEntry<>("quant", getQuant());
        case 3: return new AbstractMap.SimpleEntry<>("labid", getLabid());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 4);
    }


  }
  public static final class SFyIqfVF9 extends NodeProdPart {

    private final NodeSymbolId symid;
    private final NodeQuantifier quant;
    private final NodeString labid;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SFyIqfVF9(
      NodeSymbolId symid,
      NodeQuantifier quant,
      NodeString labid,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.symid = symid;
      this.quant = quant;
      this.labid = labid;
      this.location = location;
    }

    @Override
    public NodeSymbolId getSymid() {
      return symid;
    }

    @Override
    public NodeQuantifier getQuant() {
      return quant;
    }

    @Override
    public NodeString getLabid() {
      return labid;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSymid() {
      return true;
    }

    @Override
    public boolean hasQuant() {
      return quant != null;
    }

    @Override
    public boolean hasLabid() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("symid", getSymid());
        case 1: return getQuant() == null ? null : new AbstractMap.SimpleEntry<>("quant", getQuant());
        case 2: return new AbstractMap.SimpleEntry<>("labid", getLabid());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }

}
