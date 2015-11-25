package engine.actions;

import engine.entities.RawEntity;
import engine.entities.units.Unit;

/**
 * Created by large64 on 2015.11.22..
 */
public abstract class Action implements Runnable {
    Unit unit;
    RawEntity rawEntity;

    public Action(Unit unit, RawEntity rawEntity) {
        this.unit = unit;
        this.rawEntity = rawEntity;
    }
}
