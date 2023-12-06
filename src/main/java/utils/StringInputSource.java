package utils;

import java.util.List;

public record StringInputSource(List<String> data) implements InputSource {
    @Override
    public List<String> getLines() {
        return data;
    }

}
