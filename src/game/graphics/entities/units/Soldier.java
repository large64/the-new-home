package game.graphics.entities.units;

import game.logic.entities.units.RawSoldier;
import game.graphics.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Soldier extends Unit {
    public Soldier(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public Soldier(TexturedModel model, RawSoldier rawSoldier, float rotX, float rotY, float rotZ, float scale) {
        super(model, rawSoldier, rotX, rotY, rotZ, scale);
    }
}
