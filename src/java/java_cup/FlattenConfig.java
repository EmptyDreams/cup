package java_cup;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FlattenConfig {

    private final List<String> listSuffix = new ArrayList<>();
    private final List<Pattern> inlineExpr = new ArrayList<>();
    private final List<Pattern> namelessInlineExpr = new ArrayList<>();

    public FlattenConfig(String config) {
        String[] split = config.split("&");
        for (String item : split) {
            String[] entry = item.split("=");
            if (entry.length > 2) {
                Main.usage("-ast_flatten can be only one '=' in a kv pair, but input: " + item);
            }
            String key = entry[0];
            List<String> values = entry.length == 1 ? Collections.emptyList()
                : Arrays.stream(entry[1].split(","))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
            switch (key) {
                case "list":
                    listSuffix.addAll(values);
                    break;
                case "inline":
                    for (String value : values) {
                        inlineExpr.add(Pattern.compile(value.replace("+", "(.+)")));
                    }
                    break;
                case "namelessInline":
                    for (String value : values) {
                        namelessInlineExpr.add(Pattern.compile(value.replace("+", "(.+)")));
                    }
                    break;
                default:
                    Main.usage("-ast_flatten has unknown flatten key: " + key);
            }
        }
    }

    public FlattenConfig() {}

    public boolean isListName(String name) {
        return listSuffix.stream().anyMatch(name::endsWith);
    }

    public String getInlineName(String name) {
        var isNameless = namelessInlineExpr.stream()
            .anyMatch(pattern -> pattern.matcher(name).matches());
        if (isNameless) return "";
        return inlineExpr.stream().map(pattern -> {
            var matcher = pattern.matcher(name);
            if (!matcher.matches()) return null;
            StringBuilder result = new StringBuilder(name.length());
            result.append(matcher.group(1));
            for (int i = 2; i <= matcher.groupCount(); i++) {
                var text = matcher.group(i);
                result.append(Character.toUpperCase(text.charAt(0)));
                for (int k = 1; k < text.length(); k++) {
                    result.append(text.charAt(k));
                }
            }
            return result.toString();
        }).filter(Objects::nonNull).findAny().orElse(null);
    }

}