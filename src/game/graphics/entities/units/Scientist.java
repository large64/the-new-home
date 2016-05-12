package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 10/10/15.
 */
public class Scientist extends Unit {
    private static final Type type = Type.SCIENTIST;

    public Scientist() {
        super();
        this.setModel(Scene.getModelsMap().get("scientistUnit"));
    }

    public Scientist(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }
}
