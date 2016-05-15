package game.graphics.windowparts;

import game.logic.entities.RawEntity;
import game.logic.entities.RawNeutral;
import game.logic.entities.buildings.RawBuilding;
import game.logic.toolbox.map.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MiniMap {
    // @TODO: show which part of the map the user is currently on (viewport)
    private static final Color BASE_TILE_COLOR = new Color(183, 177, 42);
    private static final Color BEING_HEALED_COLOR = new Color(3, 132, 24);
    private static final Color BEING_ATTACKED_COLOR = new Color(255, 0, 0);
    private static final Color MARKED_COLOR = new Color(0, 0, 150);
    private static final Color BASE_ENTITY_COLOR = new Color(0, 13, 255);

    private final static float MAPPING_RATIO = 1.6f;

    private final static int SIZE = (int) Map.getSIZE();
    private static List<RawEntity> entities = new ArrayList<>();

    private final static int MAPPED_SIZE = (int) (SIZE / MAPPING_RATIO);
    private final static BufferedImage IMAGE = new BufferedImage(MAPPED_SIZE, MAPPED_SIZE, BufferedImage.TYPE_INT_RGB);
    private final static List<Position> MARKERS = new ArrayList<>();
    private final static JLabel LABEL = new JLabel();

    public MiniMap() {
        MiniMap.lookForChanges();
    }

    static void lookForChanges() {
        if (!entities.isEmpty()) {
            for (int x = 0; x < MAPPED_SIZE; x++) {
                for (int y = 0; y < MAPPED_SIZE; y++) {
                    MiniMap.IMAGE.setRGB(x, y, BASE_TILE_COLOR.getRGB());
                    for (RawEntity entity : entities) {
                        if (entity.isAlive() && !(entity instanceof RawNeutral)) {
                            Position entityPosition = entity.getTilePosition().toPosition();
                            if ((int) (entityPosition.getRow() / MAPPING_RATIO) == x && (int) (entityPosition.getColumn() / MAPPING_RATIO) == y) {
                                MiniMap.IMAGE.setRGB(x, y, BASE_ENTITY_COLOR.getRGB());

                                // Indicate buildings by using their extensions
                                if (entity instanceof RawBuilding && ((RawBuilding) entity).hasExtent()) {
                                    int extentX = (int) (((RawBuilding) entity).getExtentX() / MAPPING_RATIO);
                                    int extentY = (int) (((RawBuilding) entity).getExtentY() / MAPPING_RATIO);
                                    for (int i = (x - extentX); i < (x + extentX); i++) {
                                        for (int j = (y - extentY); j < (y + extentY); j++) {
                                            MiniMap.IMAGE.setRGB(i, j, BASE_ENTITY_COLOR.getRGB());

                                            if (entity.isBeingAttacked()) {
                                                MiniMap.IMAGE.setRGB(i, j, BEING_ATTACKED_COLOR.getRGB());
                                            }
                                            if (entity.isBeingHealed()) {
                                                MiniMap.IMAGE.setRGB(i, j, BEING_HEALED_COLOR.getRGB());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (Position marker : MARKERS) {
                        if ((int) (marker.getRow() / MAPPING_RATIO) == x && (int) (marker.getColumn() / MAPPING_RATIO) == y) {
                            MiniMap.IMAGE.setRGB(x, y, MARKED_COLOR.getRGB());
                        }
                    }
                }
            }
        }

        ImageIcon icon = new ImageIcon(MiniMap.IMAGE);
        LABEL.setIcon(icon);
    }

    public static List getEntities() {
        return MiniMap.entities;
    }

    public static void mark(Position position) {
        MiniMap.MARKERS.add(position);
    }

    public static void clearMarkers() {
        MiniMap.MARKERS.clear();
    }

    public static void setEntities() {
        MiniMap.entities = Scene.getRawEntities();
    }

    static JLabel getLabel() {
        return LABEL;
    }
}
