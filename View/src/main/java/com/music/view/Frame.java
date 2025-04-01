package com.music.view;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;

public class Frame extends JFrame {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private Header header;

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
        setTitle("Piano Virtuel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        header = new Header(this);
        add(header.getHeaderPanel(), BorderLayout.WEST);

        setVisible(true);
    }

    public void updateFrameContent(JPanel newContent) {
        getContentPane().removeAll();
        add(header.getHeaderPanel(), BorderLayout.WEST);
        add(newContent, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
