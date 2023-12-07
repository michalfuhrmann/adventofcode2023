package days.day7;

import utils.FileInputSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day7Part2 {


    public static void main(String[] args) {
        var inputSoruce = new FileInputSource("src/main/resources/days/day7/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static long compute(FileInputSource inputSoruce) {


        var input = parseHands(inputSoruce.getLines());

        var hands = input.stream()
                .map(parsedInput -> {
                    var collect = Arrays.stream(parsedInput.hand().split("")).map(Symbol::new).collect(Collectors.toList());
                    return new Hand(collect, parsedInput.score());
                }).sorted(Comparator.comparing((Hand hand) -> hand.category.handType.strength)
                        .thenComparing(hand -> hand.cards.get(0).strength)
                        .thenComparing(hand -> hand.cards.get(1).strength)
                        .thenComparing(hand -> hand.cards.get(2).strength)
                        .thenComparing(hand -> hand.cards.get(3).strength)
                        .thenComparing(hand -> {

                            System.out.println("hand = " + hand);
                            return hand.cards.get(4).strength;
                        })
                )
                .collect(Collectors.toList());

        long result = 0;
        for (int i = 0; i < hands.size(); i++) {
            result += (long) hands.get(i).bet * (i + 1);
        }

        return result;


    }

    private static List<ParsedInput> parseHands(List<String> handsData) {
        List<ParsedInput> parsedInputs = new ArrayList<>();
        for (String handData : handsData) {
            String[] parts = handData.split(" ");
            String cards = parts[0];
            int score = Integer.parseInt(parts[1]);
            parsedInputs.add(new ParsedInput(cards, score));
        }
        return parsedInputs;
    }

    enum HandType {
        FIVE(7),
        FOUR(6),
        FULL_HOUSE(5),
        THREE(4),
        TWO_PAIR(3),
        ONE_PAIR(2),
        HIGH_CARD(1);

        int strength;

        HandType(int strength) {
            this.strength = strength;
        }
    }

    record ParsedInput(String hand, int score) {
    }

    public static class Symbol implements Comparable<Symbol> {
        private final int strength;
        private final String symbol;

        Symbol(String symbol) {
            this.symbol = symbol;
            this.strength = calculateStrength(symbol);

        }

        @Override
        public String toString() {
            return symbol;
        }

        public int getStrength() {
            return strength;
        }

        public String getSymbol() {
            return symbol;
        }

        private int calculateStrength(String symbol) {
            if (symbol.equals("A")) {

                return 14;
            }
            if (symbol.equals("K")) {

                return 13;
            }
            if (symbol.equals("Q")) {

                return 12;
            }
            if (symbol.equals("J")) {

                return 1;
            }
            if (symbol.equals("T")) {

                return 10;
            }
            return Integer.parseInt(symbol);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Symbol symbol1)) return false;
            return Objects.equals(symbol, symbol1.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol);
        }

        @Override
        public int compareTo(Symbol o) {
            return symbol.compareTo(o.symbol);
        }
    }

    public static class Hand {

        private final List<Symbol> cards;
        private final int bet;
        private final HandValue category;

        public Hand(List<Symbol> cards, int bet) {
            this.cards = cards;
            this.bet = bet;
            this.category = cardsCategory(cards);
        }

        private static HandValue standardRanking(List<Symbol> symbols, Map<Symbol, Long> groups) {
            if (groups.size() >= 5) {
                return new HandValue(HandType.HIGH_CARD);
            }

            if (groups.size() == 4) {

                return new HandValue(HandType.ONE_PAIR);
            }
            if (groups.size() == 3) {
                //two pair
                var groupedGroups = groups.entrySet().stream().collect(Collectors.groupingBy(t -> t.getValue(), Collectors.counting()));

                if (groupedGroups.containsKey(3L)) {
                    return new HandValue(HandType.THREE);
                }
                if (groupedGroups.containsKey(2L)) {
                    return new HandValue(HandType.TWO_PAIR);
                }
                throw new IllegalArgumentException("Invalid hand " + symbols);

            }
            if (groups.size() == 2) {
                //two pair
                var groupedGroups = groups.entrySet().stream().collect(Collectors.groupingBy(t -> t.getValue(), Collectors.counting()));

                if (groupedGroups.containsKey(4L)) {
                    return new HandValue(HandType.FOUR);
                }
                if (groupedGroups.containsKey(3L)) {
                    return new HandValue(HandType.FULL_HOUSE);
                }
                throw new IllegalArgumentException("Invalid hand " + symbols);

            }
            if (groups.size() == 1) {
                return new HandValue(HandType.FIVE);
            }
            throw new IllegalArgumentException("Invalid hand " + symbols);
        }

        private HandValue cardsCategory(List<Symbol> symbols) {
            var groups = symbols.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            if (symbols.stream().anyMatch(symbol -> symbol.symbol.equals("J"))) {
                var max = groups.entrySet().stream()
                        .filter(symbolLongEntry -> !symbolLongEntry.getKey().symbol.equals("J"))
                        .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.counting())).entrySet().stream()
                        .max(Map.Entry.comparingByKey()).map(Map.Entry::getKey).orElse(5L);

                var bestSymbol = symbols.stream()
                        .filter(it -> !it.getSymbol().equals("J"))
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream().filter(it -> it.getValue() == max)
                        .findFirst()
                        .map(Map.Entry::getKey)
                        .orElse(new Symbol("J"));

                var improvedSymbols = symbols.stream()
                        .map(s -> {
                            if (s.symbol.equals("J")) {
                                return bestSymbol;
                            } else {
                                return s;
                            }
                        }).collect(Collectors.toList());


                var improvedRanking = standardRanking(improvedSymbols, improvedSymbols.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));

                var standardRanking = standardRanking(symbols, groups);

                if (improvedRanking.handType.strength > standardRanking.handType.strength) {
//                    System.out.println("improvedSymbols = " + improvedSymbols + " improvedRanking = " + improvedRanking);
                    return improvedRanking;
                } else {
                    System.out.println("symbols = " + symbols + " standardRanking = " + standardRanking + " while improved ranking is " + improvedRanking + "improvedSymbols = " + improvedSymbols);

                    return standardRanking;
                }

//                return jokerRanking(symbols, groups);
            }
            return standardRanking(symbols, groups);
        }

        private HandValue jokerRanking(List<Symbol> symbols, Map<Symbol, Long> groups) {
            var jokersCount = symbols.stream().filter(symbol -> symbol.symbol.equals("J"))
                    .count();

            if (groups.size() >= 5) {
                //convert joker x times to highest
//                var maxSymbol = symbols.stream().sorted(Comparator.comparing((Symbol symbol) -> symbol.symbol).reversed())
//                        .findFirst().orElseThrow();

                if (jokersCount == 1) {
                    return new HandValue(HandType.ONE_PAIR);
                } else if (jokersCount == 2) {
                    return new HandValue(HandType.THREE);
                } else if (jokersCount == 3) {
                    return new HandValue(HandType.FULL_HOUSE);
                } else if (jokersCount == 4) {
                    return new HandValue(HandType.FIVE);
                }

                return new HandValue(HandType.HIGH_CARD);
            }

            if (groups.size() == 4) {

                if (jokersCount == 1) {
                    return new HandValue(HandType.THREE);
                } else if (jokersCount == 3) {
                    return new HandValue(HandType.FOUR);
                } else if (jokersCount == 4) {
                    return new HandValue(HandType.FIVE);
                }

                return new HandValue(HandType.ONE_PAIR);
            }
            if (groups.size() == 3) {
                //two pair
                var groupedGroups = groups.entrySet().stream().collect(Collectors.groupingBy(t -> t.getValue(), Collectors.counting()));

                if (groupedGroups.containsKey(3L)) {
                    if (jokersCount == 1) {
                        return new HandValue(HandType.THREE);
                    } else if (jokersCount == 3) {
                        return new HandValue(HandType.FOUR);
                    } else if (jokersCount == 4) {
                        return new HandValue(HandType.FIVE);
                    }

                    return new HandValue(HandType.THREE);
                }
                if (groupedGroups.containsKey(2L)) {
                    return new HandValue(HandType.TWO_PAIR);
                }
                throw new IllegalArgumentException("Invalid hand " + symbols);

            }
            if (groups.size() == 2) {
                //two pair
                var groupedGroups = groups.entrySet().stream().collect(Collectors.groupingBy(t -> t.getValue(), Collectors.counting()));

                if (groupedGroups.containsKey(4L)) {
                    return new HandValue(HandType.FOUR);
                }
                if (groupedGroups.containsKey(3L)) {
                    return new HandValue(HandType.FULL_HOUSE);
                }
                throw new IllegalArgumentException("Invalid hand " + symbols);

            }
            if (groups.size() == 1) {
                return new HandValue(HandType.FIVE);
            }
            throw new IllegalArgumentException("Invalid hand " + symbols);
        }

        @Override
        public String toString() {
            return "Hand{" +
                    "cards=" + cards +
                    '}';
        }
    }

    record HandValue(HandType handType) {
    }
}

