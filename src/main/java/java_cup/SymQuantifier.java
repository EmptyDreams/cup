package java_cup;

import java.util.Collections;
import java.util.List;

public class SymQuantifier {

    private final boolean isList;
    private final boolean allowEmpty;
    private final List<production_part> partList;
    private final boolean allowTail;

    public SymQuantifier(
        List<production_part> partList,
        boolean allowEmpty,
        boolean allowTail
    ) {
        isList = true;
        this.allowEmpty = allowEmpty;
        this.partList = partList == null ? Collections.emptyList() : partList;
        this.allowTail = allowTail;
    }

    public SymQuantifier() {
        isList = false;
        allowEmpty = true;
        partList = Collections.emptyList();
        allowTail = false;
    }

    public boolean isList() {
        return isList;
    }

    public boolean isAllowEmpty() {
        return allowEmpty;
    }

    public List<production_part> getPartList() {
        return partList;
    }

    public boolean isAllowTail() {
        return allowTail;
    }

}