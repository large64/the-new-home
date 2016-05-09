package game.logic.toolbox;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.Scene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Dénes on 2016. 05. 09..
 */
public class GameSaver {
    public static void save() {
        if (Scene.getGameMode().equals(GameMode.PAUSED)) {
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
    }

    private static void saveToFile(String content) {
        File folder = new File("res/saved_games");
        File[] listOfFiles = folder.listFiles();

        // Get the last file
        if (listOfFiles != null) {
            File file = listOfFiles[listOfFiles.length - 1];
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
