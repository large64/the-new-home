package game.logic.entities.units;

import game.logic.entities.RawEntity;
import game.logic.toolbox.Side;

/**
 * Created by large64 on 23/11/15.
 */
public class RawSoldier extends RawUnit {
    private static final String ID = "soldier";
    private String id = ID + counter;

    public RawSoldier() {
    }

    public RawSoldier(int row, int column, Side side) {
        super(row, column, side);
    }

    /**
     * Provides a string representation of this
     *
     * @return String representation of this
     */
    @Override
    public String toString() {
        return (this.id + ": " + this.getTilePosition().toString() + " " + this.health + " " + this.side);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void attack(RawEntity selectedEntity) {
        selectedEntity.changeHealth(-0.5f);
    }
}
