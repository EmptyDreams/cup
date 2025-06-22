package java_cup;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class AstNodeBuilder {

    private AstNodeBuilder() {}

    public static NonTermInfo initInfo(non_terminal nt) throws internal_error {
        Map<String, String> label2Type = new HashMap<>();
        List<VirtualProduction> prods = new ArrayList<>();
        Map<String, List<InlineLabel>> inlineLabels = new HashMap<>();
        o:
        for (production prod : nt.productions()) {
            List<String> labels = new ArrayList<>();
            for (int i = 0; i < prod.rhs_length(); i++) {
                var rhs = prod.rhs(i);
                var label = rhs.label();
                if (label == null || rhs.is_action()) continue;
                labels.add(label);
                if (emit.isExistenceVar(label)) {
                    var old = label2Type.put(label, "");
                    if (old != null && !old.isEmpty()) {
                        throw new internal_error(
                            "Label " + label + " from " + nt.name() + " is used for more than one type"
                        );
                    }
                    continue;
                }
                var sym = ((symbol_part) rhs).the_symbol();
                var type = sym.astClassName();
                var old = label2Type.put(label, type);
                if (old != null && !old.equals(type)) {
                    throw new internal_error(
                        "Label " + label + " from " + nt.name() + " is used for more than one type"
                    );
                }
                var prefix = Main.ast_flatten.getInlineName(label);
                if (sym.is_non_term() && prefix != null) {
                    List<InlineLabel> list = new ArrayList<>();
                    var map = ((non_terminal) sym).getInlineExpr();
                    for (var entry : map.entrySet()) {
                        var subLabel = entry.getKey();
                        var subSym = entry.getValue();
                        var subName = emit.joinName(prefix, subLabel);
                        var subType = emit.isExistenceVar(subName) ? "" : subSym.astClassName();
                        old = label2Type.put(subName, subType);
                        if (old != null && !old.equals(subType)) {
                            throw new internal_error(
                                "Label " + subName + " from " + nt.name() + " is used for more than one type"
                            );
                        }
                        list.add(new InlineLabel(subLabel, subName));
                    }
                    inlineLabels.put(label, list);
                }
            }
            if (labels.isEmpty()) continue;
            String name = prod.getProdName();
            for (var item : prods) {
                if (item.name.equals(name)) {
                    item.addSrcExpr(prod.to_simple_string());
                    continue o;
                }
            }
            prods.add(new VirtualProduction(prod.to_simple_string(), name, labels));
        }
        return new NonTermInfo(nt.astClassName(), label2Type, prods, inlineLabels);
    }

    public static void generate(PrintWriter writer, NonTermInfo info) {
        emit.emit_package(writer);
        writer.println("import java.util.*;");
        writer.println("import java_cup.runtime.IAstNode;");
        writer.println();

        writer.println("@SuppressWarnings({");
        writer.println("  \"SpellCheckingInspection\",");
        writer.println("  \"EnhancedSwitchMigration\",");
        writer.println("  \"UnnecessaryLocalVariable\",");
        writer.println("  \"RedundantSuppression\"");
        writer.println("})");
        writer.println("public class " + info.className + " implements IAstNode {");
        writer.println();
        info.label2Type.forEach((label, type) -> {
            if (info.inlineLabels.containsKey(label)) return;
            var isExistenceVar = type.isEmpty();
            var stName = isExistenceVar ? label : emit.castToStName(label);
            if (isExistenceVar) {
                writer.println("  public boolean " + stName + "() {");
                writer.println("    return false;");
                writer.println("  }");
                writer.println();
            } else {
                writer.println("  public boolean has" + stName + "() {");
                writer.println("    return false;");
                writer.println("  }");
                writer.println();
                writer.println("  public " + type + " get" + stName + "() {");
                writer.println("    return null;");
                writer.println("  }");
                writer.println();
            }
        });
        var label2TypeExcludeInline = info.label2Type
            .entrySet()
            .stream()
            .filter(entry -> !info.inlineLabels.containsKey(entry.getKey()))
            .collect(Collectors.toList());
        writer.println("  @Override");
        writer.println("  public final boolean hasLabel(String label) {");
        if (label2TypeExcludeInline.isEmpty()) {
            writer.println("    return false;");
        } else if (label2TypeExcludeInline.size() == 1) {
            var entry = label2TypeExcludeInline.get(0);
            var label = entry.getKey();
            var type = entry.getValue();
            var isExistenceVar = type.isEmpty();
            if (isExistenceVar) {
                writer.println("    return \"" + label + "\".equals(label) && " + label + "();");
            } else {
                var stName = emit.castToStName(label);
                writer.println("    return \"" + label + "\".equals(label) && has" + stName + "();");
            }
        } else {
            writer.println("    switch (label) {");
            for (var entry : label2TypeExcludeInline) {
                var label = entry.getKey();
                var type = entry.getValue();
                var isExistenceVar = type.isEmpty();
                var stName = isExistenceVar ? label : emit.castToStName(label);
                if (isExistenceVar) {
                    writer.println("      case \"" + label + "\": return " + stName + "();");
                } else {
                    writer.println("      case \"" + label + "\": return has" + stName + "();");
                }
            }
            writer.println("      default: return false;");
            writer.println("    }");
        }
        writer.println("  }");
        writer.println();
        var label2TypeExcludeInlineExist = label2TypeExcludeInline
            .stream()
            .filter(entry -> !entry.getValue().isEmpty())
            .collect(Collectors.toList());
        writer.println("  @Override");
        writer.println("  public final Object getByLabel(String label) {");
        if (label2TypeExcludeInlineExist.isEmpty()) {
            writer.println("    return null;");
        } else if (label2TypeExcludeInlineExist.size() == 1) {
            var entry = label2TypeExcludeInlineExist.get(0);
            var label = entry.getKey();
            var stName = emit.castToStName(label);
            writer.println("    return \"" + label + "\".equals(label) ? get" + stName + "() : null;");
        } else {
            writer.println("    switch (label) {");
            for (var entry : label2TypeExcludeInlineExist) {
                var label = entry.getKey();
                var stName = emit.castToStName(label);
                writer.println("      case \"" + label + "\": return get" + stName + "();");
            }
            writer.println("      default: return null;");
            writer.println("    }");
        }
        writer.println("  }");
        writer.println();
        for (var prod : info.prods) {
            writer.print("  public static " + prod.name + " build" + prod.name + "(");
            boolean isFirst = true;
            for (String label : prod.labels) {
                var type = info.label2Type.get(label);
                var isExistenceVar = type.isEmpty();
                if (isExistenceVar) continue;
                if (isFirst) {
                    isFirst = false;
                    writer.println();
                } else {
                    writer.println(',');
                }
                writer.print("    " + type + " _" + label);
            }
            if (!isFirst) {
                writer.println();
                writer.print("  ");
            }
            writer.println(") {");
            writer.println("    " + prod.name + " result = new " + prod.name + "();");
            for (String label : prod.labels) {
                var type = info.label2Type.get(label);
                var isExistenceVar = type.isEmpty();
                if (!isExistenceVar) {
                    writer.println("    result._" + label + " = _" + label + ';');
                }
            }
            writer.println("    return result;");
            writer.println("  }");
            writer.println();
        }
        for (var prod : info.prods) {
            for (String text : prod.srcExprList) {
                writer.println("  // " + text);
            }
            writer.println("  public static final class " + prod.name + " extends " + info.className + " {");
            writer.println();
            var hasField = false;
            for (String label : prod.labels) {
                var type = info.label2Type.get(label);
                var isExistenceVar = type.isEmpty();
                if (!isExistenceVar) {
                    hasField = true;
                    writer.println("    private " + type + " _" + label + ";");
                }
            }
            if (hasField) {
                writer.println();
            }
            for (String label : prod.labels) {
                if (info.inlineLabels.containsKey(label)) {
                    for (var subLabel : info.inlineLabels.get(label)) {
                        var type = info.label2Type.get(subLabel.label);
                        var isExistenceVar = type.isEmpty();
                        if (isExistenceVar) {
                            writer.println("    @Override");
                            writer.println("    public boolean " + subLabel.label + "() {");
                            writer.println("      return _" + label + '.' + subLabel.srcLabel + "();");
                            writer.println("    }");
                            writer.println();
                        } else {
                            var stLabel = emit.castToStName(subLabel.label);
                            var stSrcLabel = emit.castToStName(subLabel.srcLabel);
                            writer.println("    @Override");
                            writer.println("    public " + type + " get" + stLabel + "() {");
                            writer.println("      return _" + label + ".get" + stSrcLabel + "();");
                            writer.println("    }");
                            writer.println();
                            writer.println("    @Override");
                            writer.println("    public boolean has" + stLabel + "() {");
                            writer.println("      return _" + label + ".has" + stSrcLabel + "();");
                            writer.println("    }");
                            writer.println();
                        }
                    }
                } else {
                    var type = info.label2Type.get(label);
                    var isExistenceVar = type.isEmpty();
                    var stName = isExistenceVar ? label : emit.castToStName(label);
                    if (isExistenceVar) {
                        writer.println("    @Override");
                        writer.println("    public boolean " + stName + "() {");
                        writer.println("      return true;");
                        writer.println("    }");
                        writer.println();
                    } else {
                        writer.println("    @Override");
                        writer.println("    public boolean has" + stName + "() {");
                        writer.println("      return true;");
                        writer.println("    }");
                        writer.println();
                        writer.println("    @Override");
                        writer.println("    public " + type + " get" + stName + "() {");
                        writer.println("      return _" + label + ';');
                        writer.println("    }");
                        writer.println();
                    }
                }
            }
            writer.println("  }");
            writer.println();
        }
        writer.println('}');
    }

    public static class NonTermInfo {

        public final String className;
        public final Map<String, String> label2Type;
        public final Map<String, List<InlineLabel>> inlineLabels;
        public final List<VirtualProduction> prods;

        public NonTermInfo(
            String className,
            Map<String, String> label2Type,
            List<VirtualProduction> prods,
            Map<String, List<InlineLabel>> inlineLabels
        ) {
            this.className = className;
            this.label2Type = Collections.unmodifiableMap(label2Type);
            this.prods = Collections.unmodifiableList(prods);
            this.inlineLabels = Collections.unmodifiableMap(inlineLabels);
        }

    }

    public static class InlineLabel {

        public final String srcLabel;
        public final String label;

        public InlineLabel(String srcLabel, String label) {
            this.srcLabel = srcLabel;
            this.label = label;
        }

    }

    public static class VirtualProduction {

        public final Set<String> srcExprList = new HashSet<>();
        public final String name;
        public final List<String> labels;

        public VirtualProduction(String srcExpr, String name, List<String> labels) {
            this.srcExprList.add(srcExpr);
            this.name = name;
            this.labels = Collections.unmodifiableList(labels);
        }

        private void addSrcExpr(String srcExpr) {
            srcExprList.add(srcExpr);
        }

    }

}