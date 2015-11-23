package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;
import engine.exceptions.ImproperActionException;

/**
 * Created by large64 on 23/11/15.
 */
public class Go extends Action {
    public Go(Unit unit, Entity entity) {
        super(unit, entity);
    }

    @Override
    public void run() {
        unit.goTo(entity);
    }
}
