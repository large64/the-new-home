package engine.actions;

import engine.entities.RawEntity;
import engine.entities.units.Unit;
import engine.exceptions.ImproperActionException;

/**
 * Created by large64 on 2015.11.22..
 */
public class Healing extends Action {
    public Healing(Unit unit, RawEntity rawEntity) {
        super(unit, rawEntity);
    }

    @Override
    public void run() {
        try {
            unit.performAction(rawEntity, ActionType.HEAL);
        } catch (ImproperActionException e) {
            // @TODO: show notification to user
        }
    }
}
