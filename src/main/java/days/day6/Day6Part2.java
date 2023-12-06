package days.day6;

import org.apache.commons.lang3.tuple.Pair;
import utils.FileInputSource;

import java.util.ArrayList;
import java.util.List;

public class Day6Part2 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day6/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static int compute(FileInputSource inputSoruce) {
        var lists = parseData(inputSoruce.getLines());
        var listOfRaces = transform(lists);

        return listOfRaces.stream()
                .map(Day6Part2::findPossibleCombinations)
                .reduce(1, (a, b) -> a * b);

    }

    private static int findPossibleCombinations(Pair<Long, Long> racePair) {
        var result = 0;

        long time = racePair.getLeft();
        long distanceRecord = racePair.getRight();

        for (int i = 0; i < time; i++) {
            var distanceTravelled = getDistanceGivenHoldPeriod(i, time);
            if (distanceTravelled > distanceRecord) {
                result++;
            }
        }

        System.out.println("racePair = " + racePair + " result " + result);

        return result;
    }

    private static long getDistanceGivenHoldPeriod(long holdPeriod, long time) {
        var travelTime = time - holdPeriod;
        return holdPeriod * travelTime;

    }

    private static List<Pair<Long, Long>> transform(List<List<Long>> lists) {

        String time = "";
        String distance = "";

        for (int i = 0; i < lists.get(0).size(); i++) {

            time += lists.get(0).get(i);
            distance += lists.get(1).get(i);
        }


        return List.of(Pair.of(Long.parseLong(time), Long.parseLong(distance)));
    }


    private static List<List<Long>> parseData(List<String> lines) {
        List<List<Long>> result = new ArrayList<>();

        for (String line : lines) {
            String[] values = line.trim().split("\\s+");
            List<Long> numbers = new ArrayList<>();

            for (int i = 1; i < values.length; i++) {
                numbers.add(Long.parseLong(values[i]));
            }
            result.add(numbers);
        }
        return result;
    }
}

