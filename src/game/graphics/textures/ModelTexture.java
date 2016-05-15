package game.graphics.textures;

public class ModelTexture {
    private int textureID;

    private boolean hasTransparency = false;
    private boolean useFakeLighting = false;
    private int numberOfRows = 1;

    public ModelTexture(int id) {
        this.textureID = id;
    }

    public boolean isUseFakeLighting() {
        return useFakeLighting;
    }

    public void setUseFakeLighting(boolean useFakeLighting) {
        this.useFakeLighting = useFakeLighting;
    }

    public boolean isHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public int getID() {
        return this.textureID;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
