package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisibilityToggleTest {

    @Test
    void startsInTheGivenState() {
        assertFalse(new VisibilityToggle(false).isVisible());
        assertTrue(new VisibilityToggle(true).isVisible());
    }

    @Test
    void switchesOnAndOffByTurns() {
        VisibilityToggle toggle = new VisibilityToggle(false);

        toggle.toggle();
        assertTrue(toggle.isVisible());

        toggle.toggle();
        assertFalse(toggle.isVisible());
    }
}
