package game.logic.entities.units;

import game.logic.toolbox.Side;

public class RawScientist extends RawUnit {
    private static final String ID = "scientist";
    private String id = ID + counter;

    public RawScientist(int row, int column, Side side) {
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
}
