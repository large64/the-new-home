package engine.actions;

import engine.entities.RawEntity;
import engine.entities.units.Unit;
import engine.toolbox.Tile;

/**
 * Created by large64 on 23/11/15.
 */
public class Go extends Action {
    public Go(Unit unit, Tile tile) {
        super(unit, tile);
    }

    @Override
    public void run() {
        unit.goTo(this.tile);
    }
}
