package java_cup;

import java.util.*;
import java_cup.runtime.*;
import java_cup.runtime.symbol.Location;
import java_cup.runtime.symbol.complex.ComplexLocation;

public class NodeSpec extends AstNode {

  @Override
  public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
    return java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION;
  }

  @Override
  public final String getNodeName() {
    return "spec";
  }

  @Override
  public final String toString() {
    return "spec" + getLocation();
  }

  public boolean hasPkg() {
    return false;
  }

  public boolean hasImports() {
    return false;
  }

  public boolean hasCls() {
    return false;
  }

  public boolean hasCodes() {
    return false;
  }

  public boolean hasSyms() {
    return false;
  }

  public boolean hasPrecs() {
    return false;
  }

  public boolean hasStart() {
    return false;
  }

  public boolean hasProds() {
    return false;
  }

  public NodeString getPkg() {
    return null;
  }

  public NodeListNodeImportSpec getImports() {
    return null;
  }

  public NodeString getCls() {
    return null;
  }

  public NodeListNodeCodePart getCodes() {
    return null;
  }

  public NodeListNodeSymbol getSyms() {
    return null;
  }

  public NodeListNodePreced getPrecs() {
    return null;
  }

  public NodeString getStart() {
    return null;
  }

  public NodeListNodeProduction getProds() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "pkg": return hasPkg();
      case "imports": return hasImports();
      case "cls": return hasCls();
      case "codes": return hasCodes();
      case "syms": return hasSyms();
      case "precs": return hasPrecs();
      case "start": return hasStart();
      case "prods": return hasProds();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "pkg": return getPkg();
      case "imports": return getImports();
      case "cls": return getCls();
      case "codes": return getCodes();
      case "syms": return getSyms();
      case "precs": return getPrecs();
      case "start": return getStart();
      case "prods": return getProds();
      default: return null;
    }
  }

  public static SjZdUcPYP buildSjZdUcPYP(
    Symbol pkg,
    Symbol imports,
    Symbol cls,
    Symbol codes,
    Symbol syms,
    Symbol precs,
    Symbol start,
    Symbol prods
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$CupParser$right = null;
    var pkgNode = new NodeString(pkg.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) pkg.getLocation());
    CUP$CupParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) pkg.getLocation();
    NodeListNodeImportSpec importsNode = null;
    if (!imports.isNull()) {
      importsNode = new NodeListNodeImportSpec(imports.<List<NodeImportSpec>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) imports.getLocation());
    }
    var clsNode = new NodeString(cls.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) cls.getLocation());
    NodeListNodeCodePart codesNode = null;
    if (!codes.isNull()) {
      codesNode = new NodeListNodeCodePart(codes.<List<NodeCodePart>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) codes.getLocation());
    }
    var symsNode = new NodeListNodeSymbol(syms.<List<NodeSymbol>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) syms.getLocation());
    NodeListNodePreced precsNode = null;
    if (!precs.isNull()) {
      precsNode = new NodeListNodePreced(precs.<List<NodePreced>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) precs.getLocation());
    }
    var startNode = new NodeString(start.<String>value(), (java_cup.runtime.symbol.complex.ComplexLocation) start.getLocation());
    var prodsNode = new NodeListNodeProduction(prods.<List<NodeProduction>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) prods.getLocation());
    CUP$CupParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) prods.getLocation();
    var CUP$CupParser$pos = (CUP$CupParser$left != null ? CUP$CupParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$CupParser$right != null ? CUP$CupParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new SjZdUcPYP(
      pkgNode,
      importsNode,
      clsNode,
      codesNode,
      symsNode,
      precsNode,
      startNode,
      prodsNode,
      CUP$CupParser$pos
    );
  }


  public static final class SjZdUcPYP extends NodeSpec {

    private final NodeString pkg;
    private final NodeListNodeImportSpec imports;
    private final NodeString cls;
    private final NodeListNodeCodePart codes;
    private final NodeListNodeSymbol syms;
    private final NodeListNodePreced precs;
    private final NodeString start;
    private final NodeListNodeProduction prods;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SjZdUcPYP(
      NodeString pkg,
      NodeListNodeImportSpec imports,
      NodeString cls,
      NodeListNodeCodePart codes,
      NodeListNodeSymbol syms,
      NodeListNodePreced precs,
      NodeString start,
      NodeListNodeProduction prods,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.pkg = pkg;
      this.imports = imports;
      this.cls = cls;
      this.codes = codes;
      this.syms = syms;
      this.precs = precs;
      this.start = start;
      this.prods = prods;
      this.location = location;
    }

    @Override
    public NodeString getPkg() {
      return pkg;
    }

    @Override
    public NodeListNodeImportSpec getImports() {
      return imports;
    }

    @Override
    public NodeString getCls() {
      return cls;
    }

    @Override
    public NodeListNodeCodePart getCodes() {
      return codes;
    }

    @Override
    public NodeListNodeSymbol getSyms() {
      return syms;
    }

    @Override
    public NodeListNodePreced getPrecs() {
      return precs;
    }

    @Override
    public NodeString getStart() {
      return start;
    }

    @Override
    public NodeListNodeProduction getProds() {
      return prods;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasPkg() {
      return true;
    }

    @Override
    public boolean hasImports() {
      return imports != null;
    }

    @Override
    public boolean hasCls() {
      return true;
    }

    @Override
    public boolean hasCodes() {
      return codes != null;
    }

    @Override
    public boolean hasSyms() {
      return true;
    }

    @Override
    public boolean hasPrecs() {
      return precs != null;
    }

    @Override
    public boolean hasStart() {
      return true;
    }

    @Override
    public boolean hasProds() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("pkg", getPkg());
        case 1: return getImports() == null ? null : new AbstractMap.SimpleEntry<>("imports", getImports());
        case 2: return new AbstractMap.SimpleEntry<>("cls", getCls());
        case 3: return getCodes() == null ? null : new AbstractMap.SimpleEntry<>("codes", getCodes());
        case 4: return new AbstractMap.SimpleEntry<>("syms", getSyms());
        case 5: return getPrecs() == null ? null : new AbstractMap.SimpleEntry<>("precs", getPrecs());
        case 6: return new AbstractMap.SimpleEntry<>("start", getStart());
        case 7: return new AbstractMap.SimpleEntry<>("prods", getProds());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 8);
    }


  }

}
