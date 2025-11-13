package java_cup.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VirtualMethod {

    public final String name;
    public final String returnType;
    public final List<VirtualField> params;
    public final List<String> exprs;

    private boolean isFinal = false;
    private boolean isStatic = false;
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