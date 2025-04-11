package com.music.view;

import javax.swing.*;
import java.awt.*;

public class Menu extends JLayeredPane {
    private int width = 650;
    private int height = 400;
    public Menu() {
        ImageIcon originalIcon = new ImageIcon(Menu.class.getClassLoader().getResource("image_menu.jpg"));
        Image originalImage = originalIcon.getImage();

        Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel menuLabel = new JLabel(resizedIcon);
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());
        this.add(menuLabel, BorderLayout.CENTER);
    }
}