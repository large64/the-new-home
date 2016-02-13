package game.logic.entities.units;

/**
 * Created by large64 on 23/11/15.
 */
public class RawSoldier extends RawUnit {
    private static final String ID = "soldier";
    private String id = ID + counter;

    public RawSoldier(int row, int column, boolean side) {
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
