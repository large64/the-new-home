package main;

import engine.Map;
import engine.entities.Entity;
import engine.toolbox.Position;

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
        Random random = new Random(6543);
        int rowsOfMap = Map.getRowNumber();
        int to = 5;

        for (int i = 0; i < to; ++i) {
            int posX = random.nextInt(rowsOfMap + 1);
            int posZ = random.nextInt(rowsOfMap + 1);
            Entity entity = new Entity(new Position(posX, posZ));
            System.out.println(entity.toString());
            entities.add(entity);
        }
        Entity myEntity = new Entity(new Position(0, 0));
        entities.add(myEntity);
        Game.map = new Map(entities);

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((Entity) entities.get(0)).getPosition().increase(0, 1);
            map.lookForChanges();
        }
    }
}
