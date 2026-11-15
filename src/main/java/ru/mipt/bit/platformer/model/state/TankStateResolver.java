package ru.mipt.bit.platformer.model.state;

/**
 * Доменная политика (domain): выбирает {@link TankState} по доле здоровья.
 * Изолирует танк от конкретных порогов смены состояния.
 * Это внутренний интерфейс правил игры, а не порт границы с инфраструктурой.
 */
public interface TankStateResolver {

    TankState resolve(float healthFraction);
}

