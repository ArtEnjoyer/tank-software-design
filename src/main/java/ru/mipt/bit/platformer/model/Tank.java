package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.state.HealthBasedTankStateResolver;
import ru.mipt.bit.platformer.model.state.TankState;
import ru.mipt.bit.platformer.model.state.TankStateResolver;

import java.util.Objects;


/**
 * Доменная сущность (domain): движение, здоровье и стрельба танка.
 * Делегирует зависящее от состояния поведение контракту {@code TankState},
 * а выбор состояния по здоровью — доменной политике {@code TankStateResolver}.
 * Не является адаптером; графика и устройства ввода в правилах танка не участвуют.
 */
public class Tank implements Movable, Shooter, Destructible {

    private final float secondsPerTile;
    private final Gun gun;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private final int maxHealth;
    private final TankStateResolver stateResolver;
    private Direction direction;
    private float movementProgress = 1f;
    private int health;
    private TankState state;

    public Tank(GridPoint2 coordinates, Direction direction, float secondsPerTile, int health, Gun gun) {
        this(coordinates, direction, secondsPerTile, health, gun, HealthBasedTankStateResolver.defaultDamageStates());
    }

    public Tank(GridPoint2 coordinates, Direction direction, float secondsPerTile, int health, Gun gun,
                TankStateResolver stateResolver) {
        if (secondsPerTile <= 0f) {
            throw new IllegalArgumentException("Seconds per tile must be positive");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive, got " + health);
        }
        this.secondsPerTile = secondsPerTile;
        this.gun = gun;
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.health = health;
        this.maxHealth = health;
        this.stateResolver = stateResolver;
        this.state = stateResolver.resolve(healthFraction());
    }

    @Override
    public void move(Direction toDirection, GameField field) {
        if (!isAlive() || isMoving()) {
            return;
        }
        direction = toDirection;
        GridPoint2 neighbour = toDirection.neighbourOf(coordinates);
        if (field.isFree(neighbour)) {
            destinationCoordinates.set(neighbour);
            movementProgress = 0f;
        }
    }

    @Override
    public void shoot(Battlefield field) {
        if (!isAlive() || isMoving() || !state.canShoot()) {
            return;
        }
        gun.fire(coordinates, direction, field);
    }

    @Override
    public void update(float deltaTime) {
        gun.update(deltaTime);
        if (!isMoving()) {
            return;
        }
        float movementDuration = secondsPerTile * state.movementDurationMultiplier();
        movementProgress = Math.min(1f, movementProgress + deltaTime / movementDuration);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        refreshStateFromHealth();
    }

    public void changeState(TankState state) {
        this.state = Objects.requireNonNull(state);
    }


    public void refreshStateFromHealth() {
        state = stateResolver.resolve(healthFraction());
    }

    public TankState getState() {
        return state;
    }

    public float getHealthFraction() {
        if (health <= 0) {
            return 0f;
        }
        return (float) health / (float) maxHealth;
    }

    private float healthFraction() {
        return getHealthFraction();
    }


    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public boolean occupies(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates) || destinationCoordinates.equals(tileCoordinates);
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth;
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }
}
