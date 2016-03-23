package game.graphics.entities.units;

import game.graphics.entities.Entity;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Map;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Unit extends Entity {
    public Unit() {}

    public Unit(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void refreshPosition(Map map) {
        this.position = getRawEntity().getPosition();
        this.rotY = getRawEntity().getRotation();
        this.position.y = map.getHeightOfMap(this.position.x, this.position.z);
    }
}
