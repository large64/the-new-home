package game.graphics.entities.buildings;

import game.graphics.entities.Entity;
import game.graphics.models.TexturedModel;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 10/10/15.
 */
public class Building extends Entity {
    public Building() {}

    public Building(TexturedModel model, float scale, Side side) {
        super(model, scale, side);
    }
}
