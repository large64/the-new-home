package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;
import engine.exceptions.ImproperActionException;

/**
 * Created by large64 on 2015.11.22..
 */
public class Healing extends Action {
    public Healing(Unit unit, Entity entity) {
        super(unit, entity);
    }

    @Override
    public void run() {
        try {
            unit.performAction(entity, ActionType.HEAL);
        } catch (ImproperActionException ex) {
            System.out.println("Cannot heal hostile entity.");
            // @TODO: show notification to user
        }
    }
}
