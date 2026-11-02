package ru.mipt.bit.platformer;

import java.util.List;

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
