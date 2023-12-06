package days.day4;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4Part2 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day4/input.txt");


        var result = compute(inputSoruce);
        System.out.println("Result is " + result);
    }

    private static int compute(FileInputSource inputSoruce) {
        var pairs = parseData(inputSoruce.getLines());

        Map<Integer, Integer> results = new HashMap<>();
        int cardIdx = 1;

        for (Pair<Set<Integer>, Set<Integer>> pair : pairs) {
            results.compute(cardIdx++, (p, c) -> 1);
        }

        cardIdx = 1;

        for (Pair<Set<Integer>, Set<Integer>> pair : pairs) {

            int copies = (int) calculateCardPoints(pair);
            var cardCount = results.get(cardIdx);

            System.out.println("copies = " + copies + " for index " + cardIdx);

            for (int idx = cardIdx + 1; idx <= copies + cardIdx; idx++) {
                results.compute(idx, (k, v) -> {
                    if (v == null) {
                        return 1;
                    }
                    return v + cardCount;
                });
            }
            System.out.println("card count after " + cardIdx + " is " + results);
            cardIdx++;
        }
        int finalCardIdx1 = cardIdx;

        System.out.println(results);
        return results.entrySet().stream()
                .filter(it -> it.getKey() < finalCardIdx1)
                .map(Map.Entry::getValue)
                .peek(System.out::println)
                .mapToInt(Integer::intValue).sum();
    }

    private static long calculateCardPoints(Pair<Set<Integer>, Set<Integer>> pair) {

        return pair.getRight().stream()
                .filter(number -> pair.getLeft().contains(number))
                .count();
    }

    private static List<Pair<Set<Integer>, Set<Integer>>> parseData(List<String> lines) {
        List<Pair<Set<Integer>, Set<Integer>>> result = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split(": ");
            String[] numbers = parts[1].split(" \\| ");

            var set1 = Arrays.stream(numbers[0].split("\s+"))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            var set2 = Arrays.stream(numbers[1].split("\s+"))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toSet());

            result.add(Pair.of(set1, set2));

        }

        return result;
    }


}
