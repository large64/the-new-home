package game.logic.entities;

import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;

public class RawNeutral extends RawEntity {

    public RawNeutral(Position position) {
        super(position);
        this.side = Side.NEUTRAL;
    }
}
