package game.graphics.textures;

public class ModelTexture {
    private final int textureID;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;
    private int numberOfRows = 1;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public int getID() {
        return this.textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }
}
