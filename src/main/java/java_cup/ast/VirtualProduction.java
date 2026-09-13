package java_cup.ast;

import java_cup.Main;
import java_cup.emit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VirtualProduction {

    public final String name;
    public final List<VirtualField> fields;

    public VirtualProduction(String name, List<VirtualField> fields) {
        this.name = name;
        this.fields = Collections.unmodifiableList(fields);
    }

    /**
     * Generates the code statements used inside the factory method for the given virtual production.
     */
    public List<String> buildFactoryExprs() {
        var factoryExprs = new ArrayList<String>();
        var leftPositionIndex = new boolean[fields.size()];
        var rightPositionIndex = new boolean[fields.size()];
        boolean isNeedSpanPos = fields.size() > 1;
        if (isNeedSpanPos) {
            // Determine which symbols can contribute to the leftmost position of the node
            for (int i = 0; i < fields.size(); i++) {
                var field = fields.get(i);
                leftPositionIndex[i] = true;
                if (!field.isOptBox()) {
                    break;
                }
            }
            // Determine which symbols can contribute to the rightmost position of the node
            for (int i = fields.size() - 1; i >= 0; i--) {
                var field = fields.get(i);
                rightPositionIndex[i] = true;
                if (!field.isOptBox()) {
                    break;
                }
            }
            // Pre-declare both boundary variables unconditionally. No assignment below
            // uses "var", so each boundary is declared exactly once regardless of which
            // fields are optional, and the null initializers satisfy the
            // definite-assignment analysis when every boundary candidate is an
            // opt-box whose value may be absent.
            factoryExprs.add(Main.customPositionClass + ' ' + emit.pre("left") + " = null;");
            factoryExprs.add(Main.customPositionClass + ' ' + emit.pre("right") + " = null;");
        }
        for (int i = 0; i < fields.size(); i++) {
            VirtualField field = fields.get(i);
            // unlabeled spread containers carry no label and are referenced by a
            // synthetic parameter name
            String ref = field.paramName(i);
            //noinspection ExtractMethodRecommender
            var getterName = emit.buildSymGetter(field.type.getRealName());
            String valueAssignment;
            if (field.type.isBasic()) {
                // For primitive types (here, "primitive" means any type not wrapped in an AST node),
                // directly instantiate the specialized node class for that type
                valueAssignment = ref + "Node = new " + field.type.className + "(" +
                    ref + '.' + getterName + ", (" + Main.customPositionClass + ") " + ref + ".getLocation());";
            } else {
                valueAssignment = ref + "Node = " + ref + "." + getterName + ';';
            }
            var positionAssignment = " = (" + Main.customPositionClass + ") " + ref + ".getLocation();";
            // fields hoisted by an unlabeled spread: they become real fields of
            // this node, so their values are read out of the container node
            var hoisted = field.isInline() && field.label == null
                ? field.allSubFields().collect(Collectors.toCollection(ArrayList::new))
                : new ArrayList<VirtualField>();
            if (field.isOptBox()) {
                factoryExprs.add(field.type.className + ' ' + ref + "Node = null;");
                for (var sub : hoisted) {
                    factoryExprs.add(sub.type.className + ' ' + sub.joinLabel() + "Node = null;");
                }
                factoryExprs.add("if (!" + ref + ".isNull()) {");
                factoryExprs.add("  " + valueAssignment);
                for (var sub : hoisted) {
                    factoryExprs.add("  " + sub.joinLabel() + "Node = " + ref + "Node." +
                        emit.joinName("get", sub.joinLabel()) + "();");
                }
            } else {
                factoryExprs.add("var " + valueAssignment);
                for (var sub : hoisted) {
                    factoryExprs.add("var " + sub.joinLabel() + "Node = " + ref + "Node." +
                        emit.joinName("get", sub.joinLabel()) + "();");
                }
            }
            if (leftPositionIndex[i]) {
                String prefix = field.isOptBox() ? "  " : "";
                // If the current field is a candidate for the left boundary,
                // attempt to assign its position to the left boundary
                if (i != 0) {
                    // If this is not the first field, it means preceding fields might be absent,
                    // so we need to check whether the left boundary has already been assigned
                    factoryExprs.add(prefix + "if (" + emit.pre("left") + " == null)");
                    prefix += "  ";
                }
                factoryExprs.add(prefix + emit.pre("left") + positionAssignment);
            }
            if (rightPositionIndex[i]) {
                // This differs from the left-boundary logic because we process fields left-to-right;
                // thus, always assigning to the right boundary ensures it ends up as the position of the last present field
                String prefix = field.isOptBox() ? "  " : "";
                factoryExprs.add(prefix + emit.pre("right") + positionAssignment);
            }
            if (field.isOptBox()) factoryExprs.add("}");
        }
        if (isNeedSpanPos) {
            // The boundaries are null only when every field is an opt-box whose value
            // is absent; fall back to the NO_LOCATION sentinel so span stays null-safe.
            factoryExprs.add(
                "var " + emit.pre("pos") + " = (" + emit.pre("left") + " != null ? " + emit.pre("left")
                    + " : " + Main.customPositionClass + ".NO_LOCATION).span("
                    + emit.pre("right") + " != null ? " + emit.pre("right")
                    + " : " + Main.customPositionClass + ".NO_LOCATION);"
            );
        } else {
            factoryExprs.add(
                "var " + emit.pre("pos") + " = (" +
                    Main.customPositionClass + ") " + fields.get(0).paramName(0) + ".getLocation();"
            );
        }
        // Construct the 'new' statement
        factoryExprs.add("return new " + name + "(");
        for (VirtualField field : fields) {
            if (field.label == null) {
                // unlabeled spread container: pass the hoisted values
                for (var sub : field.allSubFields().collect(Collectors.toList())) {
                    factoryExprs.add("  " + sub.joinLabel() + "Node,");
                }
            } else {
                factoryExprs.add("  " + field.label + "Node,");
            }
        }
        factoryExprs.add("  " + emit.pre("pos"));
        factoryExprs.add(");");
        return factoryExprs;
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