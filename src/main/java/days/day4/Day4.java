package days.day4;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day4/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static int compute(FileInputSource inputSoruce) {

        var pairs = parseData(inputSoruce.getLines());
        return pairs.stream()
                .mapToInt(Day4::calculateCardPoints)
                .peek(System.out::println)
                .sum();
    }

    private static int calculateCardPoints(Pair<Set<Integer>, Set<Integer>> pair) {

        var count = pair.getLeft().stream()
                .filter(number -> pair.getRight().contains(number))
                .count();


        return (int) Math.pow(2, count - 1);
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
