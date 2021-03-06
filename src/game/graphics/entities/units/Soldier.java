package game.graphics.entities.units;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

public class Soldier extends Unit {
    private static final Type type = Type.SOLDIER;

    public Soldier() {
        super();
        this.setModel(Scene.getModelsMap().get("soldierUnit"));
    }

    public Soldier(TexturedModel model, float scale, Side side) {
        super(model, scale, side, type);
    }

    public Soldier(TexturedModel model, Vector3f position, float scale, Side side) {
        super(model, position, scale, type, side);
    }
}
