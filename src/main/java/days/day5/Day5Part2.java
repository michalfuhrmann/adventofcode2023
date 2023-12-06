package days.day5;

import days.day5.InputParser.Mapping;
import utils.FileInputSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class Day5Part2 {


    public static void main(String[] args) throws IOException {
        var inputSoruce = new FileInputSource("src/main/resources/days/day5/input.txt");

        var result = compute(inputSoruce);

        System.out.println("Result is " + result);
    }

    private static long compute(FileInputSource inputSoruce) throws IOException {

        var inputParser = new InputParser(Files.readString(Paths.get("src/main/resources/days/day5/input.txt")));


        var mappingChain = new MappingChain(List.of(
                new MultiMapping(inputParser.getSeedToSoil()),
                new MultiMapping(inputParser.getSoilToFertilizer()),
                new MultiMapping(inputParser.getFertilizerToWater()),
                new MultiMapping(inputParser.getWaterToLight()),
                new MultiMapping(inputParser.getLightToTemperature()),
                new MultiMapping(inputParser.getTemperatureToHumidity()),
                new MultiMapping(inputParser.getHumidityToLocation())
        ));
        return inputParser.getSeedListOfList().stream().parallel()
                .flatMap(longStream -> longStream.map(mappingChain::getMappedValue))
                .mapToLong(value -> value)
                .min().orElseThrow();

    }


    static class MultiMapping {
        private final List<Mapping> mappings;

        public MultiMapping(List<Mapping> mappings) {
            this.mappings = mappings.stream().sorted(Comparator.comparing(Mapping::source)).toList();
        }

        public long getMappedValue(long key) {
            for (Mapping m : mappings) {
                if (key < m.source()) {
                    return key;
                }
                if (key < m.source() + m.range()) {
                    return m.getMappedValue(key);
                }
            }
            return key;
        }
    }

    static class MappingChain {
        private final List<MultiMapping> mappings;

        public MappingChain(List<MultiMapping> mappings) {
            this.mappings = mappings;
        }

        public long getMappedValue(long key) {
            for (MultiMapping mapping : mappings) {
                key = mapping.getMappedValue(key);
            }
            return key;
        }
    }
}
