package java_cup;

import java.util.*;

public class SymbolStateCompression {

    private static final SymbolStateCompression EMPTY = new SymbolStateCompression(Collections.emptyMap(), 0);

    public final Map<String, Integer> map;
    public final int idCount;

    private SymbolStateCompression(Map<String, Integer> map, int idCount) {
        this.map = map;
        this.idCount = idCount;
    }

    public boolean isEmpty() {
        return idCount == 0;
    }

    public Integer get(String key) {
        return map.get(key);
    }

    private static int minMaxColor = Integer.MAX_VALUE;
    private static Map<String, Integer> bestAssignment;

    public static SymbolStateCompression assignNumbers(List<List<String>> states) {
        if (states.isEmpty()) return EMPTY;

        minMaxColor = Integer.MAX_VALUE;
        bestAssignment = null;

        Set<String> allStrings = new HashSet<>();
        for (List<String> state : states) {
            allStrings.addAll(state);
        }
        List<String> strings = new ArrayList<>(allStrings);
        if (strings.isEmpty()) return EMPTY;

        boolean[][] isConflict = new boolean[strings.size()][strings.size()];
        Map<String, Integer> stringToIndex = new HashMap<>();
        for (int i = 0; i < strings.size(); i++) {
            stringToIndex.put(strings.get(i), i);
        }
        for (List<String> state : states) {
            for (int i = 0; i < state.size(); i++) {
                for (int j = i + 1; j < state.size(); j++) {
                    int idx1 = stringToIndex.get(state.get(i));
                    int idx2 = stringToIndex.get(state.get(j));
                    isConflict[idx1][idx2] = true;
                    isConflict[idx2][idx1] = true;
                }
            }
        }

        int[] colors = new int[strings.size()];
        Arrays.fill(colors, -1);
        backtrack(0, colors, isConflict, strings);

        return new SymbolStateCompression(bestAssignment, minMaxColor + 1);
    }

    private static void backtrack(int idx, int[] colors, boolean[][] isConflict, List<String> strings) {
        if (idx == strings.size()) {
            //noinspection OptionalGetWithoutIsPresent
            int maxColor = Arrays.stream(colors).max().getAsInt();
            if (maxColor < minMaxColor) {
                minMaxColor = maxColor;
                bestAssignment = new HashMap<>();
                for (int i = 0; i < strings.size(); i++) {
                    bestAssignment.put(strings.get(i), colors[i]);
                }
            }
            return;
        }

        o:for (int color = 0; color <= minMaxColor; color++) {
            for (int i = 0; i < idx; i++) {
                if (isConflict[idx][i] && colors[i] == color) {
                    continue o;
                }
            }
            colors[idx] = color;
            backtrack(idx + 1, colors, isConflict, strings);
            colors[idx] = -1;
        }
    }

}