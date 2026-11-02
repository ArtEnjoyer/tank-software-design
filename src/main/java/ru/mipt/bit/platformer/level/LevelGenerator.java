package ru.mipt.bit.platformer.level;

public interface LevelGenerator {

    LevelLayout generate(int fieldWidth, int fieldHeight);
}
