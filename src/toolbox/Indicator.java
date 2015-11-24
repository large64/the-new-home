package toolbox;

import entities.Camera;

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
            textPane.setText(
                    "x: " + picker.getCurrentTerrainPoint().getX() + "\n" +
                    "y: " + picker.getCurrentTerrainPoint().getY() + "\n" +
                    "z: " +  picker.getCurrentTerrainPoint().getZ()
            );
        } catch (NullPointerException e) {
            System.err.println("Could not determine mouse position. MAX_ZOOM must be too small.");
        }
    }
}
