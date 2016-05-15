package game.logic.entities.buildings;

import game.logic.toolbox.Side;

public class RawBarrack extends RawBuilding {
    private static final String ID = "barrack";
    private String id = ID + counter;

    public RawBarrack() {
        this.setExtent(new int[]{2, 2});
    }

    public RawBarrack(int row, int column, int health, Side side) {
        super(row, column, health, side);
        this.setExtent(new int[]{2, 2});
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
}
