package game.graphics.windowparts;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.entities.Light;
import game.graphics.entities.Player;
import game.graphics.entities.buildings.Building;
import game.graphics.entities.units.Unit;
import game.graphics.models.TexturedModel;
import game.graphics.renderers.MasterRenderer;
import game.graphics.textures.ModelTexture;
import game.graphics.textures.TerrainTexture;
import game.graphics.textures.TerrainTexturePack;
import game.graphics.toolbox.GameMode;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.MousePicker;
import game.graphics.toolbox.OBJLoader;
import game.graphics.windowparts.buildingpanel.BuildingPanel;
import game.graphics.windowparts.infopanels.EntityInfo;
import game.graphics.windowparts.infopanels.PositionInfo;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
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
        /*for (int i = 0; i < 20; i++) {
            float x = (float) (Math.random() * 200);
            float z = (float) (Math.random() * 200);

            if (i % 2 == 0) {
                Soldier soldier = new Soldier(modelsMap.get("soldierUnit"), new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(soldier);
            }
            else if (i % 3 == 0){
                Healer healer = new Healer(modelsMap.get("healerUnit"), new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(healer);
            }
            if (i % 4 == 0) {
                x = (float) (Math.random() * 200);
                z = (float) (Math.random() * 200);

                Neutral neutral = new Neutral(modelsMap.get("treeNeutral"), new Vector3f(x, 0, z), 0, 0, 0, 1);
                entities.add(neutral);
            }
            if (i % 5 == 0) {
                x = (float) (Math.random() * 200);
                z = (float) (Math.random() * 200);

                Scientist scientist = new Scientist(modelsMap.get("scientistUnit"), new Vector3f(x, 0, z), 0, 0, 0, 1, Side.FRIEND);
                entities.add(scientist);
            }
        }

        Soldier soldier = new Soldier(modelsMap.get("soldierUnit"), new Vector3f(50, 0, 20), 0, 0, 0, 1, Side.ENEMY);
        entities.add(soldier);*/
        new RawMap();
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
                        checkClick(MasterRenderer.getSelectedTile());
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
                    for (Object rawEntity : getRawEntities()) {
                        ((RawEntity) rawEntity).reset();
                    }
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
                        checkClick(MasterRenderer.getSelectedTile());
                    }

                    if (Mouse.isButtonDown(0) && !leftClick) {
                        if (levitatingEntity != null && levitatingEntity instanceof Unit) {
                            placeLevitatingEntity(Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x,
                                    picker.getCurrentTerrainPoint().z)));
                        }
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

        RawEntity rawEntity = RawMap.whatIsOnTile(tile);
        boolean entityAdded = false;

        if (rawEntity != null && !rawEntity.isSelected() && selectedEntities.size() < EntityInfo.MULTI_SIZE
                && rawEntity.getSide().equals(Side.FRIEND) && !selectedEntities.contains(rawEntity)) {
            if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                selectedEntities.clear();
                unSelectAllEntities();
            }
            selectedEntities.add(rawEntity);
            rawEntity.setSelected(true);
            entityAdded = true;
        }
        if (!entityAdded) {
            unSelectAllEntities();
        }
        UnitCreator.lookForChanges();
    }

    private static void unSelectAllEntities() {
        selectedEntities.clear();
        for (Object rawEntity : getRawEntities()) {
            ((RawEntity) rawEntity).setSelected(false);
        }
    }

    private static void processEntities(Tile selectedTile, MasterRenderer renderer) {
            switch (gameMode) {
                case ONGOING:
                    for (Entity entity : entities) {
                        RawEntity rawEntity = entity.getRawEntity();

                        if (rawEntity.isAlive()) {
                            if (rawEntity instanceof RawUnit) {
                                RawUnit rawUnit = (RawUnit) rawEntity;
                                if ((rawUnit).isMoving()) {
                                    rawUnit.performAction();
                                }
                            }
                        }
                        renderer.processEntity(entity);
                    }
                    if (levitatingEntity != null) {
                        handleLevitatingEntity();
                    }
                    break;
                case BUILDING:
                    entities.forEach(renderer::processEntity);

                    if (levitatingEntity != null) {
                        handleLevitatingEntity();
                    }
                    break;
                case PAUSED:
                    entities.forEach(renderer::processEntity);
                    break;
            }
    }

    static void restart() {
        restart = true;
    }

    public static List<RawEntity> getSelectedEntities() {
        return selectedEntities;
    }

    private static void checkClick(Tile selectedTile) {
        if (gameMode.equals(GameMode.ONGOING) && RawMap.isTileFree(selectedTile, false)) {
            for (RawEntity entity : selectedEntities) {
                if (entity instanceof RawUnit) {
                    RawUnit rawUnit = (RawUnit) entity;
                    rawUnit.calculatePath(selectedTile);
                }
            }
        }
        else if (gameMode.equals(GameMode.BUILDING)) {
            if (levitatingEntity != null && levitatingEntity instanceof Building) {
                RawEntity levitatingRawEntity = levitatingEntity.getRawEntity();
                int[] extentOfLevitatingEntity = ((RawBuilding) levitatingRawEntity).getExtent();

                if (RawMap.areTilesFree(selectedTile, extentOfLevitatingEntity)) {
                    placeLevitatingEntity(selectedTile);
                }
            }
            else {
                // Pick up building
                for (Entity entity : entities) {
                    RawEntity rawEntity = entity.getRawEntity();
                    if (!rawEntity.getSide().equals(Side.ENEMY) && entity instanceof Building) {
                        List<Tile> extentPositions = ((RawBuilding) rawEntity).getExtentPositions();
                        boolean isEntityInSelection = rawEntity.getTilePosition().equals(selectedTile)
                                || extentPositions.contains(selectedTile);

                        if (isEntityInSelection) {
                            levitatingEntity = entity;
                            RawBuilding levitatingRawBuilding = (RawBuilding) entity.getRawEntity();

                            RawMap.freeTiles(levitatingRawBuilding.getExtentPositions());
                            break;
                        }
                    }
                }
            }
            MiniMap.lookForChanges();
        }
    }

    public static Map getMainMap() {
        return mainMap;
    }

    public static List<Entity> getEntities() {
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

        TexturedModel soldierModel = new TexturedModel(OBJLoader.loadObjModel("unit", loader),
                new ModelTexture(loader.loadTexture("soldier_texture")));
        modelsMap.put("soldierUnit", soldierModel);

        TexturedModel healerModel = new TexturedModel(OBJLoader.loadObjModel("unit", loader),
                new ModelTexture(loader.loadTexture("healer_texture")));
        modelsMap.put("healerUnit", healerModel);

        TexturedModel scientistModel = new TexturedModel(OBJLoader.loadObjModel("unit", loader),
                new ModelTexture(loader.loadTexture("scientist_texture")));
        modelsMap.put("scientistUnit", scientistModel);

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

    public static void setEntities(List<Entity> entities) {
        Scene.entities = entities;
    }

    public static List<RawEntity> getRawEntities() {
        List<RawEntity> rawEntities = new ArrayList<>();
        for (Entity entity : entities) {
            rawEntities.add(entity.getRawEntity());
        }
        return rawEntities;
    }

    public static MousePicker getPicker() {
        return picker;
    }

    private static void handleLevitatingEntity() {
        if (Keyboard.isKeyDown(Keyboard.KEY_R) && entities.contains(levitatingEntity)) {
            entities.remove(levitatingEntity);
            levitatingEntity = null;
            MiniMap.setEntities();
            RawMap.setRawEntities();
        } else {
            levitatingEntity.setPosition(picker.getCurrentTerrainPoint());
            RawEntity levitatingRawEntity = levitatingEntity.getRawEntity();
            Vector3f currentTerrainPoint = picker.getCurrentTerrainPoint();
            Position currentMousePosition = new Position(currentTerrainPoint.getX(), currentTerrainPoint.getZ());
            Tile currentTile = Tile.positionToTile(currentMousePosition);

            levitatingRawEntity.setTilePosition(currentTile);

            levitatingEntity.setRawEntity(levitatingRawEntity);
        }
        MiniMap.lookForChanges();
    }

    private static void placeLevitatingEntity(Tile selectedTile) {
        levitatingEntity.getRawEntity().setTilePosition(selectedTile);
        levitatingEntity.getRawEntity().setPosition(selectedTile.toPosition());
        levitatingEntity.refreshPosition();
        MiniMap.setEntities();
        RawMap.setRawEntities();
        RawMap.lookForChanges();
        levitatingEntity = null;
    }
}
