package game.graphics.entities.units;

import game.graphics.entities.Entity;
import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

public class Unit extends Entity {
    Unit() {
        this.setScale(1);
    }

    Unit(TexturedModel model, float scale, Side side, Type type) {
        super(model, new Vector3f(0, 0, 0), 0, 0, 0, scale, type, side);
    }

    Unit(TexturedModel model, Vector3f position, float scale, Type type, Side side) {
        super(model, position, 0, 0, 0, scale, type, side);
    }
}
