package ru.mipt.bit.platformer;

import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.control.KeyboardInputHandler;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.model.Level;

public class Game implements Disposable {

    private final Level level;
    private final LevelGraphics levelGraphics;
    private final KeyboardInputHandler inputHandler;

    public Game(Level level, LevelGraphics levelGraphics, KeyboardInputHandler inputHandler) {
        this.level = level;
        this.levelGraphics = levelGraphics;
        this.inputHandler = inputHandler;
    }

    public void update(float deltaTime) {
        inputHandler.handleInput();
        level.update(deltaTime);
    }

    public void render() {
        levelGraphics.render();
    }

    @Override
    public void dispose() {
        levelGraphics.dispose();
    }
}
