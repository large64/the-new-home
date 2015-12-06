package game.graphics.entities.units;

import game.graphics.windowparts.Map;
import game.logic.entities.units.RawSoldier;
import game.graphics.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Soldier extends Unit {
    public Soldier(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.setRawEntity(new RawSoldier(((int) position.x), ((int) position.z), true));
        this.position.y = Map.getHeightOfMap(position.x, position.z);
    }
}
