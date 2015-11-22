package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;

/**
 * Created by large64 on 2015.11.22..
 */
public class Attack extends Action {
    public Attack(Unit unit, Entity entity) {
        super(unit, entity);
    }

    @Override
    public void run() {
        unit.performAction(entity, ActionType.ATTACK);
    }
}