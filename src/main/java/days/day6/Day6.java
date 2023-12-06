package days.day6;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.List;

public class Day6 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day6/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static int compute(FileInputSource inputSoruce) {
        var lists = parseData(inputSoruce.getLines());
        var listOfRaces = transform(lists);

        return listOfRaces.stream()
                .map(Day6::findPossibleCombinations)
                .reduce(1, (a, b) -> a * b);

    }

    private static int findPossibleCombinations(Pair<Integer, Integer> racePair) {
        var result = 0;

        int time = racePair.getLeft();
        int distanceRecord = racePair.getRight();

        for (int i = 0; i < time; i++) {
            var distanceTravelled = etDistanceGivenHoldPeriod(i, time);
            if (distanceTravelled > distanceRecord) {
                result++;
            }
        }

        System.out.println("racePair = " + racePair + " result " + result);

        return result;
    }

    private static int etDistanceGivenHoldPeriod(int holdPeriod, int time) {
        var travelTime = time - holdPeriod;
        return holdPeriod * travelTime;

    }

    private static List<Pair<Integer, Integer>> transform(List<List<Integer>> lists) {
        List<Pair<Integer, Integer>> races = new ArrayList<>();
        for (int i = 0; i < lists.get(0).size(); i++) {
            races.add(Pair.of(lists.get(0).get(i), lists.get(1).get(i)));

        }
        return races;

    }


    private static List<List<Integer>> parseData(List<String> lines) {
        List<List<Integer>> result = new ArrayList<>();

        for (String line : lines) {
            String[] values = line.trim().split("\\s+");
            List<Integer> numbers = new ArrayList<>();

            for (int i = 1; i < values.length; i++) {
                numbers.add(Integer.parseInt(values[i]));
            }
            result.add(numbers);
        }
        return result;
    }
}

