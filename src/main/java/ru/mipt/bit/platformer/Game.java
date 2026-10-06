package ru.mipt.bit.platformer;

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
        inputController.handleInput();
        world.update(deltaTime);
    }

    public void render() {
        renderer.render();
    }

    public void dispose() {
        renderer.dispose();
    }
}
