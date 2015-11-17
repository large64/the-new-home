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


        /*for (int i = 1; i < rowsOfMap; ++i) {
            Unit unit = new Unit(random.nextInt(Map.getRowNumber()), random.nextInt(Map.getRowNumber()));
            entities.add(unit);
        }*/

        Unit selectedUnit = new Unit(0, 0);
        entities.add(selectedUnit);

        Unit destination = new Unit(4, 4);
        entities.add(destination);

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
            selectedUnit.stepTowards(destination);
            Map.lookForChanges();
        }
    }
}
