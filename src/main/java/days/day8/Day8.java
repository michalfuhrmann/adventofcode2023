package days.day7;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day8/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static long compute(FileInputSource inputSoruce) {


        var data = parseData(inputSoruce.getLines());

        String target = "AAA";
        int counter = 0;
        var map = data.map();


        var ordesArray = data.orders().toCharArray();


        do {
            var function = getFunction(ordesArray[counter % ordesArray.length]);
            target = function.apply(map.get(target));
            counter++;
        } while (!target.equals("ZZZ"));

        return counter;


    }

    private static Data parseData(List<String> lines) {
        String firstLine = lines.get(0);

        Map<String, Pair<String, String>> map = new HashMap<>();

        Pattern pattern = Pattern.compile("([A-Z]+) = \\(([A-Z]+), ([A-Z]+)\\)");
        for (int i = 1; i < lines.size(); i++) {
            if (!lines.get(i).trim().isEmpty()) {
                Matcher matcher = pattern.matcher(lines.get(i));
                if (matcher.matches()) {
                    String key = matcher.group(1);
                    String value1 = matcher.group(2);
                    String value2 = matcher.group(3);
                    map.put(key, Pair.of(value1, value2));
                }
            }
        }

        return new Data(firstLine, map);
    }

    static Function<Pair<String, String>, String> getFunction(char c) {
        return pair -> {
            if (c == 'R') {
                return pair.getRight();
            }
            if (c == 'L') {
                return pair.getLeft();
            }
            throw new IllegalArgumentException();
        };

    }

    record Data(String orders, Map<String, Pair<String, String>> map) {

    }
}

