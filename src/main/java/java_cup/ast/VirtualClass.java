package java_cup.ast;

import java.util.ArrayList;
import java.util.List;

public class VirtualClass {

    private final String name;
    private final List<VirtualField> fields = new ArrayList<>();
    private final List<VirtualMethod> methods = new ArrayList<>();
    private final List<VirtualClass> subclasses = new ArrayList<>();

    private boolean isStatic = false;
    private boolean isFinal = false;
    private String parentClass = null;
    private String superInterface = null;

    public VirtualClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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