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
        LevelLayout fileLayout = parser.parse(readLines());
        if (fileLayout.getWidth() > fieldWidth || fileLayout.getHeight() > fieldHeight) {
            throw new IllegalArgumentException("Level " + levelFile + " of size " + fileLayout.getWidth() + "x" + fileLayout.getHeight()
                    + " does not fit into field " + fieldWidth + "x" + fieldHeight);
        }
        return new LevelLayout(fieldWidth, fieldHeight, fileLayout.getPlayerStart(), fileLayout.getTreeCoordinates());
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
