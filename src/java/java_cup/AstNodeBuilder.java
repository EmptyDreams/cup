package java_cup;

import java_cup.runtime.ArrayStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AstNodeBuilder {

    private AstNodeBuilder() {}

    private static final Map<non_terminal, VirtualType> typeCache = new HashMap<>();

    public static VirtualType buildGraph(symbol sym) throws internal_error {
        if (sym.is_non_term() && ("IAstNode".equals(sym.stack_type()))) {
            return buildGraph((non_terminal) sym, null, -1);
        } else {
            return VirtualType.ofBasic(sym.astClassName());
        }
    }

    private static VirtualType buildGraph(non_terminal nt, Production fromProd, int index) throws internal_error {
        if (nt.isAnno() && fromProd == null) return null;
        if (typeCache.containsKey(nt)) return typeCache.get(nt);
        var type = new VirtualType(true);
        if (type.isAnno) {
            type.className = emit.getAnnoExprName((non_terminal) fromProd.lhs().the_symbol(), fromProd, index);
        } else {
            type.className = nt.astClassName();
        }
        if (!nt.isAnno()) typeCache.put(nt, type);
        Map<String, VirtualProduction> prods = new HashMap<>();
        if (nt.isOptBox()) {
            var subSymbol = nt.getOptContent();
            VirtualType result;
            if (subSymbol.is_non_term()) {
                result = buildGraph((non_terminal) subSymbol, fromProd, index);
            } else {
                result = buildGraph(subSymbol);
            }
            return result;
        } else if (nt.isListBox()) {
            var subSymbol = nt.getListElementContent();
            VirtualType result;
            if (subSymbol.is_non_term()) {
                result = buildGraph((non_terminal) subSymbol, fromProd, index);
            } else {
                result = buildGraph(subSymbol);
            }
            if (result == null) return null;
            result.castToBox();
            return result.toList();
        }
        var annoItor = nt.isAnno() ? non_terminal.getAnnoLabelAndAction(fromProd).iterator() : null;
        for (Production prod : nt.productions()) {
            if (prod.hasTailAction()) continue;
            var name = prod.getProdName();
            if (prods.containsKey(name)) {
                prods.get(name).srcExprs.add(prod.to_simple_string());
                continue;
            }
            // If the current symbol is an anonymous symbol, read label and action through annoItor
            var annoInfo = annoItor == null ? null : annoItor.next();
            var annoLabelList = annoInfo == null ? null : annoInfo.getLabelList();
            if (annoInfo != null && annoInfo.getAction() != null) continue;
            List<VirtualField> fields = new ArrayStack<>();
            int annoCount = 0;
            for (int i = 0; i < prod.rhs_length(); i++) {
                var rhs = prod.rhs(i);
                if (rhs.is_action()) continue;
                var symbolPart = (symbol_part) rhs;
                var symbol = symbolPart.the_symbol();
                String label = annoLabelList == null ? symbolPart.label() : annoLabelList.get(i);
                // Skip symbols without label and not inline
                if (label == null && !symbolPart.isInline()) continue;
                var subNt = symbol.is_non_term() ? (non_terminal) symbol : null;
                if (subNt != null && subNt.isAnno()) {
                    var annoIndex = annoCount++;
                    var subType = buildGraph(subNt, prod, annoIndex);
                    if (subType == null) continue;
                    fields.add(new VirtualField(label, subType, symbolPart));
                } else {
                    var subType = buildGraph(symbol);
                    fields.add(new VirtualField(label, subType, symbolPart));
                }
            }
            var newProd = new VirtualProduction(name, fields);
            newProd.srcExprs.add(prod.to_simple_string());
            prods.put(name, newProd);
        }
        if (!nt.isLaAnno() && nt.isAnno()) return null;
        type.prods = List.copyOf(prods.values());
        type.isAnno = fromProd != null;
        return type;
    }

    public static VirtualClass buildClass(VirtualType vtype) {
        var clazz = new VirtualClass(vtype.className);
        clazz.markSuper("IAstNode");
        var allMethodField = vtype.prods.stream()
            .flatMap(it -> it.fields.stream())
            .distinct()
            .flatMap(VirtualField::allSubFields)
            .collect(Collectors.toList());
        for (VirtualField field : allMethodField) {
            clazz.addMethod(new VirtualMethod(
                emit.joinName("has", field.joinLabel()),
                "boolean",
                Collections.emptyList(),
                List.of("return false;")
            ));
        }
        for (VirtualField field : allMethodField) {
            if (field.isExistCheck()) continue;
            clazz.addMethod(new VirtualMethod(
                emit.joinName("get", field.joinLabel()),
                field.type.toString(),
                Collections.emptyList(),
                List.of("return " + field.type.getDefaultExpr() + ';')
            ));
        }
        // build hasLabel
        if (allMethodField.isEmpty()) {
            clazz.addMethod(new VirtualMethod(
                "hasLabel",
                "boolean",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                List.of("return false;")
            ).withAnnotation("@Override").markFinal());
        } else if (allMethodField.size() == 1) {
            var field = allMethodField.get(0);
            clazz.addMethod(new VirtualMethod(
                "hasLabel",
                "boolean",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                List.of(
                    "return \"" + field.joinLabel() + "\".equals(label) && "
                        + emit.joinName("has", field.joinLabel()) + "();"
                )
            ).withAnnotation("@Override").markFinal());
        } else {
            List<String> exprs = new ArrayList<>(allMethodField.size() + 3);
            exprs.add("switch (label) {");
            for (VirtualField field : allMethodField) {
                exprs.add(
                    "  case \"" + field.joinLabel() + "\": return "
                        + emit.joinName("has", field.joinLabel()) + "();"
                );
            }
            exprs.add("  default: return false;");
            exprs.add("}");
            clazz.addMethod(new VirtualMethod(
                "hasLabel",
                "boolean",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                exprs
            ).withAnnotation("@Override").markFinal());
        }
        // build getByLabel
        if (allMethodField.isEmpty()) {
            clazz.addMethod(new VirtualMethod(
                "getByLabel",
                "Object",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                List.of("return null;")
            ).withAnnotation("@Override").markFinal());
        } else if (allMethodField.size() == 1) {
            var field = allMethodField.get(0);
            clazz.addMethod(new VirtualMethod(
                "getByLabel",
                "Object",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                List.of(
                    "return \"" + field.joinLabel() + "\".equals(label) ? "
                        + emit.joinName("get", field.joinLabel()) + "() : null;"
                )
            ).withAnnotation("@Override").markFinal());
        } else {
            List<String> exprs = new ArrayList<>(allMethodField.size() + 3);
            exprs.add("switch (label) {");
            for (VirtualField field : allMethodField) {
                exprs.add(
                    "  case \"" + field.joinLabel() + "\": return "
                        + emit.joinName("get", field.joinLabel()) + "();"
                );
            }
            exprs.add("  default: return null;");
            exprs.add("}");
            clazz.addMethod(new VirtualMethod(
                "getByLabel",
                "Object",
                List.of(new VirtualField("label", VirtualType.ofBasic("String"), 0)),
                exprs
            ).withAnnotation("@Override").markFinal());
        }
        for (VirtualProduction prod : vtype.prods) {
            var allSubFields = prod.fields.stream()
                .flatMap(VirtualField::allSubFields)
                .collect(Collectors.toList());
            if (allSubFields.isEmpty()) continue;
            int basicExistCount = (int) prod.fields.stream()
                .filter(it -> it.isOptBox() && it.type.isBasic())
                .count();
            // build factory method
            var factoryExprs = new ArrayList<String>();
            factoryExprs.add("var obj = new " + prod.name + "();");
            if (basicExistCount > 0) {
                if (basicExistCount <= 32) {
                    factoryExprs.add("int mask = 0;");
                } else if (basicExistCount <= 64) {
                    factoryExprs.add("long mask = 0;");
                } else {
                    factoryExprs.add("var mask = obj." + emit.pre("mask"));
                }
            }
            int basicIndex = 0;
            for (VirtualField field : prod.fields) {
                String getter = "obj." + field.label + " = " + field.label + '.' + emit.buildSymGetter(field.type.className) + ';';
                if (field.isOptBox() && field.type.isBasic()) {
                    int index = ++basicIndex;
                    factoryExprs.add("if (" + field.label + " != null) {");
                    if (basicExistCount <= 32) {
                        factoryExprs.add("  mask |= 0x" + Integer.toHexString(index) + ";");
                    } else if (basicExistCount <= 64) {
                        factoryExprs.add("  mask |= 0x" + Long.toHexString(index) + "L;");
                    } else {
                        factoryExprs.add("  mask.set(" + index + ");");
                    }
                    factoryExprs.add("  " + getter);
                    factoryExprs.add("}");
                } else {
                    factoryExprs.add(getter);
                }
            }
            if (basicExistCount > 0) {
                factoryExprs.add("obj." + emit.pre("mask") + " = mask;");
            }
            factoryExprs.add("return obj;");
            // continue build class
            clazz.addMethod(
                new VirtualMethod(
                    "build" + prod.name,
                    prod.name,
                    prod.fields.stream().map(
                        it -> new VirtualField(
                            it.label,
                            VirtualType.ofBasic("Symbol"),
                            0
                        )
                    ).collect(Collectors.toList()),
                    factoryExprs
                ).markStatic()
            );
            var subClass = new VirtualClass(prod.name)
                .markStatic()
                .markFinal();
            subClass.markParent(clazz.name);
            // build field
            prod.fields.forEach(subClass::addField);
            if (basicExistCount != 0) {
                subClass.markMask(basicExistCount);
            }
            // build getter
            allSubFields.stream()
                .map(VirtualField::buildGetter)
                .filter(Objects::nonNull)
                .map(method -> method.withAnnotation("@Override"))
                .forEach(subClass::addMethod);
            // build checker
            for (VirtualField field : allSubFields) {
                String expr;
                if (field.fromField != null) {
                    var fromField = field.fromField;
                    if (fromField.isOptBox()) {
                        expr = "return " + fromField.label + " != null && "
                            + fromField.label + '.'
                            + emit.joinName("has", field.label) + "();";
                    } else {
                        expr = "return " + fromField.label + '.'
                            + emit.joinName("has", field.label) + "();";
                    }
                } else if (field.isOptBox() && field.type.isBasic()) {
                    var index = ++basicIndex;
                    if (basicExistCount <= 32) {
                        expr = "return (" + emit.pre("mask") + " & 0x" + Integer.toHexString(1 << (index - 1)) + ") != 0;";
                    } else if (basicExistCount <= 64) {
                        expr = "return (" + emit.pre("mask") + " & 0x" + Long.toHexString(1L << (index - 1)) + "L) != 0;";
                    } else {
                        expr = "return " + emit.pre("mask") + ".get(" + index + ");";
                    }
                    subClass.addMethod(new VirtualMethod(
                        emit.joinName("mark", field.joinLabel()),
                        "void",
                        Collections.emptyList(),
                        List.of(
                            basicExistCount <= 64 ?
                                emit.pre("mask") + " |= " + index + ';' :
                                emit.pre("mask") + ".set(" + index + ");"
                        )
                    ));
                } else if (field.isOptBox()) {
                    expr = "return " + field.label + " != null;";
                } else {
                    expr = "return true;";
                }
                subClass.addMethod(new VirtualMethod(
                    emit.joinName("has", field.joinLabel()),
                    "boolean",
                    Collections.emptyList(),
                    List.of(expr)
                ).withAnnotation("@Override"));
            }
            clazz.addClass(subClass);
        }
        return clazz;
    }
    
    public static class VirtualType {

        public final boolean isAstNode;

        public String className;
        public List<VirtualProduction> prods;
        public boolean isAnno = false;

        public VirtualType(boolean isAstNode) {
            this.isAstNode = isAstNode;
        }

        private List<VirtualField> allFields;

        public List<VirtualField> allFields() {
            if (allFields != null) return allFields;
            allFields = prods.stream()
                .flatMap(prod -> prod.fields.stream())
                .distinct()
                .collect(Collectors.toList());
            return allFields;
        }

        public boolean isBasic() {
            switch (className) {
                case "byte":
                case "short":
                case "int":
                case "long":
                case "float":
                case "double":
                case "char":
                case "boolean":
                    return true;
                default:
                    return false;
            }
        }

        public String getDefaultExpr() {
            switch (className) {
                case "byte":
                case "short":
                case "int":
                case "long":
                case "float":
                case "double":
                    return "0";
                case "char":
                    return "'\\0'";
                case "boolean":
                    return "false";
                default:
                    return "null";
            }
        }

        public void castToBox() {
            className = emit.boxType(className);
        }

        @Override
        public String toString() {
            return className;
        }

        private VirtualType _innerType;

        public VirtualType toList() {
            var type = new VirtualType(false);
            type.className = "List<" + className + '>';
            type.prods = Collections.emptyList();
            type._innerType = this;
            return type;
        }

        public Stream<VirtualType> types() {
            if (_innerType != null) {
                return Stream.of(this, _innerType);
            }
            return Stream.of(this);
        }

        private static final Map<String, VirtualType> basicTypeCache = new HashMap<>();

        public static VirtualType ofBasic(String name) {
            return basicTypeCache.computeIfAbsent(name, k -> {
                var type = new VirtualType(false);
                type.className = k;
                type.prods = Collections.emptyList();
                return type;
            });
        }

    }

    public static class VirtualProduction {

        public final String name;
        public final List<VirtualField> fields;
        public final List<String> srcExprs = new ArrayList<>();

        public VirtualProduction(String name, List<VirtualField> fields) {
            this.name = name;
            this.fields = Collections.unmodifiableList(fields);
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

    public static class VirtualField implements Comparable<VirtualField> {

        public final String label;
        public final VirtualType type;
        private final production_part part;
        private final int mask;

        private VirtualField fromField;

        public VirtualField(String label, VirtualType type, production_part part) {
            this.label = label;
            this.type = type;
            this.part = part;
            this.mask = 0;
        }

        private VirtualField(String label, VirtualType type, int mask) {
            this.label = label;
            this.type = type;
            this.part = null;
            this.mask = mask;
        }

        public String joinLabel() {
            return fromField == null ? label : emit.joinName(fromField.label, label);
        }

        public boolean isInline() {
            return (mask & 0b1) != 0 || (part != null && !part.is_action() && ((symbol_part) part).isInline());
        }

        public boolean isExistCheck() {
            return (mask & 0b10) != 0 || (part != null && part.isExistCheck());
        }

        public boolean isOptBox() {
            return (mask & 0b100) != 0 || (part != null && !part.is_action() && ((symbol_part) part).the_symbol().isOptBox());
        }

        public VirtualMethod buildGetter() {
            if (isExistCheck() || isInline()) return null;
            if (fromField != null) {
                String expr;
                if (fromField.isOptBox()) {
                    var defValue = type.getDefaultExpr();
                    expr = "return " + fromField.label + " == null ? " + defValue + " : "
                        + fromField.label + '.' + emit.joinName("get", label) + "();";
                } else {
                    expr = "return " + fromField.label + '.' + emit.joinName("get", label) + "();";
                }
                return new VirtualMethod(
                    emit.joinName("get", joinLabel()),
                    type.toString(),
                    Collections.emptyList(),
                    List.of(expr)
                );
            }
            return new VirtualMethod(
                emit.joinName("get", label),
                type.toString(),
                Collections.emptyList(),
                List.of("return " + label + ';')
            );
        }

        public VirtualField createSub(VirtualField fromField) {
            int newMask = 0;
            if (part == null) {
                newMask = mask;
            } else {
                if (isInline()) newMask |= 0b1;
                if (isExistCheck()) newMask |= 0b10;
                if (isOptBox()) newMask |= 0b100;
            }
            var result = new VirtualField(label, type, newMask);
            result.fromField = fromField;
            return result;
        }

        public String toCodeString() {
            return "private " + type + ' ' + label + ';';
        }

        public Stream<VirtualField> allSubFields() {
            if (!isInline()) return Stream.of(this);
            return type.allFields().stream()
                .flatMap(VirtualField::allSubFields)
                .map(field -> field.createSub(this));
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

        @Override
        public int compareTo(VirtualField o) {
            return label.compareTo(o.label);
        }

    }

    public static class VirtualClass {

        private final String name;
        private final List<VirtualField> fields = new ArrayList<>();
        private final List<VirtualMethod>  methods = new ArrayList<>();
        private final List<VirtualClass> subclasses = new ArrayList<>();

        private boolean isStatic = false;
        private boolean isFinal = false;
        private String parentClass = null;
        private String superInterface = null;

        private String maskMode = "none";

        public VirtualClass(String name) {
            this.name = name;
        }

        public void addField(VirtualField field) {
            fields.add(field);
        }

        public void addMethod(VirtualMethod method) {
            methods.add(method);
        }

        public void addClass(VirtualClass clazz) {
            subclasses.add(clazz);
        }

        public void markParent(String parentClass) {
            this.parentClass = parentClass;
        }

        public void markSuper(String superInterface) {
            this.superInterface = superInterface;
        }

        public void markMask(int count) {
            if (count <= 32) maskMode = "int";
            else if (count <= 64) maskMode = "long";
            else maskMode = "BigSet";
        }

        public VirtualClass markStatic() {
            isStatic = true;
            return this;
        }

        public VirtualClass markFinal() {
            isFinal = true;
            return this;
        }

        public String toCodeString(int indent) {
            String indentText = "  ".repeat(indent);
            StringBuilder builder = new StringBuilder();
            builder.append(indentText).append("public ");
            if (isStatic) builder.append("static ");
            if (isFinal) builder.append("final ");
            builder.append("class ").append(name);
            if (parentClass != null) builder.append(" extends ").append(parentClass);
            if (superInterface != null) builder.append(" implements ").append(superInterface);
            builder.append(" {\n\n");
            if (!fields.isEmpty()) {
                for (VirtualField field : fields) {
                    builder.append(indentText).append("  ")
                        .append(field.toCodeString())
                        .append('\n');
                }
                if (!"none".equals(maskMode)) {
                    builder.append(indentText).append("  ")
                        .append(maskMode)
                        .append(' ')
                        .append(emit.pre("mask"))
                        .append(';');
                }
                builder.append('\n');
            }
            if (!methods.isEmpty()) {
                for (VirtualMethod method : methods) {
                    builder.append(method.toCodeString(indent + 1))
                        .append('\n');
                }
                builder.append('\n');
            }
            if (!subclasses.isEmpty()) {
                for (VirtualClass subclass : subclasses) {
                    builder.append(subclass.toCodeString(indent + 1))
                        .append('\n');
                }
                builder.append('\n');
            }
            builder.append(indentText).append('}');
            return builder.toString();
        }

    }

    public static class VirtualMethod {

        public final String name;
        public final String returnType;
        public final List<VirtualField> params;
        public final List<String> exprs;

        private boolean isFinal = false;
        private  boolean isStatic = false;
        private final List<String> annotations = new ArrayList<>();

        public VirtualMethod(String name, String returnType, List<VirtualField> params, List<String> exprs) {
            this.name = name;
            this.returnType = returnType;
            this.params = Collections.unmodifiableList(params);
            this.exprs = Collections.unmodifiableList(exprs);
        }

        public VirtualMethod markFinal() {
            isFinal = true;
            return this;
        }

        public VirtualMethod markStatic() {
            isStatic = true;
            return this;
        }

        public VirtualMethod withAnnotation(String annotation) {
            annotations.add(annotation);
            return this;
        }

        public String toCodeString(int indent) {
            String indentText = "  ".repeat(indent);
            var builder = new StringBuilder();
            for (String annotation : annotations) {
                builder.append(indentText).append(annotation).append('\n');
            }
            builder.append(indentText)
                .append("public ");
            if (isStatic) builder.append("static ");
            if (isFinal) builder.append("final ");
            builder.append(returnType).append(' ').append(name).append('(');
            if (!params.isEmpty()) {
                boolean isFirst = true;
                for (VirtualField field : params) {
                    if (isFirst) isFirst = false;
                    else builder.append(',');
                    builder.append('\n').append(indentText).append("  ")
                        .append(field.type).append(' ').append(field.label);
                }
                builder.append('\n').append(indentText);
            }
            builder.append(") {\n");
            for (String expr : exprs) {
                builder.append(indentText).append("  ").append(expr);
                builder.append('\n');
            }
            builder.append(indentText).append("}\n");
            return builder.toString();
        }

    }

}