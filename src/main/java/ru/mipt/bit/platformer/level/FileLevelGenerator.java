package ru.mipt.bit.platformer.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FileLevelGenerator implements LevelGenerator {

    private final String levelFile;
    private final LevelParser parser = new LevelParser();

    public FileLevelGenerator(String levelFile) {
        this.levelFile = levelFile;
    }

    @Override
    public LevelLayout generate(int fieldWidth, int fieldHeight) {
        LevelLayout layout = parser.parse(readLines());
        if (layout.getWidth() > fieldWidth || layout.getHeight() > fieldHeight) {
            throw new IllegalArgumentException("Level " + levelFile + " of size " + layout.getWidth() + "x" + layout.getHeight()
                    + " does not fit into field " + fieldWidth + "x" + fieldHeight);
        }
        return layout;
    }

    private List<String> readLines() {
        InputStream stream = FileLevelGenerator.class.getClassLoader().getResourceAsStream(levelFile);
        if (stream == null) {
            throw new IllegalArgumentException("Level file not found: " + levelFile);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read level file " + levelFile, e);
        }
    }
}
