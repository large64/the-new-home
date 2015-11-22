package main;

import engine.Map;
import engine.entities.Entity;
import engine.entities.buildings.Building;
import engine.entities.units.Unit;

import java.util.List;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Game {
    public static void main(String[] args) {
        List entities = GameLoader.load("attack");

        new Map(entities);

        for (Object entity : entities) {
            System.out.println(entity.toString());
        }

        class AttackRunnable implements Runnable {
            private Unit unit;
            private Entity entity;

            public AttackRunnable(Unit unit, Entity entity) {
                this.unit = unit;
                this.entity = entity;
            }

            @Override
            public void run() {
                unit.attack(entity);
            }
        }
        AttackRunnable attackRunnable = new AttackRunnable((Unit) entities.get(0), (Building) entities.get(5));
        AttackRunnable attackRunnable1 = new AttackRunnable((Unit) entities.get(7), (Building) entities.get(3));

        new Thread(attackRunnable).start();
        new Thread(attackRunnable1).start();
    }

    public synchronized static void makeTimePass() {
        try {
            Thread.sleep(500);
            Map.lookForChanges();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
