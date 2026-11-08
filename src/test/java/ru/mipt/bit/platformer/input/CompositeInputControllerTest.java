package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.Command;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeInputControllerTest {

    private final CompositeInputController composite = new CompositeInputController();
    private final Command playerMove = () -> { };
    private final Command playerShoot = () -> { };
    private final Command enemyMove = () -> { };

    @Test
    void createsNothingWithoutControllers() {
        assertTrue(composite.createCommands().isEmpty());
    }

    @Test
    void collectsCommandsOfEveryControllerInTheOrderTheyWereAdded() {
        composite.add(() -> asList(playerShoot, playerMove));
        composite.add(() -> singletonList(enemyMove));

        assertEquals(asList(playerShoot, playerMove, enemyMove), composite.createCommands());
    }

    @Test
    void skipsControllersWithNothingToDo() {
        composite.add(Collections::emptyList);
        composite.add(() -> singletonList(enemyMove));

        assertEquals(singletonList(enemyMove), composite.createCommands());
    }

    @Test
    void controllerAddedWhileCreatingCommandsStartsOnTheNextTurn() {
        composite.add(() -> {
            composite.add(() -> singletonList(enemyMove));
            return singletonList(playerMove);
        });

        assertEquals(singletonList(playerMove), composite.createCommands());
        assertEquals(asList(playerMove, enemyMove), composite.createCommands());
    }
}
