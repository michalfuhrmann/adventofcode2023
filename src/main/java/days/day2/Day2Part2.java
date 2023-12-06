package days.day2;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class Day2Part2 {

    static Set<Color> ALL_COLORS = Set.of(Color.RED, Color.GREEN, Color.BLUE);

    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day2/input.txt");


        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static Object compute(FileInputSource inputSoruce) {
        return inputSoruce.getLines().stream()
                .map(Day2Part2::parseLine)
                .mapToInt(Day2Part2::getPowerOfASet)
                .sum();
    }

    private static Integer getPowerOfASet(Pair<Integer, List<Map<Color, Integer>>> integerListPair) {
        Map<Color, Integer> colorsMap = new HashMap<>();
        integerListPair.getRight()
                .forEach(map -> {
                    map.forEach((key, value) ->
                            colorsMap.compute(key, (color, integer) -> integer == null ? value : Math.max(integer, value)));
                });


        AtomicReference<Integer> result = new AtomicReference<>(1);
        ALL_COLORS.forEach(color -> result.set(colorsMap.getOrDefault(color, 0) * result.get()));

        return result.get();

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
