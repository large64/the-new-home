package toolbox;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private Map indicators = new HashMap<String, Indicator>();
    private Map buttons = new HashMap<String, JButton>();
    private Map panels = new HashMap<String, JPanel>();
    private Map colors = new HashMap<String, Color>();
    private Map borders = new HashMap<String, Border>();

    private static JFrame mainFrame;
    private static JPanel menuWrapperPanel;

    public Window(Canvas canvas) {
        initElements();

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuWrapperPanel = new JPanel();
        menuWrapperPanel.setBackground((Color) colors.get("darkGreen"));
        JPanel menuPanel = (JPanel) panels.get("menuPanel");
        menuPanel.setOpaque(false);

        JPanel indicatorPanel = (JPanel) panels.get("indicatorPanel");
        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));

        menuPanel.add((JButton) buttons.get("button1"));
        menuPanel.add((JButton) buttons.get("button2"));

        Indicator btnIndicator = (Indicator) indicators.get("btnIndicator");
        indicatorPanel.add(btnIndicator.getTextPane());

        menuWrapperPanel.add(menuPanel, BorderLayout.NORTH);

        mainFrame.add(menuWrapperPanel, BorderLayout.WEST);
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setVisible(true);

        menuPanel.setBorder((Border) borders.get("menuPanelBorder"));
    }

    private void initElements() {
        Color menuBgColor = new Color(2, 120, 0);
        colors.put("darkGreen", menuBgColor);

        Indicator btnIndicator = new Indicator("text");
        indicators.put("btnIndicator", btnIndicator);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 10, 20, 10);
        borders.put("menuPanelBorder", menuPanelBorder);

        JButton button = new JButton("Test");
        JButton button1 = new JButton("Test2");
        buttons.put("button1", button);
        buttons.put("button2", button1);

        JPanel menuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new GridLayout(1,1));
        panels.put("menuPanel", menuPanel);
        panels.put("indicatorPanel", indicatorPanel);
    }

    public static int getWidth() {
        return mainFrame.getWidth();
    }

    public static int getHeight() {
        return mainFrame.getHeight();
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static JPanel getMenuWrapperPanel() {
        return menuWrapperPanel;
    }
}
