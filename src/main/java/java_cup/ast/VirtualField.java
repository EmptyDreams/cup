package java_cup.ast;

import java_cup.emit;
import java_cup.production_part;
import java_cup.symbol_part;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class VirtualField implements Comparable<VirtualField> {

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

    public VirtualField(String label, VirtualType type, int mask) {
        this.label = label;
        this.type = type;
        this.part = null;
        this.mask = mask;
    }

    public String joinLabel() {
        return fromField == null ? label : emit.joinName(fromField.label, label);
    }

    /**
     * The name of the builder parameter that receives this field's Symbol. An
     * unlabeled spread container carries no label (its fields are hoisted into
     * the parent with an empty prefix, so the container itself never appears in
     * the node API); it is referenced through a synthetic name instead. The '$'
     * character cannot occur in user labels, so the synthetic name never
     * collides with one.
     */
    public String paramName(int index) {
        return label != null ? label : "$" + index;
    }

    public VirtualField toFinal() {
        return new VirtualField(label, type, mask | 0b1000);
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

    public boolean isFinal() {
        return (mask & 0b1000) != 0;
    }

    /**
     * Returns whether the field's value can be absent at runtime, either
     * because the field itself is an opt-box or because it was hoisted from
     * an opt-box via {@code ...} spread.
     */
    public boolean isNullable() {
        return isOptBox() || fromField != null && fromField.isNullable();
    }

    public VirtualMethod buildGetter() {
        if (isExistCheck() || isInline()) return null;
        if (fromField != null && fromField.label != null) {
            // hoisted by a labeled spread: delegate into the container field
            String expr;
            if (fromField.isOptBox()) {
                expr = "return " + fromField.label + " == null ? null : " +
                    fromField.label + '.' + emit.joinName("get", label) + "();";
            } else {
                expr = "return " + fromField.label + '.' + emit.joinName("get", label) + "();";
            }
            return new VirtualMethod(
                emit.joinName("get", joinLabel()),
                type.className,
                Collections.emptyList(),
                List.of(expr)
            );
        }
        // plain field (also: hoisted by an unlabeled spread, which stores the
        // value in its own field instead of delegating)
        return new VirtualMethod(
            emit.joinName("get", joinLabel()),
            type.className,
            Collections.emptyList(),
            List.of("return " + joinLabel() + ';')
        );
    }

    public VirtualMethod buildChecker() {
        if (isInline()) return null;
        if (fromField != null && fromField.label != null) {
            // hoisted by a labeled spread: delegate into the container field
            String expr;
            if (fromField.isOptBox()) {
                expr = "return " + fromField.label + " != null && " +
                    fromField.label + '.' + emit.joinName("has", label) + "();";
            } else {
                expr = "return " + fromField.label + '.' + emit.joinName("has", label) + "();";
            }
            return new VirtualMethod(
                emit.joinName("has", joinLabel()),
                "boolean",
                Collections.emptyList(),
                List.of(expr)
            );
        } else if (isNullable()) {
            return new VirtualMethod(
                emit.joinName("has", joinLabel()),
                "boolean",
                Collections.emptyList(),
                List.of("return " + joinLabel() + " != null;")
            );
        } else {
            return new VirtualMethod(
                emit.joinName("has", joinLabel()),
                "boolean",
                Collections.emptyList(),
                List.of("return true;")
            );
        }
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
        return "private " + (isFinal() ? "final " : "") + type + ' ' + label + ';';
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
        // unlabeled spread containers have no label: compare by identity so
        // that two of them never collapse in label-based deduplication
        if (label == null || that.label == null) return this == o;
        return label.equals(that.label);
    }

    @Override
    public int hashCode() {
        return label == null ? System.identityHashCode(this) : label.hashCode();
    }

    @Override
    public int compareTo(VirtualField o) {
        if (label == null) return o.label == null ? 0 : 1;
        if (o.label == null) return -1;
        return label.compareTo(o.label);
    }

}