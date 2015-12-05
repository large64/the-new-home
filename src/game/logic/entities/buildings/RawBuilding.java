package game.logic.entities.buildings;

import game.logic.entities.RawEntity;
import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class RawBuilding extends RawEntity {
    public RawBuilding(int row, int column, int health, boolean side) {
        super(new Position(row, column));
        this.setHealth(health);
        this.setSide(side);
    }

    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "building");
    }
}
