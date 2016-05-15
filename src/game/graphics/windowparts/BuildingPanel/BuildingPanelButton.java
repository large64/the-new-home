package game.graphics.windowparts.buildingpanel;

import javax.swing.*;

public class BuildingPanelButton extends JLabel {
    public BuildingPanelButton(String fileName, String tooltipString) {
        ImageIcon image = new ImageIcon("res/" + fileName + ".png");
        this.setIcon(image);
        this.setName(fileName);
        this.setToolTipText(tooltipString);
        this.addMouseListener(new BuildingPanelButtonMouseListener(this));
    }
}
