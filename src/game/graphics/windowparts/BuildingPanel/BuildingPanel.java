package game.graphics.windowparts.buildingpanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by Dénes on 2016. 04. 23..
 */
public class BuildingPanel {
    private static JPanel buildingPanel;

    public BuildingPanel() {
        buildingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buildingPanel.setPreferredSize(new Dimension(450, game.graphics.windowparts.Window.getBottomComponentHeight()));
        buildingPanel.setOpaque(false);
        buildingPanel.setVisible(false);
        buildingPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
    }

    public static void setBuilderPanelVisible() {
        if (!buildingPanel.isVisible()) {
            buildingPanel.setVisible(true);
        }
    }

    public static void setBuilderPanelInvisible() {
        if (buildingPanel.isVisible()) {
            buildingPanel.setVisible(false);
        }
    }

    public static JPanel getPanel() {
        return buildingPanel;
    }

    public static void addButton(BuildingPanelButton button) {
        buildingPanel.add(button);
    }
}
