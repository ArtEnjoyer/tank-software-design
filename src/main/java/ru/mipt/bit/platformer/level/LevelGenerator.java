package ru.mipt.bit.platformer.level;

/**
 * Выходной порт получения начальной раскладки уровня.
 * По смыслу принадлежит слою приложения (application), хотя сейчас вызывается
 * из инфраструктурного {@code GameFactory}. Реализации скрывают источник
 * раскладки: ресурс в пути классов или процедурную генерацию.
 */
public interface LevelGenerator {

    LevelLayout generate(int fieldWidth, int fieldHeight);
}
