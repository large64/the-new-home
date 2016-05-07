package game.graphics.entities.buildings;

import game.graphics.entities.Type;
import game.graphics.models.TexturedModel;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 10/10/15.
 */
public class Home extends Building {
    public Home(TexturedModel model, float scale, Side side) {
        super(model, scale, side, Type.HOME);
    }
}
