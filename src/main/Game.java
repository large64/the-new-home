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
        Random random = new Random(6547);
        int rowsOfMap = Map.getRowNumber();
        int to = 5;

        Unit unit0 = new Unit(rowsOfMap - 10, rowsOfMap - 10);
        entities.add(unit0);

        Unit unit1 = new Unit(11, 10);
        entities.add(unit1);

        Unit unit2 = new Unit(11, 11);
        entities.add(unit2);

        Unit unit3 = new Unit(11, 9);
        entities.add(unit3);

        /*for (int i = 1; i < rowsOfMap + 5; ++i) {
            Unit unit = new Unit(random.nextInt(Map.getRowNumber()), random.nextInt(Map.getRowNumber()));
            entities.add(unit);
        }*/

        Unit selectedUnit = new Unit(13, 10);
        entities.add(selectedUnit);

        Game.map = new Map(entities);

        for (Object entity : entities) {
            System.out.println(entity.toString());
        }

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
