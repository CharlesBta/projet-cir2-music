package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecordPanel extends JLayeredPane {
    private static final int ICON_WIDTH = 40;
    private static final int ICON_HEIGHT = 40;
    private static final int IMAGE_BUTTON_WIDTH = 120;
    private static final int IMAGE_BUTTON_HEIGHT = 90;
    private static final Color BUTTON_COLOR = new Color(200, 200, 200);
    private static final Color HOVER_COLOR = new Color(180, 180, 180);



    public RecordPanel() {
        JButton recordButton = createStyledButtonWithIcon("view/src/main/resources/record.png", ICON_WIDTH, ICON_HEIGHT, "Record");

        recordButton.addActionListener(
                e -> {
                    System.out.println("record");
                }
        );

        JPanel controlPanel = new JPanel();
        controlPanel.add(recordButton);
    }

    private JButton createStyledButtonWithIcon(String path, int width, int height, String tooltip) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton button = new JButton(scaledIcon);

        button.setOpaque(true);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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