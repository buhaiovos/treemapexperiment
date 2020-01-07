package ua.buhaiovos.sillygames;

import java.util.TreeMap;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        TreeMap<String, String> setActually = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i")
                .reduce(new TreeMap<>(),
                        (map, value) -> {
                            map.put(value, value);
                            return map;
                        },
                        (l, r) -> {
                            l.putAll(r);
                            return l;
                        });

        var printer = new TreeMapPrinter();
        String text = printer.treeOfColors(setActually);

        System.out.printf("""
                Got:
                %s""", text
        );
    }
}
