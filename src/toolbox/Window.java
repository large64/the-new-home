package toolbox;

import org.lwjgl.opengl.Display;
import renderEngine.MasterRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.ObjectView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private static JFrame mainFrame;
    private static JPanel menuWrapperPanel;

    private Map<String, Object> elements;

    public Window(Canvas canvas) {
        elements = new HashMap<>();
        initElements();

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuWrapperPanel = new JPanel();
        menuWrapperPanel.setBackground((Color) elements.get("darkGreen"));
        JPanel menuPanel = (JPanel) elements.get("menuPanel");
        menuPanel.setOpaque(false);

        JPanel indicatorPanel = (JPanel) elements.get("indicatorPanel");
        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));

        menuPanel.add((JButton) elements.get("button1"));
        menuPanel.add((JButton) elements.get("button2"));

        Indicator btnIndicator = (Indicator) elements.get("btnIndicator");
        indicatorPanel.add(btnIndicator.getTextPane());

        menuWrapperPanel.add(menuPanel, BorderLayout.NORTH);

        mainFrame.add(menuWrapperPanel, BorderLayout.WEST);
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setVisible(true);

        menuPanel.setBorder((Border) elements.get("menuPanelBorder"));
    }

    private void initElements() {
        Color menuBgColor = new Color(2, 120, 0);
        elements.put("darkGreen", menuBgColor);

        Indicator btnIndicator = new Indicator("text");
        elements.put("btnIndicator", btnIndicator);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 10, 20, 10);
        elements.put("menuPanelBorder", menuPanelBorder);

        JButton button = new JButton("New Game");
        JButton button1 = new JButton("Quit");
        button.addActionListener(e -> {
            MasterRenderer.restart();
        });
        elements.put("button1", button);
        elements.put("button2", button1);

        JPanel menuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new GridLayout(1,1));
        elements.put("menuPanel", menuPanel);
        elements.put("indicatorPanel", indicatorPanel);
    }

    public static int getWidth() {
        return mainFrame.getWidth();
    }

    public static int getHeight() {
        return mainFrame.getHeight();
    }

    public static JPanel getMenuWrapperPanel() {
        return menuWrapperPanel;
    }
}
