package days.day2;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day2 {

    static Map<Color, Integer> CONDITIONS = Map.of(Color.RED, 12, Color.GREEN, 13, Color.BLUE, 14);

    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day2/input.txt");


        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static Object compute(FileInputSource inputSoruce) {
        return inputSoruce.getLines().stream()
                .map(Day2::parseLine)
                .filter(Day2::isLineValid)
                .mapToInt(Pair::getLeft)
                .sum();
    }

    private static boolean isLineValid(Pair<Integer, List<Map<Color, Integer>>> integerListPair) {
        return integerListPair.getRight().stream()
                .allMatch(Day2::isValid);
    }


    private static boolean isValid(Map<Color, Integer> colorIntegerMap) {
        return colorIntegerMap.entrySet().stream()
                .allMatch(entry -> entry.getValue() <= CONDITIONS.get(entry.getKey()));
    }

    public static Pair<Integer, List<Map<Color, Integer>>> parseLine(String line) {
        String[] parts = line.split(":", 2);
        int gameNumber = Integer.parseInt(parts[0].replaceAll("[^0-9]", "").trim());

        String[] draws = parts[1].split(";");
        List<Map<Color, Integer>> drawsList = new ArrayList<>();

        for (String draw : draws) {
            Map<Color, Integer> colorCounts = new HashMap<>();
            String[] colorTokens = draw.trim().split(", ");

            for (String token : colorTokens) {
                String[] colorParts = token.trim().split(" ");
                if (colorParts.length == 2) {
                    int count = Integer.parseInt(colorParts[0]);
                    Color color = Color.valueOf(colorParts[1].toUpperCase());
                    colorCounts.put(color, colorCounts.getOrDefault(color, 0) + count);
                }
            }
            drawsList.add(colorCounts);
        }

        return Pair.of(gameNumber, drawsList);
    }

    enum Color {
        BLUE, RED, GREEN
    }

}
