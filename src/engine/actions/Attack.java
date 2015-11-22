package engine.actions;

import engine.entities.Entity;
import engine.entities.units.Unit;

/**
 * Created by large64 on 2015.11.22..
 */
public class Attack implements Runnable {
    private Unit unit;
    private Entity entity;

    public Attack(Unit unit, Entity entity) {
        this.unit = unit;
        this.entity = entity;
    }

    @Override
    public void run() {
        System.out.println("thread started");
        unit.attack(entity);
        System.out.println("thread stopped");
    }
}