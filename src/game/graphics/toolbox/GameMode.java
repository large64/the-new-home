package game.graphics.toolbox;

public enum GameMode {
    ONGOING, PAUSED, BUILDING, STOPPED;

    public static String getGameModeString(GameMode gameMode) {
        String mode = "";
        switch (gameMode) {
            case ONGOING:
                mode = "Play";
                break;
            case BUILDING:
                mode = "Build";
                break;
        }

        return mode;
    }

    public static String[] getGameModes() {
        return new String[]{"Play", "Build"};
    }
}
