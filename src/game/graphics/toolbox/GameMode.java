package game.graphics.toolbox;

public enum GameMode {
    ONGOING, PAUSED, BUILDING, STOPPED;

    public static String[] getGameModes() {
        return new String[]{"Play", "Build"};
    }
}
