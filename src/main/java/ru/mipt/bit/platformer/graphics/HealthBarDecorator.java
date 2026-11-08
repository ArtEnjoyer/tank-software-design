package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.model.Destructible;
import ru.mipt.bit.platformer.model.MovingObject;
import ru.mipt.bit.platformer.util.TileMovement;

public class HealthBarDecorator extends RenderableDecorator {

    private static final float BAR_WIDTH = 80f;
    private static final float BAR_HEIGHT = 10f;
    private static final float BAR_LIFT = 52f;
    private static final float BORDER = 2f;
    private static final Color BORDER_COLOR = new Color(0.05f, 0.05f, 0.05f, 1f);
    private static final Color LOST_HEALTH_COLOR = new Color(0.9f, 0.1f, 0.1f, 1f);
    private static final Color HEALTH_COLOR = new Color(0.55f, 1f, 0.15f, 1f);

    private final MovingObject position;
    private final Destructible health;
    private final Texture pixel;
    private final TileMovement tileMovement;
    private final VisibilityToggle visibility;
    private final Rectangle bar = new Rectangle(0f, 0f, BAR_WIDTH, BAR_HEIGHT);
    private final Color batchColor = new Color();

    public <T extends MovingObject & Destructible> HealthBarDecorator(Renderable wrapped, T object, Texture pixel,
                                                                    TileMovement tileMovement, VisibilityToggle visibility) {
        super(wrapped);
        this.position = object;
        this.health = object;
        this.pixel = pixel;
        this.tileMovement = tileMovement;
        this.visibility = visibility;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);
        if (!visibility.isVisible()) {
            return;
        }
        tileMovement.moveRectangleBetweenTileCenters(
                bar, position.getCoordinates(), position.getDestinationCoordinates(), position.getMovementProgress());
        float y = bar.y + BAR_LIFT;

        batchColor.set(batch.getColor());
        batch.setColor(BORDER_COLOR);
        batch.draw(pixel, bar.x - BORDER, y - BORDER, BAR_WIDTH + 2 * BORDER, BAR_HEIGHT + 2 * BORDER);
        batch.setColor(LOST_HEALTH_COLOR);
        batch.draw(pixel, bar.x, y, BAR_WIDTH, BAR_HEIGHT);
        batch.setColor(HEALTH_COLOR);
        batch.draw(pixel, bar.x, y, BAR_WIDTH * health.getHealthFraction(), BAR_HEIGHT);
        batch.setColor(batchColor);
    }
}
