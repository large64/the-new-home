package game.logic.toolbox;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.windowparts.Scene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dénes on 2016. 05. 09..
 */
public class GameSaver {
    public static void save() {
        List<Entity> entities = Scene.getEntities();
        Camera camera = Scene.getPlayer().getCamera();

        String JSONEntities = "";
        for (int i = 0; i < entities.size(); i++) {
            String entity = entities.get(i).getRawEntity().toJSON();

            if (entities.size() - 1 != i) {
                entity += ",";
            }

            JSONEntities += entity;
        }

        String JSONCamera = camera.toJSON();

        String toSave = combineWithWrapper(JSONCamera, JSONEntities);
        saveToFile(toSave);
    }

    private static void saveToFile(String content) {
        File folder = new File("res/saved_games");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[0].isFile()) {
                File file = listOfFiles[0];
                String fileName = file.getName();
                // Get the number out of the file name
                String numberString = fileName.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[1];
                int number = Integer.valueOf(numberString);
                number++;

                try (PrintWriter out = new PrintWriter("res/saved_games/save" + number + ".json")) {
                    out.println(content);
                } catch (FileNotFoundException e) {
                    System.err.println("Could not find file to save in.");
                }
            }
        }

        /*
        * Long created = file.lastModified();

                Timestamp timestamp = new Timestamp(created);
                LocalDateTime dateTime = timestamp.toLocalDateTime();
        * */
    }

    private static String combineWithWrapper(String player, String entities) {
        String withWrapper = "";
        withWrapper += "{";
        withWrapper += "\"player\":";
        withWrapper += player;
        withWrapper += "\"entities\":[";
        withWrapper += entities;
        withWrapper += "]";
        withWrapper += "}";

        return withWrapper;
    }
}
