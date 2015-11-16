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

        Unit unit0 = new Unit(0, 0);
        entities.add(unit0);

        Unit unit1 = new Unit(1, 0);
        entities.add(unit1);

        Unit unit2 = new Unit(1, 1);
        entities.add(unit2);

        Unit unit3 = new Unit(1, 2);
        entities.add(unit3);

        Unit selectedUnit = new Unit(3, 2);
        entities.add(selectedUnit);

        Game.map = new Map(entities);

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*if (((Unit) entities.get(1)).isAlive()) {
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
            }*/
            selectedUnit.stepTowards((Unit) entities.get(0));
            Map.lookForChanges();
        }
    }
}
