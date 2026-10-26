package ru.mipt.bit.platformer.ai;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.MoveCommand;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomAiControllerTest {

    private static class CountingCommand implements Command {

        private int executions;

        @Override
        public void execute() {
            executions++;
        }
    }

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

    @Test
    void executesTheCommandChosenByRandom() {
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        CountingCommand third = new CountingCommand();
        RandomAiController ai = new RandomAiController(asList(first, second, third), new FixedChoice(1));

        ai.handleInput();

        assertEquals(0, first.executions);
        assertEquals(1, second.executions);
        assertEquals(0, third.executions);
    }

    @Test
    void executesExactlyOneCommandPerTurn() {
        CountingCommand first = new CountingCommand();
        CountingCommand second = new CountingCommand();
        RandomAiController ai = new RandomAiController(asList(first, second), new Random(1));

        for (int turn = 0; turn < 50; turn++) {
            ai.handleInput();
        }

        assertEquals(50, first.executions + second.executions);
    }

    @Test
    void soonerOrLaterTriesEveryCommand() {
        List<CountingCommand> commands = asList(new CountingCommand(), new CountingCommand(), new CountingCommand(), new CountingCommand());
        RandomAiController ai = new RandomAiController(commands, new Random(7));

        for (int turn = 0; turn < 200; turn++) {
            ai.handleInput();
        }

        for (CountingCommand command : commands) {
            assertNotEquals(0, command.executions);
        }
    }

    @Test
    void commandsCannotBeChangedAfterAiIsCreated() {
        CountingCommand original = new CountingCommand();
        CountingCommand added = new CountingCommand();
        List<Command> commands = new ArrayList<>(asList(original));
        RandomAiController ai = new RandomAiController(commands, new FixedChoice(0));

        commands.set(0, added);
        ai.handleInput();

        assertEquals(1, original.executions);
        assertEquals(0, added.executions);
    }

    @Test
    void needsAtLeastOneCommand() {
        assertThrows(IllegalArgumentException.class, () -> new RandomAiController(emptyList(), new Random()));
    }

    @Test
    void aiTankNeverLeavesTheLevelAndNeverEntersObstacles() {
        Level level = new Level(3, 3);
        Tree tree = new Tree(new GridPoint2(1, 1));
        Tank blocker = new Tank(new GridPoint2(2, 2), Direction.RIGHT, 0.4f);
        Tank aiTank = new Tank(new GridPoint2(0, 0), Direction.RIGHT, 0.4f);
        level.add(tree);
        level.add(blocker);
        level.add(aiTank);
        List<Command> moves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            moves.add(new MoveCommand(aiTank, direction, level));
        }
        RandomAiController ai = new RandomAiController(moves, new Random(3));

        boolean visitedAnotherTile = false;
        for (int tick = 0; tick < 500; tick++) {
            ai.handleInput();
            level.update(0.1f);
            GridPoint2 position = aiTank.getCoordinates();
            GridPoint2 destination = aiTank.getDestinationCoordinates();
            for (GridPoint2 tile : asList(position, destination)) {
                assertTrue(tile.x >= 0 && tile.x < 3 && tile.y >= 0 && tile.y < 3, "left the level at " + tile);
                assertFalse(tile.equals(new GridPoint2(1, 1)), "drove into the tree");
                assertFalse(tile.equals(new GridPoint2(2, 2)), "drove into another tank");
            }
            visitedAnotherTile |= !position.equals(new GridPoint2(0, 0));
        }
        assertTrue(visitedAnotherTile);
    }
}
