package game.graphics.windowparts;

import game.logic.entities.RawEntity;
import game.logic.toolbox.map.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class MiniMap {
    // @TODO: show which part of the map the user is currently on (viewport)
    private static final Color BASE_TILE_COLOR = new Color(183, 177, 42);
    private static final Color BEING_HEALED_COLOR = new Color(3, 132, 24);
    private static final Color BEING_ATTACKED_COLOR = new Color(255, 0, 0);
    private static final Color MARKED_COLOR = new Color(0, 0, 150);
    private static final Color BASE_ENTITY_COLOR = new Color(0, 13, 255);

    private static final float MAPPING_RATIO = 1.6f;

    private static int size = (int) Map.getSIZE();
    private static List<RawEntity> entities = new ArrayList<>();
    private static boolean[][] places = new boolean[size][size];

    private static int mappedSize = (int) (size / MAPPING_RATIO);
    private static BufferedImage image = new BufferedImage(mappedSize, mappedSize, BufferedImage.TYPE_INT_RGB);;
    private static List<Position> markers = new ArrayList<>();
    private static JLabel label = new JLabel();

    public MiniMap() {
        MiniMap.lookForChanges();
    }

    public static void lookForChanges() {
        if (!entities.isEmpty()) {
            for (int x = 0; x < mappedSize; x++) {
                for (int y = 0; y < mappedSize; y++) {
                    MiniMap.image.setRGB(x, y, BASE_TILE_COLOR.getRGB());
                    places[x][y] = true;
                    for (RawEntity entity : entities) {
                        if (entity.isAlive()) {
                            Position entityPosition = entity.getTilePosition().toPosition();
                            if ((int) (entityPosition.getRow() / MAPPING_RATIO) == x && (int) (entityPosition.getColumn() / MAPPING_RATIO) == y) {
                                MiniMap.image.setRGB(x, y, BASE_ENTITY_COLOR.getRGB());
                                places[entity.getTilePosition().getRow()][entity.getTilePosition().getColumn()] = false;

                                if (entity.isBeingAttacked()) {
                                    MiniMap.image.setRGB(x, y, BEING_ATTACKED_COLOR.getRGB());
                                }
                                if (entity.isBeingHealed()) {
                                    MiniMap.image.setRGB(x, y, BEING_HEALED_COLOR.getRGB());
                                }
                            }
                        }
                        else {
                            MiniMap.image.setRGB(x, y, BASE_TILE_COLOR.getRGB());
                        }
                    }

                    for (ListIterator iterator = markers.listIterator(); iterator.hasNext();) {
                        Position marker = (Position) iterator.next();
                        if ((int) (marker.getRow() / MAPPING_RATIO) == x && (int) (marker.getColumn() / MAPPING_RATIO) == y) {
                            MiniMap.image.setRGB(x, y, MARKED_COLOR.getRGB());
                        }
                    }
                }
            }
        }

        ImageIcon icon = new ImageIcon(MiniMap.image);
        label.setIcon(icon);
    }

    /**
     * Returns the number of rows of the map matrix that equals to the number of columns
     */
    public static int getRowNumber() {
        return MiniMap.size;
    }

    public static List getEntities() {
        return MiniMap.entities;
    }

    public static void mark(Position position) {
        MiniMap.markers.add(position);
    }

    public static void clearMarkers() {
        MiniMap.markers.clear();
    }

    public static void setEntities(List entities) {
        MiniMap.entities = entities;
    }

    public static JLabel getLabel() {
        return label;
    }
}
