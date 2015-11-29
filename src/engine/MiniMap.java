package engine;

import engine.entities.RawEntity;
import engine.toolbox.Position;
import terrains.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class MiniMap {
    private static final Color BASE_TILE_COLOR = new Color(183, 177, 42);
    private static final Color BEING_HEALED_COLOR = new Color(3, 132, 24);
    private static final Color BEING_ATTACKED_COLOR = new Color(255, 0, 0);
    private static final Color MARKED_COLOR = new Color(0, 0, 150);
    private static final Color BASE_ENTITY_COLOR = new Color(0, 13, 255);

    private static int size = (int) Map.getSIZE();
    private static boolean[][] places;
    private static BufferedImage image;
    private static List<RawEntity> entities = new ArrayList<>();
    private static List<Integer> markers = new ArrayList<>();
    private static JLabel label = new JLabel();

    public MiniMap() {
        refresh();
    }

    public static void refresh() {
        int w = 150;
        int h = 150;
        int type = BufferedImage.TYPE_INT_RGB;

        MiniMap.image = new BufferedImage(w, h, type);
        MiniMap.places = new boolean[w][h];

        if (!entities.isEmpty()) {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    MiniMap.image.setRGB(x, y, BASE_TILE_COLOR.getRGB());
                    places[x][y] = true;
                    for (RawEntity entity : entities) {
                        if (entity.getPosition().getRow() == x && entity.getPosition().getColumn() == y) {
                            MiniMap.image.setRGB(x, y, BASE_ENTITY_COLOR.getRGB());
                            places[x][y] = false;
                        }
                    }
                }
            }
        }

        ImageIcon icon = new ImageIcon(MiniMap.image);
        label.setIcon(icon);
    }

    public synchronized static void lookForChanges() {
        //repaint(false);
        //drawingPanel.setPreferredSize(new Dimension(150, 150));
        //drawingPanel.revalidate();
        int w = 150;
        int h = 150;
        int type = BufferedImage.TYPE_INT_RGB;

        MiniMap.image = new BufferedImage(w, h, type);
        MiniMap.places = new boolean[w][h];

        if (!entities.isEmpty()) {
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    MiniMap.image.setRGB(x, y, BASE_TILE_COLOR.getRGB());
                    places[x][y] = true;
                    for (RawEntity entity : entities) {
                        if (entity.getPosition().getRow() == x && entity.getPosition().getColumn() == y) {
                            MiniMap.image.setRGB(x, y, BASE_ENTITY_COLOR.getRGB());
                            places[x][y] = false;
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

    public static boolean isPositionFree(Position position) {
        return MiniMap.places[position.getRow()][position.getColumn()];
    }

    public static boolean isPositionFree(Position position, boolean isDestination) {
        return isDestination || MiniMap.places[position.getRow()][position.getColumn()];
    }

    public static void mark(int position) {
        MiniMap.markers.add(position);
    }

    public static void setEntities(List entities) {
        MiniMap.entities = entities;
    }

    public static void setSize(int size) {
        MiniMap.size = (size * size);
    }

    public static JLabel getLabel() {
        return label;
    }
}
