package game.logic.entities.buildings;

import game.logic.toolbox.Side;

/**
 * Created by Dénes on 2016. 04. 25..
 */
public class RawHome extends RawBuilding {
    private static final String ID = "home";
    private String id = ID + counter;

    public RawHome() {}

    public RawHome(int row, int column, int health, Side side) {
        super(row, column, health, side);
        this.setExtent(new int[] {2, 2});
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

    public void setId(String id) {
        this.id = id;
    }
}
