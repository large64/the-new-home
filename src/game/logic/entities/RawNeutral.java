package game.logic.entities;

import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;

/**
 * Created by large64 on 2015.12.06..
 */
public class RawNeutral extends RawEntity {

    public RawNeutral(Position position) {
        super(position);
        this.side = Side.NEUTRAL;
    }
}
