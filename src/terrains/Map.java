package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by large64 on 9/22/15.
 */
public class Map {
    private static final float SIZE = 500;
    private static final float MAX_HEIGHT = 0;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x;
    private float z;
    private RawModel model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[][] heights;

    public Map(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack,
               TerrainTexture blendMap, String heightOfMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = generateMap(loader, heightOfMap);
    }

    private RawModel generateMap(Loader loader, String heightMap) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            System.err.println("Could not load height map.");
            e.printStackTrace();
        }

        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];

        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoordinates = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
        int vertexPointer = 0;

        for (int i = 0; i < VERTEX_COUNT; i++) {
            for (int j = 0; j < VERTEX_COUNT; j++) {
                vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
                float height = getHeight(j, i, image);
                heights[j][i] = height;
                vertices[vertexPointer * 3 + 1] = height;
                vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoordinates[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
                textureCoordinates[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
            for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
                int topLeft = (gz * VERTEX_COUNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoordinates, normals, indices);
    }

    private float getHeight(int x, int y, BufferedImage heightMap) {
        if (x < 0 || x >= heightMap.getHeight() || y < 0 || y >= heightMap.getHeight()) {
            return 0;
        }

        float height = heightMap.getRGB(x, y);
        height += MAX_PIXEL_COLOR / 2f;
        height /= MAX_PIXEL_COLOR / 2f;
        height *= MAX_HEIGHT;

        return height;
    }

    private Vector3f calculateNormal(int x, int y, BufferedImage image) {
        float heightLeft = getHeight(x - 1, y, image);
        float heightRight = getHeight(x + 1, y, image);
        float heightUpper = getHeight(x, y - 1, image);
        float heightLower = getHeight(x, y + 1, image);

        Vector3f normal = new Vector3f(heightLeft - heightRight, 2f, heightLower - heightUpper);
        normal.normalise();

        return normal;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfMap(float worldX, float worldY) {
        float terrainX = worldX - this.x;
        float terrainY = worldY - this.z;

        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridY = (int) Math.floor(terrainY / gridSquareSize);

        if (gridX >= heights.length - 1 || gridY >= heights.length - 1 || gridX < 0 || gridY < 0) {
            return 0;
        }

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float yCoord = (terrainY % gridSquareSize) / gridSquareSize;

        float result;
        if (xCoord <= (1 - yCoord)) {
            result = Maths.barryCentric(
                    new Vector3f(0, heights[gridX][gridY], 0),
                    new Vector3f(1, heights[gridX + 1][gridY], 0),
                    new Vector3f(0, heights[gridX][gridY + 1], 1),
                    new Vector2f(xCoord, yCoord)
            );
        } else {
            result = Maths.barryCentric(
                    new Vector3f(1, heights[gridX + 1][gridY], 0),
                    new Vector3f(1, heights[gridX + 1][gridY + 1], 1),
                    new Vector3f(0, heights[gridX][gridY + 1], 1),
                    new Vector2f(xCoord, yCoord)
            );
        }

        return result;
    }

    public static boolean isPointOnMap(float x, float z) {
        return (x < SIZE && z < SIZE);
    }
}