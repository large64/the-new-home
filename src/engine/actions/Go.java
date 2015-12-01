package engine.actions;

import engine.entities.RawEntity;
import engine.entities.units.Unit;

/**
 * Created by large64 on 23/11/15.
 */
public class Go extends Action {
    public Go(Unit unit, RawEntity rawEntity) {
        super(unit, rawEntity);
    }

    @Override
    public void run() {
        unit.goTo(rawEntity.getTilePosition());
    }
}
