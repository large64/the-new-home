package engine;

import engine.entities.RawEntity;
import engine.toolbox.Position;
import renderEngine.MasterRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class MiniMap {
    private static final Color BASE_TILE_COLOR = new Color(0, 0, 0);
    private static final Color BEING_HEALED_COLOR = new Color(3, 132, 24);
    private static final Color BEING_ATTACKED_COLOR = new Color(255, 0, 0);
    private static final Color MARKED_COLOR = new Color(0, 0, 150);

    private static int size = 30;
    private static boolean[] places;
    private static List entities = new ArrayList<>();
    private static JPanel frame;
    private static JPanel drawingPanel;
    private static List<Integer> markers = new ArrayList<>();

    public MiniMap() {
        places = new boolean[size * size];
        drawingPanel = new JPanel();
        frame = new JPanel();
        GridLayout gridLayout = new GridLayout(size, size);

        gridLayout.setHgap(2);
        gridLayout.setVgap(2);

        drawingPanel.setLayout(gridLayout);
        //frame.setUndecorated(true);
        repaint(true);
        frame.add(drawingPanel);
    }

    private static void repaint(boolean first) {
        for (int i = 0; i < getSize(); i++) {
            JTextPane toAdd;
            places[i] = true;

            if (first) {
                toAdd = new JTextPane();
            } else {
                toAdd = (JTextPane) drawingPanel.getComponent(i);
            }

            toAdd.setEnabled(false);
            toAdd.setDisabledTextColor(new Color(255, 255, 255));
            toAdd.setText("");
            toAdd.setBackground(BASE_TILE_COLOR);

            for (Object object : entities) {
                RawEntity rawEntity = (RawEntity) object;

                if (rawEntity.isAlive() && rawEntity.getPosition().convertToMatrixPosition() == i) {
                    String text = rawEntity.toString();

                    if (rawEntity.isBeingAttacked()) {
                        toAdd.setBackground(BEING_ATTACKED_COLOR);
                    }

                    if (rawEntity.isBeingHealed()) {
                        toAdd.setBackground(BEING_HEALED_COLOR);
                    }

                    toAdd.setText(text);
                    places[i] = false;
                }
            }
            if (markers.contains(i)) {
                toAdd.setBackground(MARKED_COLOR);
            }
            if (first) {
                drawingPanel.add(toAdd);
            }
        }
    }

    public static int getSize() {
        return (size * size);
    }

    public static void setSize(int size) {
        MiniMap.size = size;
    }

    public synchronized static void lookForChanges() {
        repaint(false);
        drawingPanel.revalidate();
    }

    /**
     * Returns the number of rows of the map matrix that equals to the number of columns
     */
    public static int getRowNumber() {
        return size;
    }

    public static List getEntities() {
        return entities;
    }

    public static boolean isPositionFree(Position position) {
        return places[position.convertToMatrixPosition()];
    }

    public static boolean isPositionFree(Position position, boolean isDestination) {
        return isDestination || places[position.convertToMatrixPosition()];
    }

    public static void mark(int position) {
        markers.add(position);
    }

    public static void setEntities(List entities) {
        MiniMap.entities = entities;
    }

    public static JPanel getFrame() {
        return frame;
    }
}
