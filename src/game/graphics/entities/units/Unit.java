package game.graphics.entities.units;

import game.graphics.entities.Entity;
import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Unit extends Entity {
    public Unit() {}

    public Unit(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Type type, Side side) {
        super(model, position, rotX, rotY, rotZ, scale, type, side);
    }

    public void refreshPosition() {
        this.position = getRawEntity().getPosition();
        this.rotY = getRawEntity().getRotation();
        this.position.y = Scene.getMainMap().getHeightOfMap(this.position.x, this.position.z);
    }
}
