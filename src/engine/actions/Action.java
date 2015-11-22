package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;

/**
 * Created by large64 on 2015.11.22..
 */
public abstract class Action implements Runnable {
    Unit unit;
    Entity entity;

    public Action(Unit unit, Entity entity) {
        this.unit = unit;
        this.entity = entity;
    }
}
