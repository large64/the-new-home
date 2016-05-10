package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Scientist extends Unit {
    public Scientist() {
        super();
        this.setModel(Scene.getModelsMap().get("scientistUnit"));
    }

    public Scientist(TexturedModel model, float scale, Side side) {
        super(model, scale, side, Type.SCIENTIST);
    }
}
