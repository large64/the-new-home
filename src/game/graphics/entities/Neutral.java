package game.graphics.entities;

import game.graphics.models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;

public class Neutral extends Entity {
    public Neutral(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }
}
