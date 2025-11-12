package java_cup;

import java.util.Collections;
import java.util.List;

public class AnnoNtInfo {

    private final non_terminal nt;
    private final int indexInProd;
    private final List<String> labelList;
    private final String action;

    public AnnoNtInfo(non_terminal nt, int indexInProd, List<String> labelList, String action) {
        this.nt = nt;
        this.indexInProd = indexInProd;
        this.labelList = Collections.unmodifiableList(labelList);
        this.action = action;
    }

    public non_terminal getNt() {
        return nt;
    }

    public int getIndexInProd() {
        return indexInProd;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public String getAction() {
        return action;
    }

}