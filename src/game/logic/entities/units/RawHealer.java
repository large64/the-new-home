package game.logic.entities.units;

/**
 * Created by large64 on 23/11/15.
 */
public class RawHealer extends Unit{
    private static final String ID = "healer";
    private String id = ID + counter;

    public RawHealer(int row, int column, int health, boolean side) {
        super(row, column, health, side);
    }

    /**
     * Provides a string representation of this
     *
     * @return String representation of this
     */
    @Override
    public String toString() {
        return (this.id + ": " + this.getPosition().toString() + " " + this.health + " " + this.side);
    }

    public String getId() {
        return id;
    }
}
