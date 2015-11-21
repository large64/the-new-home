package engine.entities.buildings;

import engine.entities.Entity;
import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Building extends Entity {
    public Building(int row, int column, int health) {
        super(new Position(row, column));
        this.setHealth(health);
    }

    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "building");
    }
}
