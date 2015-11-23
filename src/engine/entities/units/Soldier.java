package engine.entities.units;

/**
 * Created by large64 on 23/11/15.
 */
public class Soldier extends Unit{
    public Soldier(int row, int column, int health, boolean side) {
        super(row, column, health, side);
    }

    /**
     * Provides a string representation of this
     *
     * @return String representation of this
     */
    @Override
    public String toString() {
        return "soldier" + counter;
    }
}
