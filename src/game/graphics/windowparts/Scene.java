package game.graphics.windowparts;

import game.graphics.entities.Entity;
import game.graphics.entities.Light;
import game.graphics.entities.Player;
import game.graphics.entities.buildings.Building;
import game.graphics.entities.buildings.Home;
import game.graphics.entities.units.Scientist;
import game.graphics.entities.units.Soldier;
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
import game.graphics.windowparts.infopanels.ActionInfo;
import game.graphics.windowparts.infopanels.EntityInfo;
import game.graphics.windowparts.infopanels.PositionInfo;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.AttackWave;
import game.logic.toolbox.GameObserver;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Scene {
    private final static List<RawEntity> selectedEntities = new ArrayList<>();
    private final static MasterRenderer masterRenderer = MasterRenderer.getInstance();
    private static Map mainMap;
    private static Player player;
    private static GameMode gameMode;
    private static Runnable attackRunnable;
    private static AttackWave entityCreatorRunnable;
    private static List<Light> lights;
    private static ArrayList<Map> maps;
    private static Entity levitatingEntity = null;
    private static List<Entity> entities = Collections.synchronizedList(new ArrayList<>());
    private static java.util.Map<String, TexturedModel> modelsMap;
    private static MousePicker picker;
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;
    private static Future service;

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
        Light sun = new Light(new Vector3f(105, 500, 400), new Vector3f(1f, 1f, 1f));
        lights.add(sun);

        // Set features of player
        player = new Player();
        picker = new MousePicker(Scene.getPlayer().getCamera(), masterRenderer.getProjectionMatrix(), Scene.getMainMap());

        // Set features of entities
        loadModels();

        new RawMap();
        setGameMode(GameMode.STOPPED);

        rightClick = false;
        leftClick = false;
        middleClick = false;

        firstMiddleClickPosition = null;

        new GameObserver();

        entityCreatorRunnable = null;

        attackRunnable = () -> {
            for (Entity entity : entities) {
                RawEntity rawEntity = entity.getRawEntity();

                if (rawEntity instanceof RawUnit) {
                    RawUnit rawUnit = (RawUnit) rawEntity;

                    if (rawEntity.getSide().equals(Side.ENEMY)) {
                        RawEntity randomRawEntity = getRandomEntity(Side.FRIEND);

                        if (randomRawEntity != null) {
                            int row = randomRawEntity.getTilePosition().getRow();
                            int column = randomRawEntity.getTilePosition().getColumn();

                            Tile destinationTile = new Tile(row, column);

                            if (randomRawEntity instanceof RawBuilding) {
                                List<Tile> extentPositions = ((RawBuilding) randomRawEntity).getExtentPositions();
                                destinationTile = extentPositions.get(new Random().nextInt(extentPositions.size()));
                            }

                            rawUnit.setDestinationTile(destinationTile);
                            rawUnit.calculatePath();
                        }
                    }
                }
            }
        };

        Runnable refreshMiniMapRunnable = MiniMap::lookForChanges;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(refreshMiniMapRunnable, 3, 3, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(attackRunnable, 11, 15, TimeUnit.SECONDS);
    }

    public static void setEntityCreatorRunnable(AttackWave entityCreatorRunnable) {
        Scene.entityCreatorRunnable = entityCreatorRunnable;
    }

    public static void render() {
        switch (gameMode) {
            case STOPPED:
                Window.showMenu();
                break;
            case PAUSED:
                processEntities(masterRenderer);
                break;
            case BUILDING:
                BuildingPanel.setBuilderPanelVisible();
                if (!mainMap.isTilesShown()) mainMap.setTilesShown(true);

                if (Player.isMouseGrabbed()) {
                    if (Mouse.isButtonDown(2) && !middleClick) {
                        firstMiddleClickPosition = new Vector2f(Mouse.getX(), Mouse.getY());
                    }

                    if (Mouse.isButtonDown(0) && !leftClick) {
                        checkClick();
                    }

                    middleClick = Mouse.isButtonDown(2);
                    leftClick = Mouse.isButtonDown(0);
                    player.move(firstMiddleClickPosition);
                    PositionInfo.lookForChanges(picker);
                }
                picker.update();
                processEntities(masterRenderer);
                break;
            case ONGOING:
                BuildingPanel.setBuilderPanelInvisible();
                if (mainMap.isTilesShown() && !Window.getTilesCheckbox().isSelected()) mainMap.setTilesShown(false);
                player.move(firstMiddleClickPosition);

                if (Player.isMouseGrabbed()) {

                    if (Mouse.isButtonDown(1) && !rightClick) {
                        checkClick();
                    }
                    if (Mouse.isButtonDown(0) && !leftClick) {
                        placeLevitatingEntity();
                        selectEntities(picker, null);
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

                    EntityInfo.lookForChanges();
                    processEntities(masterRenderer);
                }
                GameObserver.checkGameOver(entityCreatorRunnable);
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

    private static void selectEntities(MousePicker picker, Tile tilePosition) {
        GameObserver.lookForChanges();
        Tile tile = tilePosition;
        if (picker != null) {
            tile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
        }

        try {
            RawEntity rawEntity = RawMap.whatIsOnTile(tile);
            boolean entityAdded = false;

            if (rawEntity != null && !rawEntity.isSelected() && selectedEntities.size() < EntityInfo.MULTI_SIZE
                    && !selectedEntities.contains(rawEntity)) {
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
            ActionInfo.lookForChanges();
        } catch (IndexOutOfBoundsException ex) {
            InfoProvider.writeMessage("Out of map.");
        }
    }

    private static void unSelectAllEntities() {
        selectedEntities.clear();
        for (Object rawEntity : getRawEntities()) {
            ((RawEntity) rawEntity).setSelected(false);
        }
    }

    private static void processEntities(MasterRenderer renderer) {
        switch (gameMode) {
            case ONGOING:
                for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
                    Entity entity = it.next();
                    RawEntity rawEntity = entity.getRawEntity();

                    if (rawEntity.isAlive()) {
                        if (!rawEntity.isApproachingEntityAround()) {
                            rawEntity.setBeingAttacked(false);
                            rawEntity.setBeingHealed(false);
                        }

                        if (rawEntity instanceof RawUnit) {
                            RawUnit rawUnit = (RawUnit) rawEntity;
                            if (rawUnit.getDestinationTile() != null) {
                                Tile destinationTile = rawUnit.getDestinationTile();

                                RawEntity destinationEntity = RawMap.whatIsOnTile(destinationTile);
                                if (destinationEntity != null) {
                                    destinationEntity.setApproachingEntity(null, null);
                                    rawUnit.performAction(destinationEntity);
                                } else if (!rawUnit.getPath().isEmpty()) {
                                    rawUnit.step();
                                }
                            }
                        }
                    }
                    if (rawEntity.isMarkedForDeletion) {
                        rawEntity.setApproachingEntity(null, null);
                        rawEntity.setBeingAttacked(false);
                        it.remove();
                        if (selectedEntities.contains(entity.getRawEntity())) {
                            selectedEntities.remove(entity.getRawEntity());
                        }
                        RawMap.setRawEntities();
                        RawMap.lookForChanges();
                        GameObserver.lookForChanges();
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

    public static List<RawEntity> getSelectedEntities() {
        return selectedEntities;
    }

    private static void checkClick() {
        GameObserver.lookForChanges();
        Tile selectedTile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
        switch (gameMode) {
            case ONGOING:
                try {
                    RawEntity destinationEntity = RawMap.whatIsOnTile(selectedTile);

                    if (destinationEntity instanceof RawUnit || destinationEntity == null) {
                        selectedEntities.stream().filter(entity -> entity instanceof RawUnit).forEach(entity -> {
                            RawUnit rawUnit = (RawUnit) entity;
                            rawUnit.setDestinationTile(selectedTile);
                            rawUnit.calculatePath();
                        });
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    InfoProvider.writeMessage("Out of map.");

                }
                break;
            case BUILDING:
                if (levitatingEntity != null) {
                    placeLevitatingEntity();
                } else {
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
                break;
        }
    }

    public static Map getMainMap() {
        return mainMap;
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public static void setEntities(List<Entity> entities) {
        Scene.entities = entities;
    }

    public static GameMode getGameMode() {
        return gameMode;
    }

    public static void setGameMode(GameMode gameMode) {
        if (!gameMode.equals(GameMode.STOPPED)) {
            Window.getGameModeList().setEnabled(true);
        } else {
            Window.getGameModeList().setEnabled(false);
        }
        Scene.gameMode = gameMode;
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

        TexturedModel enemySoldierModel = new TexturedModel(OBJLoader.loadObjModel("enemy", loader),
                new ModelTexture(loader.loadTexture("enemy_texture")));
        modelsMap.put("enemyUnit", enemySoldierModel);

        TexturedModel palmTreeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
                new ModelTexture(loader.loadTexture("palm_tree")));
        modelsMap.put("palmTreeNeutral", palmTreeModel);
    }

    public static List<RawEntity> getRawEntities() {
        return entities.stream().map(Entity::getRawEntity).collect(Collectors.toList());
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
    }

    private static void placeLevitatingEntity() {
        boolean place = false;

        if (levitatingEntity != null) {
            Tile selectedTile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x,
                    picker.getCurrentTerrainPoint().z));

            if (levitatingEntity instanceof Building) {
                RawEntity levitatingRawEntity = levitatingEntity.getRawEntity();
                int[] extentOfLevitatingEntity = ((RawBuilding) levitatingRawEntity).getExtent();

                if (RawMap.areTilesFree(selectedTile, extentOfLevitatingEntity)) {
                    place = true;
                }
            } else if (levitatingEntity instanceof Unit && RawMap.isTileFree(selectedTile, false)) {
                place = true;
            }

            if (place) {
                levitatingEntity.getRawEntity().setTilePosition(selectedTile);
                levitatingEntity.getRawEntity().setPosition(selectedTile.toPosition());
                levitatingEntity.refreshPosition();
                MiniMap.setEntities();
                RawMap.setRawEntities();
                RawMap.lookForChanges();
                levitatingEntity = null;
            }
        }
    }

    public static RawEntity getRandomEntity(Side side) {
        try {
            List<RawEntity> friends = RawMap.getRawEntities().stream().filter(entity ->
                    entity.getSide().equals(side)).collect(Collectors.toList());

            return friends.get(new Random().nextInt(friends.size()));
        } catch (IllegalArgumentException ex) {
            InfoProvider.writeMessage("No " + side + " entity on map currently.");
        }

        return null;
    }

    static void startGame(int nrOfEnemies) {
        entities.clear();
        setEntityCreatorRunnable(new AttackWave(nrOfEnemies));
        player.getCamera().reset();
        InfoProvider.clear();

        TexturedModel palmTreeModel = modelsMap.get("palmTreeNeutral");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(200);
            int z = random.nextInt(200);

            new Entity(palmTreeModel, new Vector3f(x, mainMap.getHeightOfMap(x, z), z), 0, 0, 0, 1);
        }

        float xInitial = 90;
        float zInitial = 100;
        TexturedModel soldierModel = modelsMap.get("soldierUnit");

        for (int i = 0; i < 5; i++) {
            float y = mainMap.getHeightOfMap(xInitial, zInitial);
            new Soldier(soldierModel, new Vector3f(xInitial, y, zInitial), 1, Side.FRIEND);
            xInitial += 5;
        }

        xInitial += 5;
        TexturedModel scientistModel = modelsMap.get("scientistUnit");
        new Scientist(scientistModel, new Vector3f(xInitial, mainMap.getHeightOfMap(xInitial, zInitial), zInitial), 1, Side.FRIEND);

        TexturedModel homeModel = modelsMap.get("homeBuilding");
        xInitial = 100;
        zInitial = 90;
        float y = mainMap.getHeightOfMap(xInitial, zInitial);
        new Home(homeModel, new Vector3f(xInitial, y, zInitial), 1, Side.FRIEND);

        //menuFrame.setVisible(false);
        Player.setIsMouseGrabbed(true);

        MiniMap.setEntities();
        RawMap.setRawEntities();
        RawMap.lookForChanges();

        setGameMode(GameMode.ONGOING);
        GameObserver.lookForChanges();
        if (service != null) {
            service.cancel(true);
        }
        service = scheduler.scheduleAtFixedRate(entityCreatorRunnable, 10, 120, TimeUnit.SECONDS);
    }

    public static AttackWave getEntityCreatorRunnable() {
        return entityCreatorRunnable;
    }

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static Future getService() {
        return service;
    }

    public static void setService(Future service) {
        Scene.service = service;
    }
}
