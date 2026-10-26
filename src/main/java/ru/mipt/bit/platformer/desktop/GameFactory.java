package ru.mipt.bit.platformer.desktop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.Game;
import ru.mipt.bit.platformer.ai.RandomAiController;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.Textures;
import ru.mipt.bit.platformer.input.CompositeInputController;
import ru.mipt.bit.platformer.input.KeyboardInputController;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboard;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.LevelLayout;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameFactory {

    private static final String LEVEL_MAP = "level.tmx";

    private static final Direction TANK_START_DIRECTION = Direction.RIGHT;
    private static final float PLAYER_SECONDS_PER_TILE = 0.4f;
    private static final float ENEMY_SECONDS_PER_TILE = 0.6f;

    private final LevelGenerator levelGenerator;
    private final Random random;

    public GameFactory(LevelGenerator levelGenerator, Random random) {
        this.levelGenerator = levelGenerator;
        this.random = random;
    }

    public Game createGame(Batch batch, Input input) {
        TiledMap tiledMap = new TmxMapLoader().load(LEVEL_MAP);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);
        int fieldWidth = groundLayer.getWidth();
        int fieldHeight = groundLayer.getHeight();
        LevelLayout layout = levelGenerator.generate(fieldWidth, fieldHeight);

        Textures textures = new Textures();
        Level level = new Level(fieldWidth, fieldHeight);
        LevelGraphics levelGraphics = new LevelGraphics(tiledMap, createSingleLayerMapRenderer(tiledMap, batch), batch, textures);
        GameObjectSpawner spawner = new GameObjectSpawner(level, levelGraphics, textures, groundLayer, tileMovement);
        CompositeInputController controllers = new CompositeInputController();

        Tank player = spawner.spawnTank(layout.getPlayerStart(), TANK_START_DIRECTION, PLAYER_SECONDS_PER_TILE);
        controllers.add(new KeyboardInputController(new GdxKeyboard(input), playerBindings(player, level)));

        for (GridPoint2 coordinates : layout.getEnemyTankCoordinates()) {
            Tank enemy = spawner.spawnTank(coordinates, TANK_START_DIRECTION, ENEMY_SECONDS_PER_TILE);
            controllers.add(new RandomAiController(moveCommands(enemy, level), random));
        }

        for (GridPoint2 coordinates : layout.getTreeCoordinates()) {
            spawner.spawnTree(coordinates);
        }

        return new Game(controllers, level, levelGraphics);
    }

    private Map<Integer, Command> playerBindings(Movable player, GameField field) {
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bind(bindings, new MoveCommand(player, Direction.UP, field), UP, W);
        bind(bindings, new MoveCommand(player, Direction.LEFT, field), LEFT, A);
        bind(bindings, new MoveCommand(player, Direction.DOWN, field), DOWN, S);
        bind(bindings, new MoveCommand(player, Direction.RIGHT, field), RIGHT, D);
        return bindings;
    }

    private void bind(Map<Integer, Command> bindings, Command command, int... keys) {
        for (int key : keys) {
            bindings.put(key, command);
        }
    }

    private List<Command> moveCommands(Movable movable, GameField field) {
        List<Command> commands = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            commands.add(new MoveCommand(movable, direction, field));
        }
        return commands;
    }
}
