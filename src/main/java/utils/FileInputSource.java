package utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public record FileInputSource(String path) implements InputSource {

    @Override
    public List<String> getLines() {
        try (var lines = Files.lines(Paths.get(path))) {
            return lines.toList();
        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }


}
