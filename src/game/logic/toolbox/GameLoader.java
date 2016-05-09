package game.logic.toolbox;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.entities.buildings.Barrack;
import game.graphics.entities.buildings.Home;
import game.graphics.entities.buildings.Hospital;
import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.MiniMap;
import game.graphics.windowparts.Scene;
import game.graphics.windowparts.Window;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.buildings.RawBarrack;
import game.logic.entities.buildings.RawHome;
import game.logic.entities.buildings.RawHospital;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawSoldier;
import game.logic.toolbox.map.Node;
import game.logic.toolbox.map.Tile;
import org.kopitubruk.util.json.JSONConfig;
import org.kopitubruk.util.json.JSONParser;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 21..
 */
public class GameLoader {
    private static List<Entity> entities = new ArrayList<>();
    private static List<RawEntity> rawEntities = new ArrayList<>();

    public static void load(String filename) {
        File jsonFile = new File("res/saved_games/" + filename + ".json");
        String jsonFileContent = GameLoader.getFileContent(jsonFile);
        LinkedHashMap jsonData;

        ArrayList parsedEntities;
        LinkedHashMap parsedPlayer;


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
                if (stringPath != null) {
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

                createEntity(ID, path, tilePosition, rotation, health, side, isSelected);

            }

            parsedPlayer = (LinkedHashMap) jsonData.get("player");
            Camera camera = new Camera();

            LinkedHashMap positionHashMap = (LinkedHashMap) parsedPlayer.get("position");

            String xString = (String) positionHashMap.get("x");
            float x = Float.valueOf(xString);

            String zString = (String) positionHashMap.get("z");
            float z = Float.valueOf(zString);

            String zoomString = (String) parsedPlayer.get("zoom");
            float zoom = Float.valueOf(zoomString);

            Vector3f position = new Vector3f(x, zoom, z);

            camera.setPosition(position);

            // Refresh player and picker of the scene
            Scene.getPlayer().setCamera(camera);
            Scene.getPicker().setCamera(camera);
        }
        MiniMap.getEntities().clear();
        RawMap.getRawEntities().clear();

        Scene.setEntities(entities);
        MiniMap.setEntities();
        RawMap.setRawEntities();
        RawMap.lookForChanges();
    }

    private static String getFileContent(File file) {
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

    private static void createEntity(String ID, List<Node> path, Tile tilePosition,float rotation, int health,
                                     Side side, boolean isSelected) {
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
            case "home":
                RawHome rawHome = new RawHome();
                rawHome.setId(ID);
                rawHome.setTilePosition(tilePosition);
                rawHome.setPosition(tilePosition.toPosition());
                rawHome.setRotation(rotation);
                rawHome.setHealth(health);
                rawHome.setSide(side);
                rawHome.setSelected(isSelected);
                if (isSelected) {
                    Scene.getSelectedEntities().add(rawHome);
                }
                rawEntities.add(rawHome);

                Home home = new Home();
                home.setRawEntity(rawHome);
                home.refreshPosition();
                entities.add(home);
                break;
            case "hospital":
                RawHospital rawHospital = new RawHospital();
                rawHospital.setId(ID);
                rawHospital.setTilePosition(tilePosition);
                rawHospital.setPosition(tilePosition.toPosition());
                rawHospital.setRotation(rotation);
                rawHospital.setHealth(health);
                rawHospital.setSide(side);
                rawHospital.setSelected(isSelected);
                if (isSelected) {
                    Scene.getSelectedEntities().add(rawHospital);
                }
                rawEntities.add(rawHospital);

                Hospital hospital = new Hospital();
                hospital.setRawEntity(rawHospital);
                hospital.refreshPosition();
                entities.add(hospital);
                break;
            case "barrack":
                RawBarrack rawBarrack = new RawBarrack();
                rawBarrack.setId(ID);
                rawBarrack.setTilePosition(tilePosition);
                rawBarrack.setPosition(tilePosition.toPosition());
                rawBarrack.setRotation(rotation);
                rawBarrack.setHealth(health);
                rawBarrack.setSide(side);
                rawBarrack.setSelected(isSelected);
                if (isSelected) {
                    Scene.getSelectedEntities().add(rawBarrack);
                }
                rawEntities.add(rawBarrack);

                Barrack barrack = new Barrack();
                barrack.setRawEntity(rawBarrack);
                barrack.refreshPosition();
                entities.add(barrack);
                break;
        }
    }

    public static JPanel getLoaderPanel() {
        JPanel loaderPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(5, 1, 0, 10);
        loaderPanel.setLayout(gridLayout);

        JLabel title = new JLabel("Load Game");
        title.setHorizontalAlignment(JLabel.CENTER);
        loaderPanel.add(title);

        List<String> files = getSavedFiles();

        JLabel loadLabel = new JLabel("Choose a game to load");
        loadLabel.setHorizontalAlignment(JLabel.CENTER);
        loaderPanel.add(loadLabel);

        JComboBox fileListComboBox = new JComboBox<>(files.toArray());
        loaderPanel.add(fileListComboBox);

        JButton loadButton = new JButton("Load selected");
        loadButton.setHorizontalAlignment(JButton.CENTER);
        loadButton.addActionListener(e -> {
            String toLoad = ((String) fileListComboBox.getSelectedItem()).substring(0, 5);
            Scene.getEntities().clear();
            Scene.getSelectedEntities().clear();
            GameLoader.load(toLoad);
            Window.switchMenuFrameContent(null);
            Scene.setGameMode(GameMode.PAUSED);
        });
        loaderPanel.add(loadButton);

        JButton button = new JButton("Cancel");
        button.setHorizontalAlignment(JButton.CENTER);
        button.addActionListener(e -> {
            Window.loadDefaultMenu();
        });
        loaderPanel.add(button);

        return loaderPanel;
    }

    private static List<String> getSavedFiles() {
        List<String> filesList = new ArrayList<>();

        File folder = new File("res/saved_games");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : Arrays.asList(listOfFiles)) {
                Long created = file.lastModified();

                Timestamp timestamp = new Timestamp(created);
                LocalDateTime dateTime = timestamp.toLocalDateTime();
                String dateString = dateTime.toString().replace('T', ' ').replaceAll("\\.\\d*", "");

                filesList.add(file.getName().replaceAll("\\.json", "") + " - " + dateString);
            }
        }

        return filesList;
    }
}
