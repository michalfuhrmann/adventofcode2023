package days.day3;

import utils.FileInputSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day3 {


    static Set<Character> allChars = new HashSet<>();

    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day3/input.txt");


        var result = compute(inputSoruce);


        System.out.println("Result is " + result);
        System.out.println("allChars = " + allChars);
    }

    private static int compute(FileInputSource inputSoruce) {
        var symbols = getSynbols(inputSoruce);

        AtomicInteger rowCounter = new AtomicInteger();

        return inputSoruce.getLines().stream()
                .flatMap(line -> parseLine(line, rowCounter, symbols).stream())
                .mapToInt(Integer::intValue)
                .sum();

    }

    private static Set<Point> getSynbols(FileInputSource inputSoruce) {
        AtomicInteger rowCounter = new AtomicInteger();

        return inputSoruce.getLines().stream()
                .flatMap(line -> parseSymbols(line, rowCounter).stream())
                .collect(Collectors.toSet());
    }

    private static List<Integer> parseLine(String line, AtomicInteger rowCounter, Set<Point> symbols) {
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
                if (numberNeighbours.stream().anyMatch(symbols::contains)) {
                    System.out.println("added number " + number);
                    results.add(number);
                }
                number = null;
                numberNeighbours = new HashSet<>();
                //put in
            }
            columnCounter.incrementAndGet();
        }

        if (number != null) {
            if (numberNeighbours.stream().anyMatch(symbols::contains)) {
                System.out.println("added number " + number);
                results.add(number);
            }
        }


        rowCounter.incrementAndGet();
        return results;
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
            if (!Character.isDigit(c) && c != '.') {
                allChars.add(c);
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
