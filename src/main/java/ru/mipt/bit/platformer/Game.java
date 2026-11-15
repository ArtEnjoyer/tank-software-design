package ru.mipt.bit.platformer;

import java.util.List;

/**
 * Слой приложения (application): координирует игровой цикл.
 * Получает команды через {@link InputController}, выполняет их, обновляет мир
 * через {@link Updatable} и вызывает выходной порт {@link Renderer}.
 * Входные операции пока доступны непосредственно через этот класс: для отделения
 * настольного запуска от реализации приложения можно выделить интерфейс игрового цикла.
 */
public class Game {

    private final InputController inputController;
    private final Updatable world;
    private final Renderer renderer;

    public Game(InputController inputController, Updatable world, Renderer renderer) {
        this.inputController = inputController;
        this.world = world;
        this.renderer = renderer;
    }

    public void update(float deltaTime) {
        List<Command> commands = inputController.createCommands();
        for (Command command : commands) {
            command.execute();
        }
        world.update(deltaTime);
    }

    public void render() {
        renderer.render();
    }

    public void dispose() {
        renderer.dispose();
    }
}
