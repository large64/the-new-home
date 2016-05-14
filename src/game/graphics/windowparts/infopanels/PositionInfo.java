package game.graphics.windowparts.infopanels;

import game.graphics.toolbox.MousePicker;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import javax.swing.*;
import java.awt.*;

/**
 * Created by large64 on 23/10/15.
 */
public class PositionInfo {
    private static JTextPane textPane;

    public PositionInfo() {
        textPane = new JTextPane();
        textPane.setEnabled(false);
        textPane.setOpaque(false);
        textPane.setDisabledTextColor(new Color(0, 0, 0));
        textPane.setText(
                "x:\n" +
                        "y:\n" +
                        "z:\n" +
                        "tX:\n" +
                        "tZ: "
        );
    }

    public static void lookForChanges(MousePicker picker) {
        try {
            float x = picker.getCurrentTerrainPoint().getX();
            float y = picker.getCurrentTerrainPoint().getY();
            float z = picker.getCurrentTerrainPoint().getZ();
            textPane.setText(
                    "x: " + x + "\n" +
                            "y: " + y + "\n" +
                            "z: " + z + "\n" +
                            "tX: " + Tile.positionToTile(new Position(x, z)).getRow() + "\n" +
                            "tZ: " + Tile.positionToTile(new Position(x, z)).getColumn()
            );
        } catch (NullPointerException e) {
            System.err.println("Could not determine mouse position. MAX_ZOOM must be too small.");
        }
    }

    public JTextPane getTextPane() {
        return textPane;
    }
}
