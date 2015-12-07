package game.graphics.entities.units;

import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Map;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawSoldier;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Healer extends Unit {
    public Healer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.setRawEntity(new RawHealer(((int) position.x), ((int) position.z), true));
    }
}
