package game.graphics.renderers;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.entities.Light;
import game.graphics.entities.units.Unit;
import game.graphics.models.TexturedModel;
import game.graphics.shaders.StaticShader;
import game.graphics.shaders.TerrainShader;
import game.graphics.toolbox.DisplayManager;
import game.graphics.toolbox.Loader;
import game.graphics.windowparts.Map;
import game.graphics.windowparts.Scene;
import game.graphics.windowparts.infopanels.EntityInfo;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterRenderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private static MasterRenderer instance;
    private static Loader loader;

    private final StaticShader shader = new StaticShader();
    private final java.util.Map<TexturedModel, List<Entity>> entityMap = new HashMap<>();
    private final EntityRenderer renderer;
    private final MapRenderer mapRenderer;
    private final TerrainShader terrainShader = new TerrainShader();
    private Matrix4f projectionMatrix;


    private MasterRenderer() {
        enableCulling();
        createProjectionMatrix();
        this.renderer = new EntityRenderer(shader, projectionMatrix);
        this.mapRenderer = new MapRenderer(terrainShader, projectionMatrix);
    }

    static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public static void renderScene() {
        DisplayManager.createDisplay();
        loader = new Loader();

        // Set additional things like renderer, picker
        instance = new MasterRenderer();

        EntityInfo.setEntities(Scene.getSelectedEntities());

        new Scene();

        // Start an infinite loop for rendering
        while (!Display.isCloseRequested()) {
            Scene.render();
            instance.render(Scene.getLights(), Scene.getPlayer().getCamera(), Scene.getMaps());
            DisplayManager.updateDisplay();
        }

        instance.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public static Loader getLoader() {
        return loader;
    }

    public static MasterRenderer getInstance() {
        if (instance == null) {
            instance = new MasterRenderer();
        }
        return instance;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
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

        entityMap.clear();
    }

    private void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.2f, 0.3f, 1);
    }

    /**
     * Processes an entity
     *
     * @param entity The entity that is to be processed
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entityMap.get(entityModel);

        if (entity instanceof Unit) {
            entity.refreshPosition();
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
}
