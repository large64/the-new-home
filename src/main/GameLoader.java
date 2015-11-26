package main;

import engine.MiniMap;
import engine.entities.RawEntity;
import engine.entities.buildings.RawBuilding;
import engine.entities.units.RawHealer;
import engine.entities.units.RawSoldier;
import org.kopitubruk.util.json.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Dénes on 2015. 11. 21..
 */
public class GameLoader {

    public static List load(String filename) {
        File jsonFile = new File("resources/saved_games/" + filename + ".json");
        String jsonFileContent = GameLoader.getFileContent(jsonFile);
        LinkedHashMap jsonData = null;

        List<RawEntity> entities = new ArrayList<>();
        int mapSize;

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
                int intSide = Math.toIntExact((long) entityDataMap.get("side"));
                boolean side = true;

                if (intSide == 0) {
                    side = false;
                }

                Map entityPositionMap = (Map) entityDataMap.get("position");

                int entityRow = Math.toIntExact((long) entityPositionMap.get("row"));
                int entityColumn = Math.toIntExact((long) entityPositionMap.get("column"));

                switch (type) {
                    case "soldier":
                        entities.add(new RawSoldier(entityRow, entityColumn, health, side));
                        break;
                    case "healer":
                        entities.add(new RawHealer(entityRow, entityColumn, health, side));
                        break;
                    case "building":
                        entities.add(new RawBuilding(entityRow, entityColumn, health, side));
                        break;
                }
            }
            MiniMap.setSize(mapSize / 2);
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
