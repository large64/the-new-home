package game.graphics.windowparts.BuildingPanel;

import javax.swing.*;

/**
 * Created by Dénes on 2016. 04. 23..
 */
public class BuildingPanelButton extends JLabel {
    public BuildingPanelButton(String fileName, String tooltipString) {
        ImageIcon image = new ImageIcon("res/" + fileName + ".png");
        this.setIcon(image);
        this.setName(fileName);
        this.setToolTipText(tooltipString);
        this.addMouseListener(new BuildingPanelButtonMouseListener(this));
    }
}
