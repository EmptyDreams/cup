package java_cup.ast;

import java_cup.GrammarSymbol;
import java_cup.Main;
import java_cup.emit;
import java_cup.non_terminal;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VirtualType {

    private static final Map<String, VirtualType> basicTypeCache = new HashMap<>();
    private static final VirtualType TYPE_SYMBOL = ofName("Symbol");
    private static final VirtualType TYPE_POSITION = ofName(Main.customPositionClass);
    private static final VirtualType TYPE_INT = ofName("int");

    public static VirtualType ofBasic(String name, int symId) {
        return basicTypeCache.computeIfAbsent(name, k -> {
            var type = new VirtualType(symId, false, k);
            var className = k.replaceAll("[<, ]+", "_")
                .replace(">", "");
            type.className = GrammarSymbol.getNtNodeClassName(className, true);
            type.prods = Collections.emptyList();
            return type;
        });
    }

    private static VirtualType ofName(String name) {
        var type = new VirtualType(-1, false, "");
        type.className = name;
        type.prods = Collections.emptyList();
        return type;
    }

    public static Iterable<VirtualType> basicTypeIterable() {
        return basicTypeCache.values();
    }

    private final int symId;
    public final boolean isAstNode;
    private final String basicName;

    public String className;
    public List<VirtualProduction> prods;
    public boolean isAnno = false;

    public VirtualType(int symId, boolean isAstNode, String basicName) {
        this.symId = symId;
        this.isAstNode = isAstNode;
        this.basicName = basicName;
    }

    public String getRealName() {
        return isBasic() ? basicName : className;
    }

    public boolean isBasic() {
        return basicName != null && !basicName.isEmpty();
    }

    public int getSymId() {
        return symId;
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

    public VirtualClass toVirtualClass() {
        var clazz = new VirtualClass(className);
        clazz.markParent("AstNode");
        var allMethodField = prods.stream()
            .flatMap(it -> it.fields.stream())
            .distinct()
            .flatMap(VirtualField::allSubFields)
            .collect(Collectors.toList());
        clazz.addMethod(new VirtualMethod(
            "getLocation", Main.customPositionClass, Collections.emptyList(),
            List.of("return " + Main.customPositionClass + ".NO_LOCATION;")
        ).withAnnotation("@Override"));
        if (symId == -1) {
            clazz.addMethod(
                new VirtualMethod(
                    "getNodeName", "String",
                    Collections.emptyList(),
                    List.of("return null;")
                ).markFinal().withAnnotation("@Override")
            );
            clazz.addMethod(
                new VirtualMethod(
                    "toString", "String",
                    Collections.emptyList(),
                    List.of("return \"" + className + "\" + getLocation();")
                ).markFinal().withAnnotation("@Override")
            );
        } else {
            var nodeName = non_terminal.find(symId).name();
            clazz.addMethod(
                new VirtualMethod(
                    "getNodeName", "String",
                    Collections.emptyList(),
                    List.of("return \"" + nodeName + "\";")
                ).markFinal().withAnnotation("@Override")
            );
            clazz.addMethod(
                new VirtualMethod(
                    "toString", "String",
                    Collections.emptyList(),
                    List.of("return \"" + nodeName + "\" + getLocation();")
                ).markFinal().withAnnotation("@Override")
            );
        }
        for (VirtualField field : allMethodField) {
            var method = new VirtualMethod(
                emit.joinName("has", field.joinLabel()),
                "boolean",
                Collections.emptyList(),
                List.of("return false;")
            );
            clazz.addMethod(method);
        }
        for (VirtualField field : allMethodField) {
            if (field.isExistCheck()) continue;
            var method = new VirtualMethod(
                emit.joinName("get", field.joinLabel()),
                field.type.className,
                Collections.emptyList(),
                List.of("return null;")
            );
            clazz.addMethod(method);
        }
        // build hasLabel
        switch (allMethodField.size()) {
            case 0: {
                var method = new VirtualMethod(
                    "hasLabel",
                    "boolean",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    List.of("return false;")
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
            case 1: {
                var field = allMethodField.get(0);
                var method = new VirtualMethod(
                    "hasLabel",
                    "boolean",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    List.of(
                        "return \"" + field.joinLabel() + "\".equals(label) && "
                            + emit.joinName("has", field.joinLabel()) + "();"
                    )
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
            default: {
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
                var method = new VirtualMethod(
                    "hasLabel",
                    "boolean",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    exprs
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
        }
        // build getByLabel
        switch (allMethodField.size()) {
            case 0: {
                var method = new VirtualMethod(
                    "getByLabel",
                    "AstNode",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    List.of("return null;")
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
            case 1: {
                var field = allMethodField.get(0);
                var method = new VirtualMethod(
                    "getByLabel",
                    "AstNode",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    List.of(
                        "return \"" + field.joinLabel() + "\".equals(label) ? "
                            + emit.joinName("get", field.joinLabel()) + "() : null;"
                    )
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
            default: {
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
                var method = new VirtualMethod(
                    "getByLabel",
                    "AstNode",
                    List.of(new VirtualField("label", VirtualType.ofName("String"), 0)),
                    exprs
                ).withAnnotation("@Override")
                    .markFinal();
                clazz.addMethod(method);
                break;
            }
        }
        // handle production
        for (var prod : prods) {
            var allSubFields = prod.fields.stream()
                .flatMap(VirtualField::allSubFields)
                .collect(Collectors.toList());
            if (allSubFields.isEmpty()) continue;
            // builder
            var builderParams = new ArrayList<VirtualField>();
            for (int i = 0; i < prod.fields.size(); i++) {
                builderParams.add(new VirtualField(prod.fields.get(i).paramName(i), TYPE_SYMBOL, 0));
            }
            var builderMethod = new VirtualMethod(
                "build" + prod.name,
                prod.name,
                builderParams,
                prod.buildFactoryExprs()
            ).markStatic();
            clazz.addMethod(builderMethod);
            // build inner class
            var innerClass = new VirtualClass(prod.name)
                .markStatic()
                .markFinal();
            innerClass.markParent(clazz.getName());
            // build fields and constructor; an unlabeled spread container is
            // itself never a field: its hoisted sub-fields become real fields
            var constructorParams = new ArrayList<VirtualField>();
            var constructorExprs = new ArrayList<String>();
            for (var field : prod.fields) {
                if (field.label == null) {
                    for (var sub : field.allSubFields().collect(Collectors.toList())) {
                        innerClass.addField(sub.toFinal());
                        constructorParams.add(sub);
                        constructorExprs.add("this." + sub.joinLabel() + " = " + sub.joinLabel() + ';');
                    }
                } else {
                    innerClass.addField(field.toFinal());
                    constructorParams.add(field);
                    constructorExprs.add("this." + field.label + " = " + field.label + ';');
                }
            }
            innerClass.addField(new VirtualField("location", TYPE_POSITION, 0b1000));
            constructorParams.add(new VirtualField("location", TYPE_POSITION, 0));
            constructorExprs.add("this.location = location;");
            var constructor = new VirtualMethod(
                innerClass.getName(), "", constructorParams, constructorExprs
            );
            innerClass.addMethod(constructor);
            // build getter
            allSubFields.stream()
                .map(VirtualField::buildGetter)
                .filter(Objects::nonNull)
                .map(it -> it.withAnnotation("@Override"))
                .forEachOrdered(innerClass::addMethod);
            var positionGetter = new VirtualMethod(
                "getLocation",
                Main.customPositionClass,
                Collections.emptyList(),
                List.of("return location;")
            ).withAnnotation("@Override");
            innerClass.addMethod(positionGetter);
            // build checker
            allSubFields.stream()
                .map(VirtualField::buildChecker)
                .map(it -> it.withAnnotation("@Override"))
                .forEachOrdered(innerClass::addMethod);
            // build getByIndex
            var getByIndexExprs = new ArrayList<String>();
            getByIndexExprs.add("switch (index) {");
            for (int i = 0; i < allSubFields.size(); i++) {
                var field = allSubFields.get(i);
                var getterCall = emit.joinName("get", field.joinLabel()) + "()";
                if (field.isNullable()) {
                    // AstNode.getByIndex documents that absent optional slots
                    // yield null (skipped by AstNodeIterator), not an entry
                    // with a null value.
                    getByIndexExprs.add(
                        "  case " + i + ": return " + getterCall + " == null ? null : " +
                            "new AbstractMap.SimpleEntry<>(\"" + field.joinLabel() + "\", " + getterCall + ");"
                    );
                } else {
                    getByIndexExprs.add(
                        "  case " + i + ": return new AbstractMap.SimpleEntry<>(\"" + field.joinLabel() + "\", "
                            + getterCall + ");"
                    );
                }
            }
            getByIndexExprs.add("  default: throw new IndexOutOfBoundsException(index);");
            getByIndexExprs.add("}");
            var getByIndexMethod = new VirtualMethod(
                "getByIndex", "Map.Entry<String, AstNode>",
                List.of(new VirtualField("index", TYPE_INT, 0)),
                getByIndexExprs
            ).withAnnotation("@Override");
            innerClass.addMethod(getByIndexMethod);
            var iteratorMethod = new VirtualMethod(
                "iterator", "Iterator<Map.Entry<String, AstNode>>",
                Collections.emptyList(),
                List.of("return new AstNodeIterator(this, " + allSubFields.size() + ");")
            ).withAnnotation("@Override");
            innerClass.addMethod(iteratorMethod);
            clazz.addClass(innerClass);
        }
        return clazz;
    }

    @Override
    public String toString() {
        return className;
    }

    private VirtualType _innerType;

    public VirtualType toList(int symId) {
        var result = ofBasic("List<" + className + '>', symId);
        result._innerType = this;
        return result;
    }

    public Stream<VirtualType> types() {
        if (_innerType != null) {
            return Stream.of(this, _innerType);
        }
        return Stream.of(this);
    }

}