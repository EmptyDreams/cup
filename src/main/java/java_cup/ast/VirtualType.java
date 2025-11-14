package java_cup.ast;

import java_cup.GrammarSymbol;
import java_cup.Main;
import java_cup.emit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VirtualType {

    private static final Map<String, VirtualType> basicTypeCache = new HashMap<>();
    private static final VirtualType TYPE_SYMBOL = ofName("Symbol");
    private static final VirtualType TYPE_POSITION = ofName(Main.customPositionClass);
    private static final VirtualType TYPE_INT = ofName("int");

    public static VirtualType ofBasic(String name) {
        return basicTypeCache.computeIfAbsent(name, k -> {
            var type = new VirtualType(false, k);
            var className = k.replaceAll("[<, ]+", "_")
                .replace(">", "");
            type.className = GrammarSymbol.getNtNodeClassName(className);
            type.prods = Collections.emptyList();
            return type;
        });
    }

    private static VirtualType ofName(String name) {
        var type = new VirtualType(false, "");
        type.className = name;
        type.prods = Collections.emptyList();
        return type;
    }

    public static Iterable<VirtualType> basicTypeIterable() {
        return basicTypeCache.values();
    }

    public final boolean isAstNode;
    private final String basicName;

    public String className;
    public List<VirtualProduction> prods;
    public boolean isAnno = false;

    public VirtualType(boolean isAstNode, String basicName) {
        this.isAstNode = isAstNode;
        this.basicName = basicName;
    }

    public String getRealName() {
        return isBasic() ? basicName : className;
    }

    public boolean isBasic() {
        return basicName != null && !basicName.isEmpty();
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

    public void castToBox() {
        className = emit.boxType(className);
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
            var builderMethod = new VirtualMethod(
                "build" + prod.name,
                prod.name,
                prod.fields.stream().map(
                    it -> new VirtualField(it.label, TYPE_SYMBOL, 0)
                ).collect(Collectors.toList()),
                prod.buildFactoryExprs()
            ).markStatic();
            clazz.addMethod(builderMethod);
            // build inner class
            var innerClass = new VirtualClass(prod.name)
                .markStatic()
                .markFinal();
            innerClass.markParent(clazz.getName());
            // build fields
            prod.fields.stream()
                .map(VirtualField::toFinal)
                .forEachOrdered(innerClass::addField);
            innerClass.addField(new VirtualField("location", TYPE_POSITION, 0b1000));
            // build constructor
            var constructorParams = new ArrayList<>(prod.fields);
            constructorParams.add(new VirtualField("location", TYPE_POSITION, 0));
            var constructorExprs = prod.fields.stream()
                .map(it -> "this." + it.label + " = " + it.label + ';')
                .collect(Collectors.toList());
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
                getByIndexExprs.add("  case " + i + ": return " + emit.joinName("get", field.joinLabel()) + "();");
            }
            getByIndexExprs.add("  default: new IndexOutOfBoundsException(index);");
            getByIndexExprs.add("}");
            var getByIndexMethod = new VirtualMethod(
                "getByIndex", "AstNode",
                List.of(new VirtualField("index", TYPE_INT, 0)),
                getByIndexExprs
            ).withAnnotation("@Override");
            innerClass.addMethod(getByIndexMethod);
            clazz.addClass(innerClass);
        }
        return clazz;
    }

    @Override
    public String toString() {
        return className;
    }

    private VirtualType _innerType;

    public VirtualType toList() {
        var name = "List<" + className + '>';
        var type = new VirtualType(false, name);
        type.className = name;
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

}