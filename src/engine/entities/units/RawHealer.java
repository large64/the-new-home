package engine.entities.units;

/**
 * Created by large64 on 23/11/15.
 */
public class RawHealer extends Unit{
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
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "healer");
    }
}
