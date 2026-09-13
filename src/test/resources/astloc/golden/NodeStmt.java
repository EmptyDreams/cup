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

  public boolean hasLl() {
    return false;
  }

  public boolean hasLx() {
    return false;
  }

  public boolean hasSxs() {
    return false;
  }

  public boolean hasFcX() {
    return false;
  }

  public boolean hasFcY() {
    return false;
  }

  public boolean hasTa() {
    return false;
  }

  public boolean hasTb() {
    return false;
  }

  public boolean hasTxs() {
    return false;
  }

  public boolean hasQ() {
    return false;
  }

  public boolean hasPxs() {
    return false;
  }

  public boolean hasBa() {
    return false;
  }

  public boolean hasBb() {
    return false;
  }

  public boolean hasV() {
    return false;
  }

  public boolean hasA() {
    return false;
  }

  public boolean hasLa() {
    return false;
  }

  public boolean hasLb() {
    return false;
  }

  public boolean hasX() {
    return false;
  }

  public boolean hasY() {
    return false;
  }

  public boolean hasMa() {
    return false;
  }

  public boolean hasMb() {
    return false;
  }

  public boolean hasMc() {
    return false;
  }

  public boolean hasMxs() {
    return false;
  }

  public NodeListNodeSitem getLl() {
    return null;
  }

  public NodeItem getLx() {
    return null;
  }

  public NodeListNodeItem getSxs() {
    return null;
  }

  public NodeString getFcX() {
    return null;
  }

  public NodeString getFcY() {
    return null;
  }

  public NodeItem getTa() {
    return null;
  }

  public NodeSitem getTb() {
    return null;
  }

  public NodeListNodeSitem getTxs() {
    return null;
  }

  public NodeEbnf14 getQ() {
    return null;
  }

  public NodeListNodeTitem getPxs() {
    return null;
  }

  public NodeSitem getBa() {
    return null;
  }

  public NodeTitem getBb() {
    return null;
  }

  public NodeEbnf16 getV() {
    return null;
  }

  public NodeEbnf13 getA() {
    return null;
  }

  public NodeSitem getLa() {
    return null;
  }

  public NodeItem getLb() {
    return null;
  }

  public NodeString getX() {
    return null;
  }

  public NodeString getY() {
    return null;
  }

  public NodeItem getMa() {
    return null;
  }

  public NodeSitem getMb() {
    return null;
  }

  public NodeItem getMc() {
    return null;
  }

  public NodeListNodeItem getMxs() {
    return null;
  }

  @Override
  public final boolean hasLabel(
    String label
  ) {
    switch (label) {
      case "ll": return hasLl();
      case "lx": return hasLx();
      case "sxs": return hasSxs();
      case "fcX": return hasFcX();
      case "fcY": return hasFcY();
      case "ta": return hasTa();
      case "tb": return hasTb();
      case "txs": return hasTxs();
      case "q": return hasQ();
      case "pxs": return hasPxs();
      case "ba": return hasBa();
      case "bb": return hasBb();
      case "v": return hasV();
      case "a": return hasA();
      case "la": return hasLa();
      case "lb": return hasLb();
      case "x": return hasX();
      case "y": return hasY();
      case "ma": return hasMa();
      case "mb": return hasMb();
      case "mc": return hasMc();
      case "mxs": return hasMxs();
      default: return false;
    }
  }

  @Override
  public final AstNode getByLabel(
    String label
  ) {
    switch (label) {
      case "ll": return getLl();
      case "lx": return getLx();
      case "sxs": return getSxs();
      case "fcX": return getFcX();
      case "fcY": return getFcY();
      case "ta": return getTa();
      case "tb": return getTb();
      case "txs": return getTxs();
      case "q": return getQ();
      case "pxs": return getPxs();
      case "ba": return getBa();
      case "bb": return getBb();
      case "v": return getV();
      case "a": return getA();
      case "la": return getLa();
      case "lb": return getLb();
      case "x": return getX();
      case "y": return getY();
      case "ma": return getMa();
      case "mb": return getMb();
      case "mc": return getMc();
      case "mxs": return getMxs();
      default: return null;
    }
  }

  public static LeadList buildLeadList(
    Symbol ll,
    Symbol lx
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    NodeListNodeSitem llNode = null;
    if (!ll.isNull()) {
      llNode = new NodeListNodeSitem(ll.<List<NodeSitem>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) ll.getLocation());
      CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ll.getLocation();
    }
    var lxNode = lx.<NodeItem>value();
    if (CUP$AstLocParser$left == null)
      CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lx.getLocation();
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) lx.getLocation();
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new LeadList(
      llNode,
      lxNode,
      CUP$AstLocParser$pos
    );
  }

  public static SepStar buildSepStar(
    Symbol sxs
  ) {
    NodeListNodeItem sxsNode = null;
    if (!sxs.isNull()) {
      sxsNode = new NodeListNodeItem(sxs.<List<NodeItem>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) sxs.getLocation());
    }
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) sxs.getLocation();
    return new SepStar(
      sxsNode,
      CUP$AstLocParser$pos
    );
  }

  public static FlatStmt buildFlatStmt(
    Symbol fc
  ) {
    var fcNode = fc.<NodeFlatContent>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) fc.getLocation();
    return new FlatStmt(
      fcNode,
      CUP$AstLocParser$pos
    );
  }

  public static TrailOpt buildTrailOpt(
    Symbol ta,
    Symbol tb
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    var taNode = ta.<NodeItem>value();
    CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ta.getLocation();
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) ta.getLocation();
    NodeSitem tbNode = null;
    if (!tb.isNull()) {
      tbNode = tb.<NodeSitem>value();
      CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) tb.getLocation();
    }
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new TrailOpt(
      taNode,
      tbNode,
      CUP$AstLocParser$pos
    );
  }

  public static TailSep buildTailSep(
    Symbol txs
  ) {
    NodeListNodeSitem txsNode = null;
    if (!txs.isNull()) {
      txsNode = new NodeListNodeSitem(txs.<List<NodeSitem>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) txs.getLocation());
    }
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) txs.getLocation();
    return new TailSep(
      txsNode,
      CUP$AstLocParser$pos
    );
  }

  public static QAnonStmt buildQAnonStmt(
    Symbol q
  ) {
    NodeEbnf14 qNode = null;
    if (!q.isNull()) {
      qNode = q.<NodeEbnf14>value();
    }
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) q.getLocation();
    return new QAnonStmt(
      qNode,
      CUP$AstLocParser$pos
    );
  }

  public static PlusList buildPlusList(
    Symbol pxs
  ) {
    var pxsNode = new NodeListNodeTitem(pxs.<List<NodeTitem>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) pxs.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) pxs.getLocation();
    return new PlusList(
      pxsNode,
      CUP$AstLocParser$pos
    );
  }

  public static BothOpt buildBothOpt(
    Symbol ba,
    Symbol bb
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    NodeSitem baNode = null;
    if (!ba.isNull()) {
      baNode = ba.<NodeSitem>value();
      CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ba.getLocation();
      CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) ba.getLocation();
    }
    NodeTitem bbNode = null;
    if (!bb.isNull()) {
      bbNode = bb.<NodeTitem>value();
      if (CUP$AstLocParser$left == null)
        CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) bb.getLocation();
      CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) bb.getLocation();
    }
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new BothOpt(
      baNode,
      bbNode,
      CUP$AstLocParser$pos
    );
  }

  public static TypedStmt buildTypedStmt(
    Symbol v
  ) {
    var vNode = v.<NodeEbnf16>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) v.getLocation();
    return new TypedStmt(
      vNode,
      CUP$AstLocParser$pos
    );
  }

  public static AnonStmt buildAnonStmt(
    Symbol a
  ) {
    var aNode = a.<NodeEbnf13>value();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) a.getLocation();
    return new AnonStmt(
      aNode,
      CUP$AstLocParser$pos
    );
  }

  public static LeadOpt buildLeadOpt(
    Symbol la,
    Symbol lb
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    NodeSitem laNode = null;
    if (!la.isNull()) {
      laNode = la.<NodeSitem>value();
      CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) la.getLocation();
    }
    var lbNode = lb.<NodeItem>value();
    if (CUP$AstLocParser$left == null)
      CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) lb.getLocation();
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) lb.getLocation();
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new LeadOpt(
      laNode,
      lbNode,
      CUP$AstLocParser$pos
    );
  }

  public static SpreadStmt buildSpreadStmt(
    Symbol $0
  ) {
    var $0Node = $0.<NodeFlatContent>value();
    var xNode = $0Node.getX();
    var yNode = $0Node.getY();
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) $0.getLocation();
    return new SpreadStmt(
      xNode,
      yNode,
      CUP$AstLocParser$pos
    );
  }

  public static MixOpt buildMixOpt(
    Symbol ma,
    Symbol mb,
    Symbol mc
  ) {
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$left = null;
    java_cup.runtime.symbol.complex.ComplexLocation CUP$AstLocParser$right = null;
    var maNode = ma.<NodeItem>value();
    CUP$AstLocParser$left = (java_cup.runtime.symbol.complex.ComplexLocation) ma.getLocation();
    NodeSitem mbNode = null;
    if (!mb.isNull()) {
      mbNode = mb.<NodeSitem>value();
    }
    var mcNode = mc.<NodeItem>value();
    CUP$AstLocParser$right = (java_cup.runtime.symbol.complex.ComplexLocation) mc.getLocation();
    var CUP$AstLocParser$pos = (CUP$AstLocParser$left != null ? CUP$AstLocParser$left : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION).span(CUP$AstLocParser$right != null ? CUP$AstLocParser$right : java_cup.runtime.symbol.complex.ComplexLocation.NO_LOCATION);
    return new MixOpt(
      maNode,
      mbNode,
      mcNode,
      CUP$AstLocParser$pos
    );
  }

  public static MultiSep buildMultiSep(
    Symbol mxs
  ) {
    var mxsNode = new NodeListNodeItem(mxs.<List<NodeItem>>value(), (java_cup.runtime.symbol.complex.ComplexLocation) mxs.getLocation());
    var CUP$AstLocParser$pos = (java_cup.runtime.symbol.complex.ComplexLocation) mxs.getLocation();
    return new MultiSep(
      mxsNode,
      CUP$AstLocParser$pos
    );
  }


  public static final class LeadList extends NodeStmt {

    private final NodeListNodeSitem ll;
    private final NodeItem lx;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  LeadList(
      NodeListNodeSitem ll,
      NodeItem lx,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ll = ll;
      this.lx = lx;
      this.location = location;
    }

    @Override
    public NodeListNodeSitem getLl() {
      return ll;
    }

    @Override
    public NodeItem getLx() {
      return lx;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLl() {
      return ll != null;
    }

    @Override
    public boolean hasLx() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getLl() == null ? null : new AbstractMap.SimpleEntry<>("ll", getLl());
        case 1: return new AbstractMap.SimpleEntry<>("lx", getLx());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SepStar extends NodeStmt {

    private final NodeListNodeItem sxs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SepStar(
      NodeListNodeItem sxs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.sxs = sxs;
      this.location = location;
    }

    @Override
    public NodeListNodeItem getSxs() {
      return sxs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasSxs() {
      return sxs != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getSxs() == null ? null : new AbstractMap.SimpleEntry<>("sxs", getSxs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class FlatStmt extends NodeStmt {

    private final NodeFlatContent fc;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  FlatStmt(
      NodeFlatContent fc,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.fc = fc;
      this.location = location;
    }

    @Override
    public NodeString getFcX() {
      return fc.getX();
    }

    @Override
    public NodeString getFcY() {
      return fc.getY();
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasFcX() {
      return fc.hasX();
    }

    @Override
    public boolean hasFcY() {
      return fc.hasY();
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("fcX", getFcX());
        case 1: return new AbstractMap.SimpleEntry<>("fcY", getFcY());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class TrailOpt extends NodeStmt {

    private final NodeItem ta;
    private final NodeSitem tb;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  TrailOpt(
      NodeItem ta,
      NodeSitem tb,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ta = ta;
      this.tb = tb;
      this.location = location;
    }

    @Override
    public NodeItem getTa() {
      return ta;
    }

    @Override
    public NodeSitem getTb() {
      return tb;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTa() {
      return true;
    }

    @Override
    public boolean hasTb() {
      return tb != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("ta", getTa());
        case 1: return getTb() == null ? null : new AbstractMap.SimpleEntry<>("tb", getTb());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class TailSep extends NodeStmt {

    private final NodeListNodeSitem txs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  TailSep(
      NodeListNodeSitem txs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.txs = txs;
      this.location = location;
    }

    @Override
    public NodeListNodeSitem getTxs() {
      return txs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasTxs() {
      return txs != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getTxs() == null ? null : new AbstractMap.SimpleEntry<>("txs", getTxs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class QAnonStmt extends NodeStmt {

    private final NodeEbnf14 q;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  QAnonStmt(
      NodeEbnf14 q,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.q = q;
      this.location = location;
    }

    @Override
    public NodeEbnf14 getQ() {
      return q;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasQ() {
      return q != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getQ() == null ? null : new AbstractMap.SimpleEntry<>("q", getQ());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class PlusList extends NodeStmt {

    private final NodeListNodeTitem pxs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  PlusList(
      NodeListNodeTitem pxs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.pxs = pxs;
      this.location = location;
    }

    @Override
    public NodeListNodeTitem getPxs() {
      return pxs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasPxs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("pxs", getPxs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class BothOpt extends NodeStmt {

    private final NodeSitem ba;
    private final NodeTitem bb;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  BothOpt(
      NodeSitem ba,
      NodeTitem bb,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ba = ba;
      this.bb = bb;
      this.location = location;
    }

    @Override
    public NodeSitem getBa() {
      return ba;
    }

    @Override
    public NodeTitem getBb() {
      return bb;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasBa() {
      return ba != null;
    }

    @Override
    public boolean hasBb() {
      return bb != null;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getBa() == null ? null : new AbstractMap.SimpleEntry<>("ba", getBa());
        case 1: return getBb() == null ? null : new AbstractMap.SimpleEntry<>("bb", getBb());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class TypedStmt extends NodeStmt {

    private final NodeEbnf16 v;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  TypedStmt(
      NodeEbnf16 v,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.v = v;
      this.location = location;
    }

    @Override
    public NodeEbnf16 getV() {
      return v;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasV() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("v", getV());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class AnonStmt extends NodeStmt {

    private final NodeEbnf13 a;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  AnonStmt(
      NodeEbnf13 a,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.a = a;
      this.location = location;
    }

    @Override
    public NodeEbnf13 getA() {
      return a;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasA() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("a", getA());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }
  public static final class LeadOpt extends NodeStmt {

    private final NodeSitem la;
    private final NodeItem lb;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  LeadOpt(
      NodeSitem la,
      NodeItem lb,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.la = la;
      this.lb = lb;
      this.location = location;
    }

    @Override
    public NodeSitem getLa() {
      return la;
    }

    @Override
    public NodeItem getLb() {
      return lb;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasLa() {
      return la != null;
    }

    @Override
    public boolean hasLb() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return getLa() == null ? null : new AbstractMap.SimpleEntry<>("la", getLa());
        case 1: return new AbstractMap.SimpleEntry<>("lb", getLb());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class SpreadStmt extends NodeStmt {

    private final NodeString x;
    private final NodeString y;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  SpreadStmt(
      NodeString x,
      NodeString y,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.x = x;
      this.y = y;
      this.location = location;
    }

    @Override
    public NodeString getX() {
      return x;
    }

    @Override
    public NodeString getY() {
      return y;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasX() {
      return true;
    }

    @Override
    public boolean hasY() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("x", getX());
        case 1: return new AbstractMap.SimpleEntry<>("y", getY());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 2);
    }


  }
  public static final class MixOpt extends NodeStmt {

    private final NodeItem ma;
    private final NodeSitem mb;
    private final NodeItem mc;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  MixOpt(
      NodeItem ma,
      NodeSitem mb,
      NodeItem mc,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.ma = ma;
      this.mb = mb;
      this.mc = mc;
      this.location = location;
    }

    @Override
    public NodeItem getMa() {
      return ma;
    }

    @Override
    public NodeSitem getMb() {
      return mb;
    }

    @Override
    public NodeItem getMc() {
      return mc;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasMa() {
      return true;
    }

    @Override
    public boolean hasMb() {
      return mb != null;
    }

    @Override
    public boolean hasMc() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("ma", getMa());
        case 1: return getMb() == null ? null : new AbstractMap.SimpleEntry<>("mb", getMb());
        case 2: return new AbstractMap.SimpleEntry<>("mc", getMc());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 3);
    }


  }
  public static final class MultiSep extends NodeStmt {

    private final NodeListNodeItem mxs;
    private final java_cup.runtime.symbol.complex.ComplexLocation location;

    public  MultiSep(
      NodeListNodeItem mxs,
      java_cup.runtime.symbol.complex.ComplexLocation location
    ) {
      this.mxs = mxs;
      this.location = location;
    }

    @Override
    public NodeListNodeItem getMxs() {
      return mxs;
    }

    @Override
    public java_cup.runtime.symbol.complex.ComplexLocation getLocation() {
      return location;
    }

    @Override
    public boolean hasMxs() {
      return true;
    }

    @Override
    public Map.Entry<String, AstNode> getByIndex(
      int index
    ) {
      switch (index) {
        case 0: return new AbstractMap.SimpleEntry<>("mxs", getMxs());
        default: throw new IndexOutOfBoundsException(index);
      }
    }

    @Override
    public Iterator<Map.Entry<String, AstNode>> iterator() {
      return new AstNodeIterator(this, 1);
    }


  }

}
