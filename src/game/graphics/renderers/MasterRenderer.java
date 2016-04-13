package game.graphics.renderers;

import game.graphics.entities.*;
import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.shaders.StaticShader;
import game.graphics.shaders.TerrainShader;
import game.graphics.textures.ModelTexture;
import game.graphics.textures.TerrainTexture;
import game.graphics.textures.TerrainTexturePack;
import game.graphics.toolbox.DisplayManager;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.MousePicker;
import game.graphics.toolbox.OBJLoader;
import game.graphics.windowparts.*;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by large64 on 2015.09.17..
 */
public class MasterRenderer {
    private static final float FOV = 70; // Field of view
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static Tile selectedTile;
    private static MasterRenderer masterRenderer;
    private static MousePicker picker;
    private static Loader loader;
    private static Scene scene;

    private StaticShader shader = new StaticShader();
    private java.util.Map entityMap = new HashMap<>();
    private EntityRenderer renderer;
    private Matrix4f projectionMatrix;
    private MapRenderer mapRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        this.renderer = new EntityRenderer(shader, projectionMatrix);
        this.mapRenderer = new MapRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    private void render(List<Light> lights, Camera camera, ArrayList<Map> maps) {
        prepare();
        shader.start();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entityMap);
        shader.stop();

        terrainShader.start();
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        mapRenderer.render(maps);
        terrainShader.stop();
        skyboxRenderer.render(camera);

        entityMap.clear();
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.2f, 0.3f, 1);
    }

    /**
     * Processes an entity
     * @param entity The entity that is to be processed
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = (List<Entity>) entityMap.get(entityModel);

        if (entity instanceof game.graphics.entities.units.Unit) {
            ((game.graphics.entities.units.Unit) entity).refreshPosition();
        }

        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entityMap.put(entityModel, newBatch);
        }
    }

    private void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static void renderScene() {
        // @TODO: make game loader work for map, too
        // @TODO: create edit mode and place entities
        // @TODO: create selectable actions
        // @TODO: create new frames for actions
        DisplayManager.createDisplay();
        loader = new Loader();

        // Set additional things like renderer, picker
        masterRenderer = new MasterRenderer(loader);

        selectedTile = null;
        EntityInfo.setEntities(Scene.getSelectedEntities());

        new Scene();

        // Start an infinite loop for rendering
        while(!Display.isCloseRequested()) {
            masterRenderer.render(Scene.getLights(), Scene.getPlayer().getCamera(), Scene.getMaps());
            Scene.render();
            DisplayManager.updateDisplay();
        }

        //guiRenderer.cleanUp();
        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public static Tile getSelectedTile() {
        return selectedTile;
    }

    public static void setSelectedTile(Tile selectedTile) {
        MasterRenderer.selectedTile = selectedTile;
    }

    public static MasterRenderer getMasterRenderer() {
        return masterRenderer;
    }

    public static Loader getLoader() {
        return loader;
    }
}
