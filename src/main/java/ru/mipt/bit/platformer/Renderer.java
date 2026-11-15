package ru.mipt.bit.platformer;

/**
 * Выходной порт слоя приложения (application): отрисовка кадра
 * и освобождение ресурсов представления без зависимости от графической библиотеки.
 * Адаптер инфраструктуры реализует этот контракт средствами libGDX.
 */
public interface Renderer {

    void render();

    void dispose();
}
