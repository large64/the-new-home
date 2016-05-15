package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

public class Scientist extends Unit {
    private static final Type type = Type.SCIENTIST;

    public Scientist() {
        super();
        this.setModel(Scene.getModelsMap().get("scientistUnit"));
    }

    public Scientist(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }

    public Scientist(TexturedModel model, Vector3f position, float scale, Side side) {
        super(model, position, scale, type, side);
    }
}
