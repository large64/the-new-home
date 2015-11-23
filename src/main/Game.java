package main;

import engine.Map;
import engine.actions.Attack;
import engine.actions.Healing;
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

        Attack attackRunnable = new Attack((Unit) entities.get(0), (Building) entities.get(3));
        Attack attackRunnable1 = new Attack((Unit) entities.get(7), (Building) entities.get(5));
        Healing healing = new Healing((Unit) entities.get(8), (Building) entities.get(6));

        Thread thread = new Thread(attackRunnable);
        Thread thread1 = new Thread(attackRunnable1);
        Thread thread2 = new Thread(healing);

        thread.start();
        thread1.start();
        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
    }

    public static void makeTimePass() {
        try {
            Thread.sleep(500);
            Map.lookForChanges();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
