package java_cup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlattenConfig {

    private final List<String> listSuffix = new ArrayList<>();
    private final List<String> inlineSuffix = new ArrayList<>();

    public FlattenConfig(String config) {
        String[] split = config.split("&");
        for (String item : split) {
            String[] entry = item.split("=");
            if (entry.length != 2) {
                Main.usage("-ast_flatten can be only one '=' in a kv pair, but input: " + item);
            }
            String key = entry[0];
            List<String> values = Arrays.stream(entry[1].split(","))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            switch (key) {
                case "list":
                    listSuffix.addAll(values);
                    break;
                case "inline":
                    inlineSuffix.addAll(values);
                    break;
                default:
                    Main.usage("-ast_flatten has unknown flatten key: " + key);
            }
        }
    }

    public FlattenConfig() { }

    public boolean isListName(String name) {
        return listSuffix.stream().anyMatch(name::endsWith);
    }

    public boolean isInlineName(String name) {
        return inlineSuffix.stream().anyMatch(name::endsWith);
    }

}