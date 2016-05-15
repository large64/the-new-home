package game.graphics.entities.buildings;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

public class Home extends Building {
    private static final Type type = Type.HOME;

    public Home() {
        super();
        this.setModel(Scene.getModelsMap().get("homeBuilding"));
    }

    public Home(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }

    public Home(TexturedModel model, Vector3f position, float scale, Side side) {
        super(model, position, scale, type, side);
    }
}
