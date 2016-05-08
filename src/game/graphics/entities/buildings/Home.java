package game.graphics.entities.buildings;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 10/10/15.
 */
public class Home extends Building {
    public Home() {
        super();
        this.setModel(Scene.getModelsMap().get("homeBuilding"));
    }

    public Home(TexturedModel model, float scale, Side side) {
        super(model, scale, side, Type.HOME);
    }
}
