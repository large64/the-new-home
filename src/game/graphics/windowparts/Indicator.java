package game.graphics.windowparts;

import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import game.graphics.toolbox.MousePicker;

import javax.swing.*;
import java.awt.*;

/**
 * Created by large64 on 23/10/15.
 */
public class Indicator {
    private static final String[] types = {"button", "text"};
    private static JTextPane textPane;
    private static Color color;

    public Indicator(String type) {
        switch (type) {
            case "text":
                textPane = new JTextPane();
                textPane.setEnabled(false);
                textPane.setOpaque(false);
                textPane.setDisabledTextColor(new Color(0, 0, 0));
                break;
            default:
                System.err.println("No such type of Indicator.");
        }
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public static void setColor(Color color) {
        textPane.setOpaque(true);
        textPane.setBackground(color);
    }

    public static void lookForChanges(MousePicker picker) {
        try {
            float x = picker.getCurrentTerrainPoint().getX();
            float y = picker.getCurrentTerrainPoint().getY();
            float z = picker.getCurrentTerrainPoint().getZ();
            textPane.setText(
                    "x: " + x + "\n" +
                    "y: " + y + "\n" +
                    "z: " + z  + "\n" +
                    "tX: " + Tile.positionToTile(new Position(x, z)).getRow() + "\n" +
                    "tZ: " + Tile.positionToTile(new Position(x, z)).getColumn()
            );
        } catch (NullPointerException e) {
            System.err.println("Could not determine mouse position. MAX_ZOOM must be too small.");
        }
    }
}
