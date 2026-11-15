package ru.mipt.bit.platformer.model.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Реализация доменной политики (domain) выбора состояния по диапазонам здоровья.
 * По умолчанию: лёгкое состояние выше 70%, среднее от 15% до 70% включительно,
 * тяжёлое ниже 15%. Дополнительные диапазоны передаются через конструктор.
 * Не является инфраструктурным адаптером: реализует правила внутри domain.
 */
public final class HealthBasedTankStateResolver implements TankStateResolver {


    private final List<HealthRangeTankState> states;

    public HealthBasedTankStateResolver(Collection<HealthRangeTankState> states) {
        if (states.isEmpty()) {
            throw new IllegalArgumentException("At least one health state is required");
        }
        this.states = new ArrayList<>(states);
    }

    public static HealthBasedTankStateResolver defaultDamageStates() {
        return new HealthBasedTankStateResolver(Arrays.asList(
                new HealthRangeTankState(0.7f, false, 1f, true, LightTankState.INSTANCE),
                new HealthRangeTankState(0.15f, true, 0.7f, true, MediumTankState.INSTANCE),
                new HealthRangeTankState(Float.NEGATIVE_INFINITY, true, 0.15f, false, HeavyTankState.INSTANCE)
        ));
    }

    @Override
    public TankState resolve(float healthFraction) {
        for (HealthRangeTankState state : states) {
            if (state.matches(healthFraction)) {
                return state.behavior();
            }
        }
        throw new IllegalStateException("No tank state matches health fraction " + healthFraction);
    }
}

