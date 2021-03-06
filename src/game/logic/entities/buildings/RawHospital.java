package game.logic.entities.buildings;

import game.logic.toolbox.Side;

public class RawHospital extends RawBuilding {
    private static final String ID = "hospital";
    private String id = ID + counter;

    public RawHospital() {
        this.setExtent(new int[]{2, 3});
    }

    public RawHospital(int row, int column, int health, Side side) {
        super(row, column, health, side);
        this.setExtent(new int[]{2, 3});
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
