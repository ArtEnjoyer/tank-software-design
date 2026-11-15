package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.Command;

/**
 * Сценарий слоя приложения (application), физически расположенный в model.
 * Реализует {@code Command} и вызывает доменную операцию {@link Shooter}.
 * Правила выстрела остаются в domain; команда не является адаптером клавиатуры.
 */
public class ShootCommand implements Command {

    private final Shooter shooter;
    private final Battlefield field;

    public ShootCommand(Shooter shooter, Battlefield field) {
        this.shooter = shooter;
        this.field = field;
    }

    @Override
    public void execute() {
        shooter.shoot(field);
    }
}
