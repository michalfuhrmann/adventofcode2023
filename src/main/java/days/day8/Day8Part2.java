package days.day8;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day8Part2 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day8/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static long compute(FileInputSource inputSoruce) {

        var data = parseData(inputSoruce.getLines());

        var map = data.map();

        var ordesArray = data.orders();

        long lcm = 1;
        for (String node : map.keySet()) {
            if (node.endsWith("A")) {
                long cycleLength = getCycleLength(map, ordesArray, node);
                lcm = lcm(lcm, cycleLength);
            }
        }

        return lcm;

    }


    private static long getCycleLength(Map<String, Pair<String, String>> map, String orders, String startNode) {
        String currentNode = startNode;
        long steps = 0;

        do {
            char instruction = orders.charAt((int) (steps % orders.length()));
            currentNode = getFunction(instruction).apply(map.get(currentNode));
            steps++;
        } while (!currentNode.endsWith("Z") && !currentNode.equals(startNode));

        return currentNode.endsWith("Z") ? steps : 0;
    }


    private static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    private static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    private static Data parseData(List<String> lines) {
        String firstLine = lines.get(0);

        Map<String, Pair<String, String>> map = new HashMap<>();

        Pattern pattern = Pattern.compile("([0-9A-Z]+) = \\(([0-9A-Z]+), ([0-9A-Z]+)\\)");
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

