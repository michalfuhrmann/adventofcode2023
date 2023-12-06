package days.day3;

import utils.FileInputSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day3Part2 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day3/input.txt");


        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static long compute(FileInputSource inputSoruce) {
        var symbols = getSynbols(inputSoruce);

        AtomicInteger rowCounter = new AtomicInteger();
        Map<Point, List<Integer>> linksCounter = new HashMap<>();

        inputSoruce.getLines().stream()
                .forEach(line -> parseLine(line, rowCounter, symbols, linksCounter));


        return linksCounter.entrySet().stream()
                .filter(it -> it.getValue().size() == 2)
                .peek(it -> System.out.println("it = " + it))
                .mapToLong(it -> it.getValue().stream().reduce(1, (a, b) -> a * b))
                .sum();

    }

    private static Set<Point> getSynbols(FileInputSource inputSoruce) {
        AtomicInteger rowCounter = new AtomicInteger();

        return inputSoruce.getLines().stream()
                .flatMap(line -> parseSymbols(line, rowCounter).stream())
                .collect(Collectors.toSet());
    }

    private static List<Integer> parseLine(String line, AtomicInteger rowCounter, Set<Point> symbols, Map<Point, List<Integer>> links) {
        AtomicInteger columnCounter = new AtomicInteger();

        List<Integer> results = new ArrayList<>();
        Integer number = null;
        Set<Point> numberNeighbours = new HashSet<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (Character.isDigit(c)) {
                if (number == null) {
                    number = Character.getNumericValue(c);
                } else {
                    number = number * 10 + Character.getNumericValue(c);
                }
                numberNeighbours.addAll(getNeighbours(rowCounter, columnCounter));

            } else {
//                System.out.println("found number " + number + " with neighbours " + numberNeighbours);
                addLink(symbols, numberNeighbours, number, links);
                number = null;
                numberNeighbours = new HashSet<>();
                //put in
            }
            columnCounter.incrementAndGet();
        }

        if (number != null) {
            addLink(symbols, numberNeighbours, number, links);

        }


        rowCounter.incrementAndGet();
        return results;
    }

    private static void addLink(Set<Point> symbols, Set<Point> numberNeighbours, Integer number, Map<Point, List<Integer>> linksCounter) {

        numberNeighbours.stream()
                .filter(symbols::contains)
                .forEach(point ->
                        linksCounter.computeIfAbsent(point, (p) -> new ArrayList<>())
                                .add(number));

    }

    private static Set<Point> getNeighbours(AtomicInteger rowCounter, AtomicInteger columnCounter) {

        Set<Point> neighbours = new HashSet<>();
        for (int row = rowCounter.get() - 1; row <= rowCounter.get() + 1; row++) {
            for (int column = columnCounter.get() - 1; column <= columnCounter.get() + 1; column++) {
                if (row == rowCounter.get() && column == columnCounter.get()) {
                    continue;
                }
                neighbours.add(new Point(column, row));
            }
        }
        return neighbours;
    }

    private static Set<Point> parseSymbols(String line, AtomicInteger rowCounter) {
        AtomicInteger columnCounter = new AtomicInteger();


        Set<Point> points = new HashSet<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '*') {
                points.add(new Point(columnCounter.get(), rowCounter.get()));
            }
            columnCounter.incrementAndGet();
        }


        rowCounter.incrementAndGet();
        return points;
    }


    record Point(int x, int y) {
    }


}
