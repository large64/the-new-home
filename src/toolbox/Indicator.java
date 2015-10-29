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

    public Indicator(String type) {
        switch (type) {
            case "text":
                textPane = new JTextPane();
                textPane.setEnabled(false);
                textPane.setDisabledTextColor(new Color(0, 0, 0));
                break;
            default:
                System.err.println("No such type of Indicator.");
        }
    }

    public JTextPane getTextPane() {
        return textPane;
    }

    public static void lookForChanges() {
        if (Camera.isMouseGrabbed) {
            textPane.setText("Mouse grabbed");
        }
        else {
            textPane.setText("Mouse free");
        }
    }
}