package game.graphics.windowparts.BuildingPanel;

import game.graphics.entities.buildings.Home;
import game.graphics.models.TexturedModel;
import game.graphics.renderers.MasterRenderer;
import game.graphics.textures.ModelTexture;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.OBJLoader;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Dénes on 2016. 04. 23..
 */
public class BuildingPanelButtonMouseListener extends MouseAdapter {
    private JLabel button;

    public BuildingPanelButtonMouseListener(JLabel button) {
        this.button = button;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        button.setBorder(null);

        switch (button.getName()) {
            case "home":
                Home home = new Home(Scene.getHomeModel(), 1, Side.FRIEND);
                Scene.addRawEntity(home.getRawEntity());
                Scene.addEntity(home);
                Scene.setLevitatingEntity(home);
                break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }
}
