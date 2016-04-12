package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Healer extends Unit {
    public Healer(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Side side) {
        super(model, position, rotX, rotY, rotZ, scale, Type.HEALER, side);
    }
}
