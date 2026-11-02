package ru.mipt.bit.platformer.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.mipt.bit.platformer.Game;
import ru.mipt.bit.platformer.level.FileLevelGenerator;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.RandomLevelGenerator;
import ru.mipt.bit.platformer.level.RandomTanksLevelGenerator;

import java.util.Random;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class GameDesktopLauncher implements ApplicationListener {

    private static final String LEVEL_FILE = "level.txt";
    private static final String RANDOM_MODE = "random";
    private static final int RANDOM_TREE_COUNT = 12;
    private static final int ENEMY_TANK_COUNT = 3;

    private final LevelGenerator levelGenerator;
    private final Random random;
    private Batch batch;
    private Game game;

    public GameDesktopLauncher(LevelGenerator levelGenerator, Random random) {
        this.levelGenerator = levelGenerator;
        this.random = random;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        game = new GameFactory(levelGenerator, random).createGame(batch, Gdx.input);
    }

    @Override
    public void render() {
        clearScreen();
        game.update(Gdx.graphics.getDeltaTime());
        game.render();
    }

    @Override
    public void resize(int width, int height) {
        // do not react to window resizing
    }

    @Override
    public void pause() {
        // game doesn't get paused
    }

    @Override
    public void resume() {
        // game doesn't get paused
    }

    @Override
    public void dispose() {
        // dispose of all the native resources (classes which implement com.badlogic.gdx.utils.Disposable)
        game.dispose();
        batch.dispose();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        // level width: 10 tiles x 128px, height: 8 tiles x 128px
        config.setWindowedMode(1280, 1024);
        Random random = new Random();
        new Lwjgl3Application(new GameDesktopLauncher(createGenerator(args, random), random), config);
    }

    private static LevelGenerator createGenerator(String[] args, Random random) {
        LevelGenerator obstaclesGenerator = args.length > 0 && RANDOM_MODE.equals(args[0])
                ? new RandomLevelGenerator(RANDOM_TREE_COUNT, random)
                : new FileLevelGenerator(LEVEL_FILE);
        return new RandomTanksLevelGenerator(obstaclesGenerator, ENEMY_TANK_COUNT, random);
    }
}
