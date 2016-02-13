package game.logic.entities;

import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;

/**
 * Created by large64 on 2015.12.06..
 */
public class Neutral extends RawEntity {

    public Neutral(Position position) {
        super(position);
        //super.tilePosition.y = Map.getHeightOfMap(position.x, position.z);
        this.side = Side.NEUTRAL;
    }
}
