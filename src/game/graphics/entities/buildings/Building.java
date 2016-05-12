package game.graphics.entities.buildings;

import game.graphics.entities.Entity;
import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Building extends Entity {
    public Building() {
        this.setScale(1);
    }

    public Building(TexturedModel model, float scale, Side side, Type type) {
        super(model, new Vector3f(0, 0, 0), 0, 0, 0, scale, type, side);
    }

    public Building(TexturedModel model, Vector3f position, float scale, Type type, Side side) {
        super(model, position, 0, 0, 0, scale, type, side);
    }
}
