package java_cup.ast;

import java_cup.Main;
import java_cup.emit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VirtualProduction {

    public final String name;
    public final List<VirtualField> fields;
    public final List<String> srcExprs = new ArrayList<>();

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
            boolean isMaybeNull = rightPositionIndex[0] && leftPositionIndex[1];
            if (leftPositionIndex[1]) {
                var base = Main.customPositionClass + ' ' + emit.pre("left");
                if (isMaybeNull) {
                    base += " = " + Main.customPositionClass + ".NO_LOCATION;";
                } else {
                    base += ';';
                }
                factoryExprs.add(base);
            }
            if (rightPositionIndex[fields.size() - 2]) {
                var base = Main.customPositionClass + ' ' + emit.pre("right");
                if (isMaybeNull) {
                    base += " = " + Main.customPositionClass + ".NO_LOCATION;";
                } else {
                    base += ';';
                }
                factoryExprs.add(base);
            }
        }
        for (int i = 0; i < fields.size(); i++) {
            VirtualField field = fields.get(i);
            //noinspection ExtractMethodRecommender
            var getterName = emit.buildSymGetter(field.type.getRealName());
            String valueAssignment;
            if (field.type.isBasic()) {
                // For primitive types (here, "primitive" means any type not wrapped in an AST node),
                // directly instantiate the specialized node class for that type
                valueAssignment = field.label + "Node = new " + field.type.className + "(" +
                    field.label + '.' + getterName + ", (" + Main.customPositionClass + ") " + field.label + ".getLocation());";
            } else {
                valueAssignment = field.label + "Node = " + field.label + "." + getterName + ';';
            }
            var positionAssignment = " = (" + Main.customPositionClass + ") " + field.label + ".getLocation();";
            if (field.isOptBox()) {
                factoryExprs.add(field.type.className + ' ' + field.label + "Node = null;");
                factoryExprs.add("if (" + field.label + " != null) {");
                factoryExprs.add("  " + valueAssignment);
            } else {
                factoryExprs.add("var " + valueAssignment);
            }
            if (leftPositionIndex[i]) {
                String prefix = field.isOptBox() ? "  " : "";
                // If the current field is a candidate for the left boundary,
                // attempt to assign its position to the left boundary
                if (i != 0) {
                    // If this is not the first field, it means preceding fields might be null,
                    // so we need to check whether the left boundary has already been assigned
                    factoryExprs.add(prefix + "if (" + emit.pre("left") + " == null)");
                    prefix += "  ";
                } else if (!field.isOptBox()) {
                    prefix += "var ";
                }
                factoryExprs.add(prefix + emit.pre("left") + positionAssignment);
            }
            if (rightPositionIndex[i]) {
                // This differs from the left-boundary logic because we process fields left-to-right;
                // thus, always assigning to the right boundary ensures it ends up as the position of the last non-null field
                String prefix = field.isOptBox() ? "  " : "";
                if ((i == 0 || !rightPositionIndex[i - 1]) && !field.isOptBox()) {
                    prefix += "var ";
                }
                factoryExprs.add(prefix + emit.pre("right") +  positionAssignment);
            }
            if (field.isOptBox()) factoryExprs.add("}");
        }
        if (isNeedSpanPos) {
            factoryExprs.add(
                "var " + emit.pre("pos") + " = " +
                    emit.pre("left") + ".span(" + emit.pre("right") + ");"
            );
        } else {
            factoryExprs.add(
                "var " + emit.pre("pos") + " = (" +
                    Main.customPositionClass + ") " + fields.get(0).label + ".getLocation();"
            );
        }
        // Construct the 'new' statement
        factoryExprs.add("return new " + name + "(");
        for (VirtualField field : fields) {
            factoryExprs.add("  " + field.label + "Node,");
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