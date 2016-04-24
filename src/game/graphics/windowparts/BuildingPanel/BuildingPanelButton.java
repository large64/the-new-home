package game.graphics.windowparts.BuildingPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Dénes on 2016. 04. 23..
 */
public class BuildingPanelButton extends JLabel {
    public BuildingPanelButton(String fileName) {
        ImageIcon image = new ImageIcon("res/" + fileName + ".png");
        this.setIcon(image);
        this.setName(fileName);
        this.addMouseListener(new BuildingPanelButtonMouseListener(this));
    }
}