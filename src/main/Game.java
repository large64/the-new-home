package main;

import engine.MiniMap;
import engine.actions.Attack;
import engine.actions.Healing;
import engine.entities.buildings.RawBuilding;
import engine.entities.units.RawSoldier;
import engine.entities.units.Unit;

import java.util.List;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Game {
    public static void main(String[] args) {
        List entities = GameLoader.load("attack");

        for (Object entity : entities) {
            System.out.println(entity.toString());
        }

        Attack attackRunnable = new Attack((RawSoldier) entities.get(0), (RawBuilding) entities.get(3));
        Attack attackRunnable1 = new Attack((Unit) entities.get(7), (RawBuilding) entities.get(5));
        Healing healing = new Healing((Unit) entities.get(8), (RawBuilding) entities.get(6));

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
}
