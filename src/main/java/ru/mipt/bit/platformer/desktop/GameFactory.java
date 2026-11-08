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
import ru.mipt.bit.platformer.graphics.HealthBarDecorator;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.MovableGraphics;
import ru.mipt.bit.platformer.graphics.ObjectGraphicsFactory;
import ru.mipt.bit.platformer.graphics.StaticObjectGraphics;
import ru.mipt.bit.platformer.graphics.Textures;
import ru.mipt.bit.platformer.graphics.ToggleVisibilityCommand;
import ru.mipt.bit.platformer.graphics.VisibilityToggle;
import ru.mipt.bit.platformer.input.CompositeInputController;
import ru.mipt.bit.platformer.input.KeyboardInputController;
import ru.mipt.bit.platformer.input.keyboard.GdxJustPressedKeyboard;
import ru.mipt.bit.platformer.input.keyboard.GdxKeyboard;
import ru.mipt.bit.platformer.level.LevelGenerator;
import ru.mipt.bit.platformer.level.LevelLayout;
import ru.mipt.bit.platformer.model.Battlefield;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Gun;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.ShootCommand;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.L;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static java.util.Collections.singletonMap;
import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class GameFactory {

    private static final String LEVEL_MAP = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";
    private static final String BULLET_TEXTURE = "images/bullet.png";
    private static final String HEALTH_BAR_TEXTURE = "images/pixel.png";

    private static final Direction TANK_START_DIRECTION = Direction.RIGHT;
    private static final int TANK_MIN_HEALTH = 80;
    private static final int TANK_MAX_HEALTH = 100;
    private static final boolean HEALTH_BARS_VISIBLE_AT_START = false;
    private static final float PLAYER_SECONDS_PER_TILE = 0.4f;
    private static final float ENEMY_SECONDS_PER_TILE = 0.6f;
    private static final float PLAYER_RELOAD_SECONDS = 0.5f;
    private static final float ENEMY_RELOAD_SECONDS = 1.5f;
    private static final float BULLET_SECONDS_PER_TILE = 0.1f;
    private static final int BULLET_DAMAGE = 25;

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
        VisibilityToggle healthBars = new VisibilityToggle(HEALTH_BARS_VISIBLE_AT_START);
        ObjectGraphicsFactory graphicsFactory = objectGraphics(textures, groundLayer, tileMovement, healthBars);
        LevelGraphics levelGraphics = new LevelGraphics(
                tiledMap, createSingleLayerMapRenderer(tiledMap, batch), batch, textures, graphicsFactory);

        Level level = new Level(fieldWidth, fieldHeight);
        level.subscribe(levelGraphics);

        CompositeInputController controllers = new CompositeInputController();
        controllers.add(new KeyboardInputController(
                new GdxJustPressedKeyboard(input), singletonMap(L, new ToggleVisibilityCommand(healthBars))));

        Tank player = tank(layout.getPlayerStart(), PLAYER_SECONDS_PER_TILE, PLAYER_RELOAD_SECONDS);
        level.add(player);
        controllers.add(new KeyboardInputController(new GdxKeyboard(input), playerBindings(player, level)));

        for (GridPoint2 coordinates : layout.getEnemyTankCoordinates()) {
            Tank enemy = tank(coordinates, ENEMY_SECONDS_PER_TILE, ENEMY_RELOAD_SECONDS);
            level.add(enemy);
            controllers.add(new RandomAiController(aiCommands(enemy, level), random));
        }

        for (GridPoint2 coordinates : layout.getTreeCoordinates()) {
            level.add(new Tree(coordinates));
        }

        return new Game(controllers, level, levelGraphics);
    }

    private ObjectGraphicsFactory objectGraphics(Textures textures, TiledMapTileLayer groundLayer,
                                                 TileMovement tileMovement, VisibilityToggle healthBars) {
        ObjectGraphicsFactory factory = new ObjectGraphicsFactory();
        factory.register(Tank.class, tank -> new HealthBarDecorator(
                new MovableGraphics(tank, textures.get(TANK_TEXTURE), groundLayer, tileMovement),
                tank, textures.get(HEALTH_BAR_TEXTURE), tileMovement, healthBars));
        factory.register(Bullet.class, bullet -> new MovableGraphics(bullet, textures.get(BULLET_TEXTURE), groundLayer, tileMovement));
        factory.register(Tree.class, tree -> new StaticObjectGraphics(tree, textures.get(TREE_TEXTURE), groundLayer));
        return factory;
    }

    private Tank tank(GridPoint2 coordinates, float secondsPerTile, float reloadSeconds) {
        Gun gun = new Gun(BULLET_SECONDS_PER_TILE, BULLET_DAMAGE, reloadSeconds);
        int health = TANK_MIN_HEALTH + random.nextInt(TANK_MAX_HEALTH - TANK_MIN_HEALTH + 1);
        return new Tank(coordinates, TANK_START_DIRECTION, secondsPerTile, health, gun);
    }

    private Map<Integer, Command> playerBindings(Tank player, Battlefield field) {
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bind(bindings, new ShootCommand(player, field), SPACE);
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

    private List<Command> aiCommands(Tank tank, Battlefield field) {
        List<Command> commands = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            commands.add(new MoveCommand(tank, direction, field));
        }
        commands.add(new ShootCommand(tank, field));
        return commands;
    }
}
