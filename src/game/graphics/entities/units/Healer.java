package game.graphics.entities.units;

import game.graphics.models.TexturedModel;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawSoldier;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Healer extends Unit {
    public Healer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public Healer(TexturedModel model, RawHealer rawHealer, float rotX, float rotY, float rotZ, float scale) {
        super(model, rawHealer, rotX, rotY, rotZ, scale);
    }
}
