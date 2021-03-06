package game.graphics.windowparts.buildingpanel;

import game.graphics.windowparts.Window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BuildingPanel {
    private static JPanel buildingPanel;

    public BuildingPanel() {
        buildingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buildingPanel.setPreferredSize(new Dimension(450, Window.BOTTOM_COMPONENT_HEIGHT));
        buildingPanel.setOpaque(false);
        buildingPanel.setVisible(false);
        buildingPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
    }

    public static void setBuilderPanelVisible() {
        if (!buildingPanel.isVisible()) {
            buildingPanel.setVisible(true);
            Window.getEntityInfoPanel().setVisible(false);
        }
    }

    public static void setBuilderPanelInvisible() {
        if (buildingPanel.isVisible()) {
            buildingPanel.setVisible(false);
            Window.getEntityInfoPanel().setVisible(true);
        }
    }

    public static JPanel getPanel() {
        return buildingPanel;
    }

    public static void addButton(Component button) {
        buildingPanel.add(button);
    }
}
