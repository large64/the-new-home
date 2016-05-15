package game.graphics.renderers;

import game.graphics.entities.Entity;
import game.graphics.models.RawModel;
import game.graphics.models.TexturedModel;
import game.graphics.shaders.StaticShader;
import game.graphics.textures.ModelTexture;
import game.logic.toolbox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

class EntityRenderer {
    private final StaticShader shader;
    private final float shineDamper = 1;
    private final float reflectivity = 0;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);

            for (Entity entity : batch) {
                prepareInstance(entity);
                if (entity.getRawEntity().isSelected()) {
                    shader.loadShineVariables(1, 1.5f);
                } else {
                    shader.loadShineVariables(shineDamper, reflectivity);
                }
                if (entity.getRawEntity().isBeingAttacked()) {
                    shader.loadIsBeingAttacked(true);
                } else {
                    shader.loadIsBeingAttacked(false);
                }
                if (entity.getRawEntity().isBeingHealed()) {
                    shader.loadIsBeingHealed(true);
                } else {
                    shader.loadIsBeingHealed(false);
                }
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel model) {
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumberOfRows());

        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }

        shader.loadFakeLightingVariable(texture.isUseFakeLighting());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    private void unbindTexturedModel() {
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());

        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }
}
