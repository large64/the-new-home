package game.logic.entities.units;

import game.logic.entities.RawEntity;
import game.logic.toolbox.Side;

public class RawHealer extends RawUnit {
    private static final String ID = "healer";
    private String id = ID + counter;

    public RawHealer() {
    }

    public RawHealer(int row, int column, Side side) {
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

    public void heal(RawEntity selectedEntity) {
        selectedEntity.changeHealth(0.05f);
    }
}
