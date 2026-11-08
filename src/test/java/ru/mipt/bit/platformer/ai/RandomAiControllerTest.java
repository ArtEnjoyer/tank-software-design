package ru.mipt.bit.platformer.ai;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.LevelListener;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.ShootCommand;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class RandomAiControllerTest {

    private static class FixedChoice extends Random {

        private final int index;

        FixedChoice(int index) {
            this.index = index;
        }

        @Override
        public int nextInt(int bound) {
            return index;
        }
    }

    private final Command first = () -> { };
    private final Command second = () -> { };
    private final Command third = () -> { };

    @Test
    void createsTheCommandChosenByRandom() {
        RandomAiController ai = new RandomAiController(asList(first, second, third), new FixedChoice(1));

        assertEquals(singletonList(second), ai.createCommands());
    }

    @Test
    void createsExactlyOneCommandPerTurn() {
        RandomAiController ai = new RandomAiController(asList(first, second), new Random(1));

        for (int turn = 0; turn < 50; turn++) {
            assertEquals(1, ai.createCommands().size());
        }
    }

    @Test
    void soonerOrLaterChoosesEveryCommand() {
        RandomAiController ai = new RandomAiController(asList(first, second, third), new Random(7));
        Set<Command> chosen = new HashSet<>();

        for (int turn = 0; turn < 200; turn++) {
            chosen.addAll(ai.createCommands());
        }

        assertEquals(new HashSet<>(asList(first, second, third)), chosen);
    }

    @Test
    void commandsCannotBeChangedAfterAiIsCreated() {
        List<Command> commands = new ArrayList<>(singletonList(first));
        RandomAiController ai = new RandomAiController(commands, new FixedChoice(0));

        commands.set(0, second);

        assertEquals(singletonList(first), ai.createCommands());
    }

    @Test
    void needsAtLeastOneCommand() {
        assertThrows(IllegalArgumentException.class, () -> new RandomAiController(emptyList(), new Random()));
    }

    @Test
    void aiTankDrivesAndShootsButNeverLeavesTheLevelOrEntersObstacles() {
        Level level = new Level(3, 3);
        List<GameObject> added = new ArrayList<>();
        level.subscribe(new LevelListener() {
            @Override
            public void onObjectAdded(GameObject object) {
                added.add(object);
            }

            @Override
            public void onObjectRemoved(GameObject object) {
            }
        });
        Tank aiTank = tankAt(0, 0);
        level.add(new Tree(new GridPoint2(1, 1)));
        level.add(new Tree(new GridPoint2(2, 2)));
        level.add(aiTank);
        List<Command> commands = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            commands.add(new MoveCommand(aiTank, direction, level));
        }
        commands.add(new ShootCommand(aiTank, level));
        RandomAiController ai = new RandomAiController(commands, new Random(3));

        boolean visitedAnotherTile = false;
        for (int tick = 0; tick < 500; tick++) {
            for (Command command : ai.createCommands()) {
                command.execute();
            }
            level.update(0.1f);
            for (GridPoint2 tile : asList(aiTank.getCoordinates(), aiTank.getDestinationCoordinates())) {
                assertTrue(level.isInside(tile), "left the level at " + tile);
                assertFalse(tile.equals(new GridPoint2(1, 1)), "drove into a tree");
                assertFalse(tile.equals(new GridPoint2(2, 2)), "drove into a tree");
            }
            visitedAnotherTile |= !aiTank.getCoordinates().equals(new GridPoint2(0, 0));
        }

        assertTrue(visitedAnotherTile);
        assertTrue(added.stream().anyMatch(object -> object instanceof Bullet));
        assertTrue(aiTank.isAlive());
    }
}
