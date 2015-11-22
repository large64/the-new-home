package main;

import engine.Map;
import engine.entities.Entity;
import engine.entities.buildings.Building;
import engine.entities.units.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Game {
    private static Map map;
    private static List entities = new ArrayList<Entity>();

    public static void main(String[] args) {
        entities = GameLoader.load("attack");

        Game.map = new Map(entities);

        for (Object entity : entities) {
            System.out.println(entity.toString());
        }

        ((Unit)(entities.get(0))).attack((Building) entities.get(5));
        ((Unit)(entities.get(0))).attack((Building) entities.get(6));
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
