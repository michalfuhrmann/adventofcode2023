package days.day5;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class InputParser {

    private final List<Long> seeds;
    private final List<Mapping> seedToSoil;
    private final List<Mapping> soilToFertilizer;
    private final List<Mapping> fertilizerToWater;
    private final List<Mapping> waterToLight;
    private final List<Mapping> lightToTemperature;
    private final List<Mapping> temperatureToHumidity;
    private final List<Mapping> humidityToLocation;
    private final Stream<Long> seedsStream;
    private final List<Stream<Long>> seedListOfList;

    public InputParser(String input) {
        String[] sections = input.split("\n\n");
        this.seeds = List.of();
        this.seedsStream = parseSeedsPartTwo(sections[0]);
        this.seedListOfList = parseSeedListOfList(sections[0]);
        this.seedToSoil = parseMap(sections[1]);
        this.soilToFertilizer = parseMap(sections[2]);
        this.fertilizerToWater = parseMap(sections[3]);
        this.waterToLight = parseMap(sections[4]);
        this.lightToTemperature = parseMap(sections[5]);
        this.temperatureToHumidity = parseMap(sections[6]);
        this.humidityToLocation = parseMap(sections[7]);
    }

    private List<Long> parseSeeds(String section) {
        return Arrays.stream(section.split(": ")[1].split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    private Stream<Long> parseSeedsPartTwo(String section) {
        var seeds = Arrays.stream(section.split(": ")[1].split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return LongStream.range(0, seeds.size() - 1)
                .filter(i -> i % 2 == 0)
                .peek(System.out::println)
                .flatMap(value -> LongStream.range(seeds.get((int) value), seeds.get((int) value) + seeds.get((int) (value + 1))))
                .boxed();


    }

    private List<Stream<Long>> parseSeedListOfList(String section) {
        var seeds = Arrays.stream(section.split(": ")[1].split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        return LongStream.range(0, seeds.size() - 1)
                .filter(i -> i % 2 == 0)
                .peek(System.out::println)
                .mapToObj(value -> LongStream.range(seeds.get((int) value), seeds.get((int) value) + seeds.get((int) (value + 1))).boxed())
                .collect(Collectors.toList());


    }


    private List<Mapping> parseMap(String section) {
        String[] lines = section.split(":\n")[1].split("\n");
        return Arrays.stream(lines)
                .map(line -> {
                    var parts = line.split(" ");
                    return new Mapping(Long.parseLong(parts[0]), Long.parseLong(parts[1]), Long.parseLong(parts[2]));
                })
                .collect(Collectors.toList());
    }


    // Getters
    public List<Long> getSeeds() {
        return seeds;
    }

    public Stream<Long> getSeedsStream() {
        return seedsStream;
    }

    public List<Stream<Long>> getSeedListOfList() {
        return seedListOfList;
    }

    public List<Mapping> getSeedToSoil() {
        return seedToSoil;
    }

    public List<Mapping> getSoilToFertilizer() {
        return soilToFertilizer;
    }

    public List<Mapping> getFertilizerToWater() {
        return fertilizerToWater;
    }

    public List<Mapping> getWaterToLight() {
        return waterToLight;
    }

    public List<Mapping> getLightToTemperature() {
        return lightToTemperature;
    }

    public List<Mapping> getTemperatureToHumidity() {
        return temperatureToHumidity;
    }

    public List<Mapping> getHumidityToLocation() {
        return humidityToLocation;
    }

    record Mapping(long destination, long source, long range) {

        public boolean inRange(long value) {
            return value >= source && value < source + range;
        }

        public long getMappedValue(long value) {
            return destination + (value - source);
        }
    }
}
