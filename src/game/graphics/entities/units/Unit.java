package game.graphics.entities.units;

import game.logic.entities.RawEntity;
import game.logic.entities.units.RawSoldier;
import game.graphics.entities.Entity;
import game.graphics.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import game.graphics.windowparts.Map;

/**
 * Created by large64 on 10/10/15.
 */
public class Unit extends Entity {
    public Unit() {}

    public Unit(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public void refreshPosition() {
        this.position = getRawEntity().getPosition();
    }
}