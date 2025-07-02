package java_cup;

import java_cup.runtime.ArrayStack;

import java.io.PrintWriter;
import java.util.*;

public class AstNodeBuilder {

    private AstNodeBuilder() {}

    private static final Map<non_terminal, VirtualType> typeCache = new HashMap<>();
    private static final Map<String, VirtualType> basicTypeCache = new HashMap<>();

    public static VirtualType initInfo(symbol sym) throws internal_error {
        if (sym.is_non_term() && ("IAstNode".equals(sym.stack_type()) || non_terminal.START_nt.equals(sym))) {
            return initInfo((non_terminal) sym, null, 0);
        } else {
            return basicTypeCache.computeIfAbsent(sym.astClassName(), k -> {
                var type = new VirtualType();
                type.className = k;
                type.prods = Collections.emptyList();
                return type;
            });
        }
    }

    private static VirtualType initInfo(non_terminal nt, production fromProd, int index) throws internal_error {
        if (nt.isAnno() && fromProd == null) return null;
        if (typeCache.containsKey(nt)) return typeCache.get(nt);
        var type = new VirtualType();
        if (!nt.isAnno()) typeCache.put(nt, type);
        int count = 0;
        Map<String, VirtualProduction> prods = new HashMap<>();
        var annoLabelsMap = fromProd == null ? null : ((non_terminal) fromProd.lhs().the_symbol()).getAnnoLabelAndAction(fromProd);
        for (production prod : nt.productions()) {
            if (prod.hasTailAction() && !nt.equals(non_terminal.START_nt)) continue;
            var anno = annoLabelsMap == null ? null : annoLabelsMap.get(prod.index());
            if (anno != null && anno.getSecond() != null) continue;
            var annoLabels = anno == null ? null : anno.getFirst().iterator();
            var name = prod.getProdName();
            if (prods.containsKey(name)) {
                prods.get(name).srcExprs.add(prod.to_simple_string());
                continue;
            }
            List<VirtualField> fields = new ArrayStack<>();
            for (int i = 0; i < prod.rhs_length(); i++) {
                var rhs = prod.rhs(i);
                if (rhs.is_action()) continue;
                var symbolPart = (symbol_part) rhs;
                var label = annoLabels == null ? rhs.label() : annoLabels.next();
                if (label == null && !symbolPart.isInline()) continue;
                var symbol = symbolPart.the_symbol();
                if (symbol.is_non_term() && ((non_terminal) symbol).isAnno()) {
                    var subType = initInfo((non_terminal) symbol, fromProd == null ? prod : fromProd, index);
                    if (subType == null) continue;
                    index += subType.count + 1;
                    count += subType.count + 1;
                    fields.add(new VirtualField(label, subType, symbolPart.isInline()));
                } else {
                    var subType = initInfo(symbol);
                    fields.add(new VirtualField(label, subType, symbolPart.isInline()));
                }
            }
            var newProd = new VirtualProduction(name, fields);
            newProd.srcExprs.add(prod.to_simple_string());
            prods.put(name, newProd);
        }
        if (!nt.isLaAnno() && nt.isAnno() && count == 0) return null;
        type.count = count;
        type.prods = List.copyOf(prods.values());
        type.isAnno = fromProd != null;
        type.className = type.isAnno ? emit.getAnnoExprName((non_terminal) fromProd.lhs().the_symbol(), fromProd, index) : nt.astClassName();
        return type;
    }

    public static void generate(PrintWriter writer, VirtualType info, boolean isInner) throws internal_error {
        throw new AssertionError();
//        if (!isInner) {
//            emit.emit_package(writer);
//            writer.println("import java.util.*;");
//            writer.println("import java_cup.runtime.IAstNode;");
//            writer.println();
//
//            writer.println("@SuppressWarnings({");
//            writer.println("  \"SpellCheckingInspection\",");
//            writer.println("  \"EnhancedSwitchMigration\",");
//            writer.println("  \"UnnecessaryLocalVariable\",");
//            writer.println("  \"RedundantSuppression\"");
//            writer.println("})");
//            writer.println("public class " + info.className + " implements IAstNode {");
//        } else {
//            writer.println("  public static class " + info.className + " implements IAstNode {");
//        }
//        writer.println();
//        String prefix = isInner ? "  " : "";
//        var allGetters = info.allGetters();
//        // Generating getters
//        for (VirtualField field : allGetters) {
//            var label = field.label;
//            var type = field.type;
//            if (isExistenceVar(label)) {
//                writer.println(prefix + "  public boolean " + label + "() {");
//                writer.println(prefix + "    return false;");
//                writer.println(prefix + "  }");
//                writer.println();
//            } else {
//                writer.println(prefix + "  public boolean has" + castToStName(label) + "() {");
//                writer.println(prefix + "    return false;");
//                writer.println(prefix + "  }");
//                writer.println();
//                writer.println(prefix + "  public " + type + " get" + castToStName(label) + "() {");
//                switch (type) {
//                    case "byte": case "short": case "int": case "long": case "float": case "double":
//                        writer.println(prefix + "    return 0;");
//                        break;
//                    case "boolean":
//                        writer.println(prefix + "    return false;");
//                        break;
//                    case "char":
//                        writer.println(prefix + "    return '\\0';");
//                        break;
//                    default:
//                        writer.println(prefix + "    return null;");
//                        break;
//                }
//                writer.println(prefix + "  }");
//                writer.println();
//            }
//        }
//        // Generating hasLabel method
//        writer.println(prefix + "  @Override");
//        writer.println(prefix + "  public final boolean hasLabel(String label) {");
//        if (allGetters.isEmpty()) {
//            writer.println(prefix + "    return false;");
//        } else if (allGetters.size() == 1) {
//            var label = allGetters.iterator().next().label;
//            if (isExistenceVar(label)) {
//                writer.println(prefix + "    return \"" + label  + "\".equals(label) && " + label + "();");
//            } else {
//                writer.println(prefix + "    return \"" + label  + "\".equals(label) && has" + castToStName(label) + "();");
//            }
//        } else {
//            writer.println(prefix + "    switch (label) {");
//            for (VirtualField field : allGetters) {
//                var label = field.label;
//                writer.println(prefix + "      case \"" + label + "\":");
//                if (isExistenceVar(label)) {
//                    writer.println(prefix + "        return " + label + "();");
//                } else {
//                    writer.println(prefix + "        return has" + castToStName(label) + "();");
//                }
//            }
//            writer.println(prefix + "      default: return false;");
//            writer.println(prefix + "    }");
//        }
//        writer.println(prefix + "  }");
//        writer.println();
//        // Generating getByLabel method
//        var commonGetters = allGetters.stream()
//            .filter(it -> !isExistenceVar(it.label))
//            .collect(Collectors.toList());
//        writer.println(prefix + "  public final Object getByLabel(String label) {");
//        if (commonGetters.isEmpty()) {
//            writer.println(prefix + "    return null;");
//        } else if (commonGetters.size() == 1) {
//            var label = commonGetters.get(0).label;
//            writer.println(prefix + "    return \"" + label + "\".equals(label) ? get" + castToStName(label) + "() : null;");
//        } else {
//            writer.println(prefix + "    switch (label) {");
//            for (var label : commonGetters) {
//                writer.println(prefix + "      case \"" + label.label + "\":");
//                writer.println(prefix + "        return get" + castToStName(label.label) + "();");
//            }
//            writer.println(prefix + "      default: return null;");
//            writer.println(prefix + "    }");
//        }
//        writer.println(prefix + "  }");
//        writer.println();
//        // Generating static factory methods
//        for (var prod : info.prods) {
//            writer.print(prefix + "  public static " + prod.name + " build" + prod.name + "(");
//            boolean isFirst = true;
//            for (VirtualField field : prod.allGetters()) {
//                if (isExistenceVar(field.label)) continue;
//                if (isFirst) {
//                    isFirst = false;
//                    writer.println();
//                } else {
//                    writer.println(',');
//                }
//                writer.print(prefix + "    " + field.type + ' ' + field.label);
//            }
//            if (!isFirst) {
//                writer.println();
//                writer.print(prefix + "  ");
//            }
//            writer.println(") {");
//            writer.println(prefix + "    " + prod.name + " result = new " + prod.name + "();");
//            for (VirtualField field : prod.allGetters()) {
//                if (isExistenceVar(field.label)) continue;
//                writer.println(prefix + "    result." + field.label + " = " + field.label + ';');
//            }
//            writer.println(prefix + "    return result;");
//            writer.println(prefix + "  }");
//            writer.println();
//        }
//        // generate class for each virtual production
//        for (var prod : info.prods) {
//            for (String text : prod.srcExprs) {
//                writer.println(prefix + "  // " + text);
//            }
//            writer.println(prefix + "  public static final class " + prod.name + " extends " + info.className + " {");
//            writer.println();
//            var hasField = false;
//            for (var field : prod.items) {
//                if (isExistenceVar(field.label)) continue;
//                hasField = true;
//                writer.println(prefix + "    private " + field.type + " " + field.label + ';');
//            }
//            if (hasField) {
//                writer.println();
//            }
//            for (var field : prod.allGetters()) {
//                if (isExistenceVar(field.label)) {
//                    writer.println(prefix + "    @Override");
//                    writer.println(prefix + "    public boolean " + field.label + "() {");
//                    if (field.fromLabel == null) {
//                        writer.println(prefix + "      return true;");
//                    } else {
//                        writer.println(prefix + "      return " + field.fromLabel + '.' + field.subLabel + "();");
//                    }
//                    writer.println(prefix + "    }");
//                } else {
//                    writer.println(prefix + "    @Override");
//                    writer.println(prefix + "    public boolean has" + castToStName(field.label) + "() {");
//                    if (field.fromLabel == null) {
//                        writer.println(prefix + "      return true;");
//                    } else {
//                        writer.println(prefix + "      return " + field.fromLabel + ".has" + castToStName(field.subLabel) + "();");
//                    }
//                    writer.println(prefix + "    }");
//                    writer.println();
//                    writer.println(prefix + "    @Override");
//                    writer.println(prefix + "    public " + field.type + " get" + castToStName(field.label) + "() {");
//                    if (field.fromLabel == null) {
//                        writer.println(prefix + "      return " + field.label + ';');
//                    } else {
//                        writer.println(prefix + "      return " + field.fromLabel + ".get" + castToStName(field.subLabel) + "();");
//                    }
//                    writer.println(prefix + "   }");
//                }
//                writer.println();
//            }
//            writer.println(prefix + "  }");
//            writer.println();
//        }
//        // generate class for each inline non-terminal
//        if (!isInner) {
//            List<VirtualType> deque = new LinkedList<>(info.subTerms);
//            while (!deque.isEmpty()) {
//                VirtualType subInfo = deque.remove(0);
//                generate(writer, subInfo, true);
//                deque.addAll(subInfo.subTerms);
//            }
//        }
//        writer.println(prefix + '}');
    }

    public static class VirtualType {

        public String className;
        public List<VirtualProduction> prods;
        public int count = 0;
        public boolean isAnno = false;

    }

    public static class VirtualProduction {

        public final String name;
        public final List<VirtualField> items;
        public final List<String> srcExprs = new ArrayList<>();

        public VirtualProduction(String name, List<VirtualField> items) {
            this.name = name;
            this.items = Collections.unmodifiableList(items);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            VirtualProduction that = (VirtualProduction) o;
            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

    }

    public static class VirtualField {

        public final String label;
        public final VirtualType type;
        public final boolean isInline;

        public VirtualField(String label, VirtualType type, boolean isInline) {
            this.label = label;
            this.type = type;
            this.isInline = isInline;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            VirtualField that = (VirtualField) o;
            return label.equals(that.label);
        }

        @Override
        public int hashCode() {
            return label.hashCode();
        }

    }

}