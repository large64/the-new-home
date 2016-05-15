package game.graphics.entities.buildings;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

public class Barrack extends Building {
    private static final Type type = Type.BARRACK;

    public Barrack() {
        super();
        this.setModel(Scene.getModelsMap().get("barrackBuilding"));
    }

    public Barrack(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }
}
