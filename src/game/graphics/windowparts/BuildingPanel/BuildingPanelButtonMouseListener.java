package game.graphics.windowparts.BuildingPanel;

import game.graphics.entities.buildings.Barrack;
import game.graphics.entities.buildings.Home;
import game.graphics.entities.buildings.Hospital;
import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.toolbox.Side;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * Created by Dénes on 2016. 04. 23..
 */
public class BuildingPanelButtonMouseListener extends MouseAdapter {
    private JLabel button;

    BuildingPanelButtonMouseListener(JLabel button) {
        this.button = button;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        Map<String, TexturedModel> modelsMap = Scene.getModelsMap();

        button.setBorder(null);

        switch (button.getName()) {
            case "home":
                TexturedModel homeModel = modelsMap.get("homeBuilding");
                Home home = new Home(homeModel, 1, Side.FRIEND);
                Scene.addRawEntity(home.getRawEntity());
                Scene.addEntity(home);
                Scene.setLevitatingEntity(home);
                break;
            case "hospital":
                TexturedModel hospitalModel = modelsMap.get("hospitalBuilding");
                Hospital hospital = new Hospital(hospitalModel, 1, Side.FRIEND);
                Scene.addRawEntity(hospital.getRawEntity());
                Scene.addEntity(hospital);
                Scene.setLevitatingEntity(hospital);
                break;
            case "barrack":
                TexturedModel barrackModel = modelsMap.get("barrackBuilding");
                Barrack barrack = new Barrack(barrackModel, 1, Side.FRIEND);
                Scene.addRawEntity(barrack.getRawEntity());
                Scene.addEntity(barrack);
                Scene.setLevitatingEntity(barrack);
                break;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }
}
