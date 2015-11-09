package main;

import engine.Map;
import engine.entities.Entity;
import engine.entities.units.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Game {
    private static Map map;
    private static List entities = new ArrayList<Entity>();

    public static void main(String[] args) {
        Random random = new Random(6546);
        int rowsOfMap = Map.getRowNumber();
        int to = 5;

        for (int i = 0; i < to; ++i) {
            int rowPos = random.nextInt(rowsOfMap);
            int columnPos = random.nextInt(rowsOfMap);
            Unit unit = new Unit(rowPos, columnPos);
            System.out.println(unit.toString());
            entities.add(unit);
        }
        Game.map = new Map(entities);

        Unit selectedUnit = (Unit) entities.get(0);

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (((Unit) entities.get(1)).isAlive()) {
                selectedUnit.attack((Unit) entities.get(1));
            }
            else if (((Unit) entities.get(2)).isAlive()) {
                selectedUnit.attack((Unit) entities.get(2));
            }
            else if (((Unit) entities.get(3)).isAlive()) {
                selectedUnit.attack((Unit) entities.get(3));
            }
            else if (((Unit) entities.get(4)).isAlive()) {
                selectedUnit.attack((Unit) entities.get(4));
            }
            Map.lookForChanges();
        }
    }
}
