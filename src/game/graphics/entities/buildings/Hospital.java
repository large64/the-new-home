package game.graphics.entities.buildings;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

public class Hospital extends Building {
    private static final Type type = Type.HOSPITAL;

    public Hospital() {
        super();
        this.setModel(Scene.getModelsMap().get("hospitalBuilding"));
    }

    public Hospital(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }
}
