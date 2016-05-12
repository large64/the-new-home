package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 10/10/15.
 */
public class Healer extends Unit {
    private static final Type type = Type.HEALER;

    public Healer() {
        super();
        this.setModel(Scene.getModelsMap().get("healerUnit"));
    }

    public Healer(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }
}
