package game.graphics.windowparts.buildingpanel;

import game.graphics.entities.buildings.Barrack;
import game.graphics.entities.buildings.Home;
import game.graphics.entities.buildings.Hospital;
import game.graphics.models.TexturedModel;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.InfoProvider;
import game.graphics.windowparts.Scene;
import game.logic.exceptions.EnemyNearbyException;
import game.logic.toolbox.GameObserver;
import game.logic.toolbox.Side;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

class BuildingPanelButtonMouseListener extends MouseAdapter {
    private JLabel button;

    BuildingPanelButtonMouseListener(JLabel button) {
        this.button = button;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        button.setBorder(null);
        if (Scene.getGameMode().equals(GameMode.BUILDING)) {
            GameObserver.lookForChanges();
            if (GameObserver.getNumberOfEnemyEntities() > 0) {
                try {
                    throw new EnemyNearbyException();
                } catch (EnemyNearbyException e1) {
                    InfoProvider.writeMessage("Cannot build now. Enemies nearby!");
                }
            } else {
                Map<String, TexturedModel> modelsMap = Scene.getModelsMap();

                button.setBorder(null);

                switch (button.getName()) {
                    case "home":
                        TexturedModel homeModel = modelsMap.get("homeBuilding");
                        Home home = new Home(homeModel, 1, Side.FRIEND);
                        Scene.setLevitatingEntity(home);
                        break;
                    case "hospital":
                        TexturedModel hospitalModel = modelsMap.get("hospitalBuilding");
                        Hospital hospital = new Hospital(hospitalModel, 1, Side.FRIEND);
                        Scene.setLevitatingEntity(hospital);
                        break;
                    case "barrack":
                        TexturedModel barrackModel = modelsMap.get("barrackBuilding");
                        Barrack barrack = new Barrack(barrackModel, 1, Side.FRIEND);
                        Scene.setLevitatingEntity(barrack);
                        break;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }
}
