package days.day1;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface DigitExtractor {

    Pair<Integer, Integer> extract(String line);

    static class SimpleDigitExtractor implements DigitExtractor {

        @Override
        public Pair<Integer, Integer> extract(String line) {
            Integer first = null;
            Integer last = null;
            for (char c : line.toCharArray()) {
                if (Character.isDigit(c) && first == null) {
                    first = Character.getNumericValue(c);
                }
                if (Character.isDigit(c)) {
                    last = Character.getNumericValue(c);
                }

            }
            return Pair.of(first, last);
        }
    }


    public static class TextOrDigitExtractor implements DigitExtractor {
        private static final Map<String, Integer> DICTIONARY = Map.of("one", 1, "two", 2, "three", 3, "four", 4, "five", 5,
                "six", 6, "seven", 7, "eight", 8, "nine", 9, "ten", 10);
        private static final int MAX_WINDOW = DICTIONARY.keySet().stream().mapToInt(String::length).max().orElseThrow();

        private static Optional<Integer> getDigitIfPresent(String line, String window, int i) {
            var textValue = DICTIONARY.entrySet().stream()
                    .filter(x -> window.startsWith(x.getKey()))
                    .mapToInt(Map.Entry::getValue)
                    .max();

            var digitValue = getDigitValue(line, i);

            return Stream.of(textValue, digitValue).map(OptionalInt::stream)
                    .flatMap(IntStream::boxed)
                    .max(Integer::compareTo);
        }

        private static OptionalInt getDigitValue(String line, int i) {
            OptionalInt digitValue = OptionalInt.empty();

            if (Character.isDigit(line.charAt(i))) {
                digitValue = OptionalInt.of(Character.getNumericValue(line.charAt(i)));
            }
            return digitValue;
        }

        @Override
        public Pair<Integer, Integer> extract(String line) {

            Integer first = null;
            Integer last = null;

            for (int i = 0; i < line.length(); i++) {
                String window = line.substring(i, Math.min(line.length(), i + MAX_WINDOW));

                var value = getDigitIfPresent(line, window, i);

                if (value.isPresent()) {
                    if (first == null) {
                        first = value.get();
                    }
                    last = value.get();

                }
            }
            System.out.println("line = " + line + "first = " + first + ", last = " + last);

            return Pair.of(first, last);
        }
    }

}

