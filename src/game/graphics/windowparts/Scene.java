package game.graphics.windowparts;

import game.graphics.entities.*;
import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.renderers.MasterRenderer;
import game.graphics.textures.ModelTexture;
import game.graphics.textures.TerrainTexture;
import game.graphics.textures.TerrainTexturePack;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.MousePicker;
import game.graphics.toolbox.OBJLoader;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2016. 04. 13..
 */
public class Scene {
    private static Map mainMap;
    private static boolean restart = false;
    private static Player player;

    private static List<Light> lights;
    private static ArrayList<Map> maps;
    private static List<RawEntity> rawEntities = new ArrayList<>();
    private static List<RawEntity> selectedEntities = new ArrayList<>();
    private static List<Entity> entities = new ArrayList<>();

    private static MousePicker picker;

    private static boolean rightClick;
    private static boolean leftClick;
    private static boolean middleClick;

    private static Vector2f firstMiddleClickPosition;

    public Scene() {
        Loader loader = MasterRenderer.getLoader();
        // Set map features
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkflowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));

        maps = new ArrayList<>();
        mainMap = new Map(0, 1, loader, texturePack, blendMap, "heightmap2");
        maps.add(mainMap);

        maps.add(new Map(1, 1, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(-1, 1, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(0, -199, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(0, 200, loader, texturePack, blendMap, "heightmap"));

        maps.add(new Map(1, 199, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(-1, 199, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(1, -199, loader, texturePack, blendMap, "heightmap"));
        maps.add(new Map(-1, -199, loader, texturePack, blendMap, "heightmap"));

        // Set features of lights
        lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(-2000, 2000, 2000), new Vector3f(1f, 1f, 1f)));

        // Set features of GUIs
        /*List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("julia_set"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);*/

        // Set features of player
        player = new Player();
        picker = new MousePicker(Scene.getPlayer().getCamera(), MasterRenderer.getMasterRenderer().getProjectionMatrix(), Scene.getMainMap());

        // Set features of entities
        TexturedModel soldierModel = new TexturedModel(OBJLoader.loadObjModel("soldier", loader),
                new ModelTexture(loader.loadTexture("soldier")));
        TexturedModel healerModel = new TexturedModel(OBJLoader.loadObjModel("healer", loader),
                new ModelTexture(loader.loadTexture("healer")));
        TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
                new ModelTexture(loader.loadTexture("palm_tree")));

        // Generate random coordinates for entities
        for (int i = 0; i < 20; i++) {
            float x = (float) (Math.random() * 200);
            float z = (float) (Math.random() * 200);

            if (i % 2 == 0) {
                Soldier soldier = new Soldier(soldierModel, new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(soldier);
            }
            else if (i % 3 == 0){
                Healer healer = new Healer(healerModel, new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(healer);
            }
            if (i % 4 == 0) {
                x = (float) (Math.random() * 200);
                z = (float) (Math.random() * 200);

                Neutral neutral = new Neutral(treeModel, new Vector3f(x, 0, z), 0, 0, 0, 1);
                entities.add(neutral);
            }
        }

        Soldier soldier = new Soldier(soldierModel, new Vector3f(10, 0, 10), 0, 0, 0, 1, Side.ENEMY);
        entities.add(soldier);

        MiniMap.setEntities(rawEntities);
        MiniMap.lookForChanges();
        new RawMap(rawEntities);

        rightClick = false;
        leftClick = false;
        middleClick = false;

        firstMiddleClickPosition = null;
    }

    public static void render() {
        if (restart) {
            player.reset();
            rawEntities.forEach(RawEntity::reset);
            restart = false;
        }
        else {
            player.move(firstMiddleClickPosition);
        }

        if (Camera.isMouseGrabbed()) {
            if (Mouse.isButtonDown(1) && !rightClick && !selectedEntities.isEmpty()) {
                MiniMap.clearMarkers();
                MasterRenderer.setSelectedTile(Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x,
                        picker.getCurrentTerrainPoint().z)));
                checkSelection(MasterRenderer.getSelectedTile());
            }

            if (Mouse.isButtonDown(0) && !leftClick) {
                processSelectedEntities(picker, null);
            }

            if (Mouse.isButtonDown(2) && !middleClick) {
                firstMiddleClickPosition = new Vector2f(Mouse.getX(), Mouse.getY());
            }

            rightClick = Mouse.isButtonDown(1);
            leftClick = Mouse.isButtonDown(0);
            middleClick = Mouse.isButtonDown(2);

            // Move the player per frame (and so the camera)
            picker.update();
            PositionInfo.lookForChanges(picker);

            EntityInfo.refreshInfo();
            MiniMap.lookForChanges();
            RawMap.lookForChanges();
            //guiRenderer.render(guis);
        }


        processMovements(MasterRenderer.getSelectedTile(), MasterRenderer.getMasterRenderer());

    }

    public static Player getPlayer() {
        return player;
    }

    public static List<Light> getLights() {
        return lights;
    }

    public static ArrayList<Map> getMaps() {
        return maps;
    }

    private static void processSelectedEntities(MousePicker picker, Tile tilePosition) {
        Tile tile = tilePosition;
        if (picker != null) {
            tile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
        }

        boolean atLeastOne = false;
        for (Entity entity : entities) {
            RawEntity rawEntity = entity.getRawEntity();

            if (rawEntity.getTilePosition().equals(tile) && rawEntity.getSide().equals(Side.FRIEND)
                    && !selectedEntities.contains(rawEntity)) {
                if (selectedEntities.size() < EntityInfo.MULTI_SIZE) {
                    selectedEntities.add(rawEntity);
                    entity.setSelected(true);
                }
                atLeastOne = true;
            }
        }
        if (!atLeastOne) {
            unSelectAllEntities();
        }
    }

    static void unSelectAllEntities() {
        selectedEntities.clear();
        for (Entity entity : entities) {
            entity.setSelected(false);
        }
    }

    private static void processMovements(Tile selectedTile, MasterRenderer renderer) {
        for (Entity entity : entities) {
            RawEntity rawEntity = entity.getRawEntity();
            if (rawEntity.isAlive()) {
                if (rawEntity instanceof RawUnit) {
                    RawUnit rawUnit = (RawUnit) rawEntity;
                    if ((rawUnit).isMoving() && selectedTile != null) {
                        rawUnit.performAction(selectedTile);
                    }
                }

                renderer.processEntity(entity);
            }
        }
    }

    static void restart() {
        restart = true;
    }

    public static void addRawEntity(RawEntity entity) {
        rawEntities.add(entity);
    }

    public static List<RawEntity> getSelectedEntities() {
        return selectedEntities;
    }

    private static void checkSelection(Tile selectedTile) {
        for (RawEntity entity : selectedEntities) {
            if (entity instanceof RawUnit) {
                RawUnit rawUnit = (RawUnit) entity;
                rawUnit.calculatePath(selectedTile);
            }
        }
    }

    public static Map getMainMap() {
        return mainMap;
    }

    public static List getEntities() {
        return entities;
    }
}
