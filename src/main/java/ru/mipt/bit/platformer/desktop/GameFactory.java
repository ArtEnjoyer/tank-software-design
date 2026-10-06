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
import ru.mipt.bit.platformer.InputController;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.MovableGraphics;
import ru.mipt.bit.platformer.graphics.StaticObjectGraphics;
import ru.mipt.bit.platformer.input.KeyboardInputController;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboard;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private static final GridPoint2 PLAYER_START = new GridPoint2(1, 1);
    private static final Direction PLAYER_START_DIRECTION = Direction.RIGHT;
    private static final float TANK_SECONDS_PER_TILE = 0.4f;
    private static final GridPoint2 TREE_PLACE = new GridPoint2(1, 3);

    public Game createGame(Batch batch, Input input) {
        TiledMap tiledMap = new TmxMapLoader().load(LEVEL_MAP);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        TileMovement tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        Level level = new Level();
        LevelGraphics levelGraphics = new LevelGraphics(tiledMap, createSingleLayerMapRenderer(tiledMap, batch), batch);

        Tank player = new Tank(PLAYER_START, PLAYER_START_DIRECTION, TANK_SECONDS_PER_TILE);
        level.add(player);
        levelGraphics.add(new MovableGraphics(player, TANK_TEXTURE, groundLayer, tileMovement));

        Tree tree = new Tree(TREE_PLACE);
        level.add(tree);
        levelGraphics.add(new StaticObjectGraphics(tree, TREE_TEXTURE, groundLayer));

        InputController inputController = new KeyboardInputController(new GdxKeyboard(input), playerBindings(player, level));

        return new Game(inputController, level, levelGraphics);
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
}
