package game.graphics.windowparts;

import game.graphics.entities.*;
import game.graphics.entities.buildings.Building;
import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.renderers.MasterRenderer;
import game.graphics.textures.ModelTexture;
import game.graphics.textures.TerrainTexture;
import game.graphics.textures.TerrainTexturePack;
import game.graphics.toolbox.GameMode;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.MousePicker;
import game.graphics.toolbox.OBJLoader;
import game.graphics.windowparts.BuildingPanel.BuildingPanel;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dénes on 2016. 04. 13..
 */
public class Scene {
    private static Map mainMap;
    private static boolean restart = false;
    private static Player player;
    private static GameMode gameMode;

    private static List<Light> lights;
    private static ArrayList<Map> maps;
    private static List<RawEntity> rawEntities = new ArrayList<>();
    private static List<RawEntity> selectedEntities = new ArrayList<>();
    private static Entity levitatingEntity = null;
    private static List<Entity> entities = new ArrayList<>();
    private static java.util.Map<String, TexturedModel> modelsMap;

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
        loadModels();

        // Generate random coordinates for entities
        for (int i = 0; i < 20; i++) {
            float x = (float) (Math.random() * 200);
            float z = (float) (Math.random() * 200);

            if (i % 2 == 0) {
                Soldier soldier = new Soldier(modelsMap.get("soldierUnit"), new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(soldier);
            }/*
            else if (i % 3 == 0){
                Healer healer = new Healer(modelsMap.get("healerUnit"), new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(healer);
            }
            if (i % 4 == 0) {
                x = (float) (Math.random() * 200);
                z = (float) (Math.random() * 200);

                Neutral neutral = new Neutral(modelsMap.get("treeNeutral"), new Vector3f(x, 0, z), 0, 0, 0, 1);
                entities.add(neutral);
            }*/
        }

        Soldier soldier = new Soldier(modelsMap.get("soldierUnit"), new Vector3f(50, 0, 20), 0, 0, 0, 1, Side.ENEMY);
        entities.add(soldier);

        MiniMap.setEntities(rawEntities);
        MiniMap.lookForChanges();
        new RawMap(rawEntities);
        setGameMode(GameMode.STOPPED);

        rightClick = false;
        leftClick = false;
        middleClick = false;

        firstMiddleClickPosition = null;
    }

    public static void render() {
        switch (gameMode) {
            case STOPPED:
                Component menu = Window.getMenuFrame();
                if (!menu.isVisible()) {
                    menu.setVisible(true);
                    menu.setFocusable(true);
                    menu.requestFocus();
                }
                break;
            case PAUSED:
                processEntities(MasterRenderer.getSelectedTile(), MasterRenderer.getMasterRenderer());
                break;
            case BUILDING:
                BuildingPanel.setBuilderPanelVisible();
                if (!mainMap.isTilesShown()) mainMap.setTilesShown(true);

                if (Camera.isMouseGrabbed()) {
                    if (Mouse.isButtonDown(2) && !middleClick) {
                        firstMiddleClickPosition = new Vector2f(Mouse.getX(), Mouse.getY());
                    }

                    if (Mouse.isButtonDown(0) && !leftClick) {
                        MasterRenderer.setSelectedTile(Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x,
                                picker.getCurrentTerrainPoint().z)));
                        checkSelection(MasterRenderer.getSelectedTile());
                    }

                    middleClick = Mouse.isButtonDown(2);
                    leftClick = Mouse.isButtonDown(0);
                    player.move(firstMiddleClickPosition);
                    PositionInfo.lookForChanges(picker);
                }
                picker.update();
                processEntities(MasterRenderer.getSelectedTile(), MasterRenderer.getMasterRenderer());
                break;
            case ONGOING:
                BuildingPanel.setBuilderPanelInvisible();
                if (mainMap.isTilesShown()) mainMap.setTilesShown(false);

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
                    //guiRenderer.render(guis);
                    processEntities(MasterRenderer.getSelectedTile(), MasterRenderer.getMasterRenderer());
                }
                break;
        }
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

    private static void processEntities(Tile selectedTile, MasterRenderer renderer) {
        for (Entity entity : entities) {
            RawEntity rawEntity = entity.getRawEntity();
            if (gameMode.equals(GameMode.ONGOING)) {
                if (rawEntity.isAlive()) {
                    if (rawEntity instanceof RawUnit) {
                        RawUnit rawUnit = (RawUnit) rawEntity;
                        if ((rawUnit).isMoving() && selectedTile != null) {
                            rawUnit.performAction(selectedTile);
                        }
                    }
                }
            }
            else if (gameMode.equals(GameMode.BUILDING) && levitatingEntity != null) {
                // @TODO: clean code
                levitatingEntity.setPosition(picker.getCurrentTerrainPoint());
                levitatingEntity.getRawEntity().setTilePosition(selectedTile);
            }

            renderer.processEntity(entity);
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
        if (gameMode.equals(GameMode.ONGOING) && RawMap.isTileFree(selectedTile, false)) {
            for (RawEntity entity : selectedEntities) {
                if (entity instanceof RawUnit) {
                    RawUnit rawUnit = (RawUnit) entity;
                    rawUnit.calculatePath(selectedTile);
                }
            }
        }
        else if (gameMode.equals(GameMode.BUILDING)) {
            if (levitatingEntity != null) {
                levitatingEntity.getRawEntity().setTilePosition(selectedTile);
                Position newPosition = selectedTile.toPosition();
                newPosition.setY(mainMap.getHeightOfMap(newPosition.getX(), newPosition.getZ()));
                levitatingEntity.setPosition(newPosition);
                levitatingEntity = null;
                RawMap.lookForChanges();
                MiniMap.lookForChanges();
            }
            else {
                for (Entity entity : entities) {
                    RawEntity rawEntity = entity.getRawEntity();
                    if (!rawEntity.getSide().equals(Side.ENEMY) && entity instanceof Building) {
                        List<Tile> extentPositions = ((RawBuilding) rawEntity).getExtentPositions();
                        boolean isEntityInSelection = rawEntity.getTilePosition().equals(selectedTile)
                                || extentPositions.contains(selectedTile);
                        if (isEntityInSelection) {
                            levitatingEntity = entity;
                            break;
                        }
                    }
                }
            }
        }
    }

    public static Map getMainMap() {
        return mainMap;
    }

    public static List getEntities() {
        return entities;
    }

    public static void setGameMode(GameMode gameMode) {
        if (!gameMode.equals(GameMode.STOPPED)) {
            Window.getGameModeList().setEnabled(true);
        }
        else {
            Window.getGameModeList().setEnabled(false);
        }
        Scene.gameMode = gameMode;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    public static void setLevitatingEntity(Entity levitatingEntity) {
        Scene.levitatingEntity = levitatingEntity;
    }

    public static void addEntity(Entity entity) {
        entities.add(entity);
    }

    public static java.util.Map<String, TexturedModel> getModelsMap() {
        return modelsMap;
    }

    private static void loadModels() {
        Loader loader = MasterRenderer.getLoader();
        modelsMap = new HashMap<>();

        TexturedModel soldierModel = new TexturedModel(OBJLoader.loadObjModel("soldier", loader),
                new ModelTexture(loader.loadTexture("soldier")));
        modelsMap.put("soldierUnit", soldierModel);

        TexturedModel healerModel = new TexturedModel(OBJLoader.loadObjModel("healer", loader),
                new ModelTexture(loader.loadTexture("healer")));
        modelsMap.put("healerUnit", healerModel);

        TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
                new ModelTexture(loader.loadTexture("palm_tree")));
        modelsMap.put("treeNeutral", treeModel);

        TexturedModel homeModel = new TexturedModel(OBJLoader.loadObjModel("home", loader),
                new ModelTexture(loader.loadTexture("home_texture")));
        modelsMap.put("homeBuilding", homeModel);

        TexturedModel hospitalModel = new TexturedModel(OBJLoader.loadObjModel("hospital", loader),
                new ModelTexture(loader.loadTexture("hospital_texture")));
        modelsMap.put("hospitalBuilding", hospitalModel);

        TexturedModel barrackModel = new TexturedModel(OBJLoader.loadObjModel("barrack", loader),
                new ModelTexture(loader.loadTexture("barrack_texture")));
        modelsMap.put("barrackBuilding", barrackModel);
    }
}
