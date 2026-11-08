package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float TIME_TO_PASS_A_TILE = 0.4f;

    private static class Brick implements GameObject {

        private final GridPoint2 coordinates;
        private float timeLived;

        Brick(GridPoint2 coordinates) {
            this.coordinates = coordinates;
        }

        @Override
        public GridPoint2 getCoordinates() {
            return new GridPoint2(coordinates);
        }

        @Override
        public boolean occupies(GridPoint2 tileCoordinates) {
            return coordinates.equals(tileCoordinates);
        }

        @Override
        public void update(float deltaTime) {
            timeLived += deltaTime;
        }
    }

    private static class Spawner extends Brick {

        private final Level level;
        private final Brick spawned;

        Spawner(GridPoint2 coordinates, Level level, Brick spawned) {
            super(coordinates);
            this.level = level;
            this.spawned = spawned;
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            level.add(spawned);
        }
    }

    private static class SelfRemover extends Brick {

        private final Level level;
        private final Brick victim;

        SelfRemover(GridPoint2 coordinates, Level level, Brick victim) {
            super(coordinates);
            this.level = level;
            this.victim = victim;
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            level.remove(victim);
            level.remove(this);
        }
    }

    private static class RecordingListener implements LevelListener {

        private final List<String> events = new ArrayList<>();

        @Override
        public void onObjectAdded(GameObject object) {
            events.add("added " + object.getCoordinates());
        }

        @Override
        public void onObjectRemoved(GameObject object) {
            events.add("removed " + object.getCoordinates());
        }
    }

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    private final Level level = new Level(WIDTH, HEIGHT);

    private static Tank tankAt(int x, int y) {
        return TestTanks.tankAt(x, y);
    }

    @Test
    void emptyLevelIsFreeEverywhere() {
        assertTrue(level.isFree(new GridPoint2(0, 0)));
        assertTrue(level.isFree(new GridPoint2(5, 5)));
    }

    @Test
    void tileWithoutObjectsIsFree() {
        level.add(new Tree(new GridPoint2(1, 3)));

        assertTrue(level.isFree(new GridPoint2(5, 5)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        level.add(new Tree(new GridPoint2(1, 3)));

        assertFalse(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void tileWithTankIsNotFree() {
        level.add(tankAt(1, 1));

        assertFalse(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void newKindOfObjectWorksWithoutChangingLevel() {
        Brick brick = new Brick(new GridPoint2(4, 4));
        level.add(brick);

        level.update(1.5f);

        assertFalse(level.isFree(new GridPoint2(4, 4)));
        assertEquals(1.5f, brick.timeLived, 1e-6f);
    }

    @Test
    void objectAddedDuringGameTakesPartFromThatMoment() {
        level.update(1f);
        Brick bullet = new Brick(new GridPoint2(3, 3));

        level.add(bullet);
        level.update(0.5f);

        assertFalse(level.isFree(new GridPoint2(3, 3)));
        assertEquals(0.5f, bullet.timeLived, 1e-6f);
    }

    @Test
    void objectMayAddAnotherObjectWhileBeingUpdated() {
        Brick bullet = new Brick(new GridPoint2(2, 1));
        level.add(new Spawner(new GridPoint2(1, 1), level, bullet));

        assertDoesNotThrow(() -> level.update(0.5f));

        assertFalse(level.isFree(new GridPoint2(2, 1)));
        assertEquals(0f, bullet.timeLived, 1e-6f);

        level.update(0.5f);

        assertEquals(0.5f, bullet.timeLived, 1e-6f);
    }

    @Test
    void tankDoesNotDriveThroughTree() {
        Tank tank = tankAt(1, 1);
        level.add(tank);
        level.add(new Tree(new GridPoint2(1, 2)));

        tank.move(Direction.UP, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void tankDoesNotDriveThroughAnotherTank() {
        Tank player = tankAt(1, 1);
        level.add(player);
        level.add(tankAt(2, 1));

        player.move(Direction.RIGHT, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 1), player.getCoordinates());
    }

    @Test
    void updateMovesEveryTankOfTheLevel() {
        Tank first = tankAt(1, 1);
        Tank second = tankAt(5, 5);
        level.add(first);
        level.add(second);

        first.move(Direction.UP, level);
        second.move(Direction.LEFT, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 2), first.getCoordinates());
        assertEquals(new GridPoint2(4, 5), second.getCoordinates());
    }

    @Test
    void tilesOutsideTheLevelAreNeverFree() {
        assertFalse(level.isFree(new GridPoint2(-1, 0)));
        assertFalse(level.isFree(new GridPoint2(0, -1)));
        assertFalse(level.isFree(new GridPoint2(WIDTH, 0)));
        assertFalse(level.isFree(new GridPoint2(0, HEIGHT)));
        assertTrue(level.isFree(new GridPoint2(WIDTH - 1, HEIGHT - 1)));
    }

    @Test
    void tankStopsAtTheBorderLikeAtAnObstacle() {
        Tank tank = tankAt(0, 0);
        level.add(tank);

        tank.move(Direction.LEFT, level);
        level.update(TIME_TO_PASS_A_TILE);
        tank.move(Direction.DOWN, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(0, 0), tank.getCoordinates());
        assertEquals(Direction.DOWN, tank.getDirection());
    }

    @Test
    void tankStopsAtTheFarBorders() {
        Tank tank = tankAt(WIDTH - 1, HEIGHT - 1);
        level.add(tank);

        tank.move(Direction.RIGHT, level);
        level.update(TIME_TO_PASS_A_TILE);
        tank.move(Direction.UP, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(WIDTH - 1, HEIGHT - 1), tank.getCoordinates());
    }

    @Test
    void movingTankKeepsBothItsTilesForItself() {
        Tank mover = tankAt(1, 1);
        Tank fromBehind = tankAt(0, 1);
        Tank fromAhead = tankAt(3, 1);
        level.add(mover);
        level.add(fromBehind);
        level.add(fromAhead);

        mover.move(Direction.RIGHT, level);
        level.update(TIME_TO_PASS_A_TILE / 2);
        fromBehind.move(Direction.RIGHT, level);
        fromAhead.move(Direction.LEFT, level);
        level.update(TIME_TO_PASS_A_TILE / 4);

        assertFalse(fromBehind.isMoving());
        assertFalse(fromAhead.isMoving());
        assertEquals(new GridPoint2(0, 1), fromBehind.getCoordinates());
        assertEquals(new GridPoint2(3, 1), fromAhead.getCoordinates());
    }

    @Test
    void twoTanksCannotStartMovingIntoTheSameTile() {
        Tank left = tankAt(0, 1);
        Tank right = tankAt(2, 1);
        level.add(left);
        level.add(right);

        left.move(Direction.RIGHT, level);
        right.move(Direction.LEFT, level);

        assertTrue(left.isMoving());
        assertFalse(right.isMoving());
    }

    @Test
    void cannotBeCreatedWithNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new Level(0, 8));
        assertThrows(IllegalArgumentException.class, () -> new Level(10, -1));
    }

    @Test
    void tellsSubscribersAboutEveryAddedObject() {
        RecordingListener listener = new RecordingListener();
        level.subscribe(listener);

        level.add(new Tree(new GridPoint2(1, 3)));
        level.add(tankAt(2, 2));

        assertEquals(asList("added (1, 3)", "added (2, 2)"), listener.events);
    }

    @Test
    void tellsSubscribersAboutRemovedObjects() {
        RecordingListener listener = new RecordingListener();
        Tree tree = new Tree(new GridPoint2(1, 3));
        level.add(tree);
        level.subscribe(listener);

        level.remove(tree);

        assertEquals(singletonList("removed (1, 3)"), listener.events);
        assertTrue(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void removingAnObjectTwiceNotifiesOnlyOnce() {
        RecordingListener listener = new RecordingListener();
        Tree tree = new Tree(new GridPoint2(1, 3));
        level.add(tree);
        level.subscribe(listener);

        level.remove(tree);
        level.remove(tree);
        level.remove(new Tree(new GridPoint2(5, 5)));

        assertEquals(singletonList("removed (1, 3)"), listener.events);
    }

    @Test
    void everySubscriberIsNotified() {
        RecordingListener first = new RecordingListener();
        RecordingListener second = new RecordingListener();
        level.subscribe(first);
        level.subscribe(second);

        level.add(new Tree(new GridPoint2(0, 0)));

        assertEquals(singletonList("added (0, 0)"), first.events);
        assertEquals(singletonList("added (0, 0)"), second.events);
    }

    @Test
    void unsubscribedListenerHearsNothing() {
        RecordingListener listener = new RecordingListener();
        level.subscribe(listener);
        level.unsubscribe(listener);

        level.add(new Tree(new GridPoint2(0, 0)));

        assertTrue(listener.events.isEmpty());
    }

    @Test
    void objectsAddedBeforeSubscriptionAreNotRepeated() {
        level.add(new Tree(new GridPoint2(0, 0)));
        RecordingListener listener = new RecordingListener();
        level.subscribe(listener);

        level.add(new Tree(new GridPoint2(1, 0)));

        assertEquals(singletonList("added (1, 0)"), listener.events);
    }

    @Test
    void objectRemovedDuringUpdateIsNotUpdatedAnymore() {
        Brick victim = new Brick(new GridPoint2(5, 5));
        level.add(new SelfRemover(new GridPoint2(1, 1), level, victim));
        level.add(victim);

        level.update(1f);
        level.update(1f);

        assertEquals(0f, victim.timeLived, 1e-6f);
        assertTrue(level.isFree(new GridPoint2(5, 5)));
        assertTrue(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void findsTheObjectStandingOnATile() {
        Tree tree = new Tree(new GridPoint2(1, 3));
        level.add(tree);

        assertEquals(tree, level.objectAt(new GridPoint2(1, 3)).orElse(null));
        assertFalse(level.objectAt(new GridPoint2(2, 3)).isPresent());
    }

    @Test
    void knowsItsBorders() {
        assertTrue(level.isInside(new GridPoint2(0, 0)));
        assertTrue(level.isInside(new GridPoint2(WIDTH - 1, HEIGHT - 1)));
        assertFalse(level.isInside(new GridPoint2(WIDTH, 0)));
        assertFalse(level.isInside(new GridPoint2(0, -1)));
    }
}
