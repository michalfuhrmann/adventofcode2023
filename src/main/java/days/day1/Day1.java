package days.day1;

import utils.FileInputSource;
import utils.InputSource;

public class Day1 {

    public static void main(String[] args) {

        var inputSoruce = new FileInputSource("src/main/resources/days/day1/input.txt");
//        var digitExtractor = new DigitExtractor.SimpleDigitExtractor();
        var digitExtractor = new DigitExtractor.TextOrDigitExtractor();


        var sumForFile = getSumForFilePart1(inputSoruce, digitExtractor);

        System.out.println("Result is " + sumForFile);
    }

    private static int getSumForFilePart1(InputSource source, DigitExtractor digitExtractor) {

        return source.getLines().stream()
                .map(digitExtractor::extract)
                .mapToInt(pair -> pair.getLeft() * 10 + pair.getRight())
                .sum();
    }


}
