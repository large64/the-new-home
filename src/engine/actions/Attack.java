package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;
import engine.exceptions.ImproperActionException;

/**
 * Created by large64 on 2015.11.22..
 */
public class Attack extends Action {
    public Attack(Unit unit, Entity entity) {
        super(unit, entity);
    }

    @Override
    public void run() {
        try {
            unit.performAction(entity, ActionType.ATTACK);
        } catch (ImproperActionException e) {
            // @TODO: show notification to user
            // @TODO: add fields to exception to know the problem
        }
    }
}