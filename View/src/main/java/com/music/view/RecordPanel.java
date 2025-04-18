package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecordPanel extends JLayeredPane {
    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;
    private static final int IMAGE_BUTTON_WIDTH = 50;
    private static final int IMAGE_BUTTON_HEIGHT = 50;
    private static final Color BUTTON_COLOR = new Color(255, 255, 255);
    private static final Color HOVER_COLOR = new Color(255, 255, 255);

    public RecordPanel() {
        setLayout(new BorderLayout());

        JButton recordButton = createStyledButtonWithIcon("view/src/main/resources/record.png", ICON_WIDTH, ICON_HEIGHT, "Record");
        JButton stopButton = createStyledButtonWithIcon("view/src/main/resources/stop.png", ICON_WIDTH, ICON_HEIGHT, "Stop");

        recordButton.addActionListener(
                e -> {
                    System.out.println("record");
                }
        );

        stopButton.addActionListener(
                e -> {
                    System.out.println("stop");
                }
        );

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
        controlPanel.add(recordButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add some space between buttons
        controlPanel.add(stopButton);

        // Add padding to the right to shift buttons to the left
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        controlPanel.setBackground(Color.WHITE);

        add(controlPanel, BorderLayout.WEST);
    }

    private JButton createStyledButtonWithIcon(String path, int width, int height, String tooltip) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton button = new JButton(scaledIcon);

        button.setOpaque(true);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setPreferredSize(new Dimension(IMAGE_BUTTON_WIDTH, IMAGE_BUTTON_HEIGHT));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }
}
