package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

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

    private static class Shooter extends Brick {

        private final Level level;
        private final Brick bullet;

        Shooter(GridPoint2 coordinates, Level level, Brick bullet) {
            super(coordinates);
            this.level = level;
            this.bullet = bullet;
        }

        @Override
        public void update(float deltaTime) {
            super.update(deltaTime);
            level.add(bullet);
        }
    }

    private static final int WIDTH = 10;
    private static final int HEIGHT = 8;

    private final Level level = new Level(WIDTH, HEIGHT);

    private static Tank tankAt(int x, int y) {
        return new Tank(new GridPoint2(x, y), Direction.RIGHT, TIME_TO_PASS_A_TILE);
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
        level.add(new Shooter(new GridPoint2(1, 1), level, bullet));

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
}
