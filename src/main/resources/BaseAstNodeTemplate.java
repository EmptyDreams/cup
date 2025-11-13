import java_cup.runtime.IAstNode;

public final class $ClassName$ extends IAstNode {

    private final $type$ value;
    private final java_cup.runtime.symbol.Location location;

    public $ClassName$($type$ value, java_cup.runtime.symbol.Location location) {
        this.value = value;
        this.location = location;
    }

    public $type$ get$Type$() {
        return value;
    }

    @Override
    public java_cup.runtime.symbol.Location getLocation() {
        return location;
    }

    @Override
    public boolean hasLabel(String label) {
        return false;
    }

    @Override
    public IAstNode getByLabel(String label) {
        return null;
    }

    @Override
    public int hashCode() {
        return value + location.hashCode() * 31;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        var that = ($ClassName$) o;
        return value == that.value && location.equals(that.location);
    }

    @Override
    public String toString() {
        return "$Type$(" + value + ")@" + location;
    }

}