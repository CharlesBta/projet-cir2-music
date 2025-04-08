package com.music.view;

import javax.swing.*;
import java.awt.*;


public class Menu extends JLayeredPane {
    public Menu() {
        // Charger l'image
        ImageIcon menu = new ImageIcon(Menu.class.getClassLoader().getResource("image_menu.jpg"));
        JLabel menuLabel = new JLabel(menu);
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Ajouter le JLabel au layeredPane
        this.add(menuLabel);
        this.setLayout(new BorderLayout());
    }
}