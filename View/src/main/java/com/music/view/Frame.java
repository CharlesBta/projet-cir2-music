package com.music.view;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class Frame {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    public Frame() {
        initializeUI();
    }

    private void initializeUI() {
        setLookAndFeel();
        createAndShowFrame();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private void createAndShowFrame() {
        JFrame frame = new JFrame("Piano Virtuel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(Color.WHITE);

        Header header = new Header();
        frame.add(header.getHeaderPanel(), BorderLayout.WEST);

        frame.setVisible(true);
    }

}
