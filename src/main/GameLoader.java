package main;

import engine.*;
import engine.entities.Entity;
import engine.entities.buildings.Building;
import engine.entities.units.Unit;
import org.kopitubruk.util.json.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;
import java.util.Map;

/**
 * Created by Dénes on 2015. 11. 21..
 */
public class GameLoader {

    public static List load(String filename) {
        File jsonFile = new File("resources/saved_games/" + filename + ".json");
        String jsonFileContent = GameLoader.getFileContent(jsonFile);
        LinkedHashMap jsonData = null;

        List<Entity> entities = new ArrayList<>();
        int mapSize = 0;

        try {
            jsonData = (LinkedHashMap) JSONParser.parseJSON(jsonFileContent);
        } catch (ParseException e) {
            System.err.println("Could not get JSON data from file " + filename + ".");
            e.printStackTrace();
        }

        if (jsonData != null) {
            mapSize = Math.toIntExact((long) jsonData.get("mapSize"));
            Map jsonDataMap = (Map) jsonData.get("entities");

            for (int i = 0; i < (jsonDataMap).size(); i++) {
                Map entityDataMap = (Map) jsonDataMap.get("entity" + i);
                String type = (String) entityDataMap.get("type");
                int health = Math.toIntExact((long) entityDataMap.get("health"));
                Map entityPositionMap = (Map) entityDataMap.get("position");

                int entityRow = Math.toIntExact((long) entityPositionMap.get("row"));
                int entityColumn = Math.toIntExact((long) entityPositionMap.get("column"));

                switch (type) {
                    case "unit":
                        entities.add(new Unit(entityRow, entityColumn, health));
                        break;
                    case "building":
                        entities.add(new Building(entityRow, entityColumn, health));
                        break;
                }
            }
            engine.Map.setSize(mapSize / 2);
        }

        return entities;
    }

    public static String getFileContent(File file) {
        String content = "";
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                content += nextLine;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file " + file.getName());
            e.printStackTrace();
        }
        return content;
    }
}
