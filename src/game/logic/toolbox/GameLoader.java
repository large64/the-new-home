package game.logic.toolbox;

import game.graphics.entities.Entity;
import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.windowparts.MiniMap;
import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawSoldier;
import game.logic.toolbox.map.Node;
import game.logic.toolbox.map.Tile;
import org.kopitubruk.util.json.JSONConfig;
import org.kopitubruk.util.json.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Dénes on 2015. 11. 21..
 */
public class GameLoader {

    public static List<Entity> load(String filename) {
        File jsonFile = new File("res/saved_games/" + filename + ".json");
        String jsonFileContent = GameLoader.getFileContent(jsonFile);
        LinkedHashMap jsonData = null;

        ArrayList parsedEntities = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        List<RawEntity> rawEntities = new ArrayList<>();


        JSONConfig config = new JSONConfig();
        config.setEncodeNumericStringsAsNumbers(true);
        jsonData = (LinkedHashMap) JSONParser.parseJSON(jsonFileContent, config);


        if (jsonData != null) {
            parsedEntities = (ArrayList) jsonData.get("entities");

            for (Object entity : parsedEntities) {
                Map entityDataMap = (Map) entity;

                String ID = (String) entityDataMap.get("id");
                List stringPath = (List) entityDataMap.get("path");
                List<Node> path = new ArrayList<>();

                Node previousNode = null;
                for (Object nodeString : stringPath) {
                    if (nodeString instanceof String) {
                        int[] values = GameLoader.splitAndTrim((String) nodeString);
                        Node node = new Node(values[0], values[1]);
                        if (previousNode != null) {
                            node.parent = previousNode;
                        }
                        path.add(node);
                        previousNode = node;
                    }
                }

                String tilePositionString = (String) entityDataMap.get("tilePosition");
                int[] values = GameLoader.splitAndTrim(tilePositionString);
                Tile tilePosition = new Tile(values[0], values[1]);

                String rotationString = ((String) entityDataMap.get("rotation"));
                float rotation = Float.valueOf(rotationString);

                String healthString = (String) entityDataMap.get("health");
                int health = Integer.valueOf(healthString);

                Side side = Side.valueOf((String) entityDataMap.get("side"));

                boolean isSelected = (boolean) entityDataMap.get("isSelected");

                // Get string from ID
                String type = ID.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];

                switch (type) {
                    case "soldier":
                        RawSoldier rawSoldier = new RawSoldier();
                        rawSoldier.setId(ID);
                        rawSoldier.setPath(path);
                        if (!path.isEmpty()) {
                            rawSoldier.setCurrentNode(path.get(0));
                        }
                        rawSoldier.setTilePosition(tilePosition);
                        rawSoldier.setPosition(tilePosition.toPosition());
                        rawSoldier.setRotation(rotation);
                        rawSoldier.setHealth(health);
                        rawSoldier.setSide(side);
                        rawSoldier.setSelected(isSelected);
                        if (isSelected) {
                            Scene.getSelectedEntities().add(rawSoldier);
                        }
                        rawEntities.add(rawSoldier);

                        Soldier soldier = new Soldier();
                        soldier.setRawEntity(rawSoldier);
                        entities.add(soldier);
                        break;
                    case "healer":
                        RawHealer rawHealer = new RawHealer();
                        rawHealer.setId(ID);
                        rawHealer.setPath(path);
                        if (!path.isEmpty()) {
                            rawHealer.setCurrentNode(path.get(0));
                        }
                        rawHealer.setTilePosition(tilePosition);
                        rawHealer.setPosition(tilePosition.toPosition());
                        rawHealer.setRotation(rotation);
                        rawHealer.setHealth(health);
                        rawHealer.setSide(side);
                        rawHealer.setSelected(isSelected);
                        if (isSelected) {
                            Scene.getSelectedEntities().add(rawHealer);
                        }
                        rawEntities.add(rawHealer);

                        Healer healer = new Healer();
                        healer.setRawEntity(rawHealer);
                        entities.add(healer);
                        break;
                }
            }
        }
        MiniMap.setEntities(rawEntities);
        RawMap.setRawEntities(rawEntities);

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

    private static int[] splitAndTrim(String string) {
        int[] values = new int[2];
        if (!string.equals("")) {
            String[] stringPieces = string.split(",");
            values[0] = Integer.valueOf(stringPieces[0].trim());
            values[1] = Integer.valueOf(stringPieces[1].trim());
        }
        return values;
    }
}
