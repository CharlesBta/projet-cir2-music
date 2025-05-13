package com.music.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static final int WIDTH = 1600;
    private static final int HEIGHT = 900;
    private final IController controller;
    private JLayeredPane layeredPane;

    public Frame(IController controller) {
        this.controller = controller;
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

        Header header = new Header(this, controller);
        add(header.getHeaderPanel(), BorderLayout.NORTH);

        layeredPane = new JLayeredPane();
        layeredPane.setLayout(new GridBagLayout());
        add(layeredPane, BorderLayout.CENTER);

        Menu menu = new Menu();
        layeredPane.add(menu, new GridBagConstraints());

        setVisible(true);
    }

    public void updateFrameContent(JLayeredPane newContent) {
        // Stop any music playback in OuvrirPartition instances
        Component[] components = layeredPane.getComponents();
        for (Component component : components) {
            if (component instanceof OuvrirPartition) {
                ((OuvrirPartition) component).stopPlayback();
            }
        }

        layeredPane.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        layeredPane.setBackground(Color.WHITE);

        layeredPane.add(newContent, gbc);
        layeredPane.revalidate();
        layeredPane.repaint();

        // Remove any existing RecordPanel
        Component[] contentComponents = getContentPane().getComponents();
        for (Component component : contentComponents) {
            if (component instanceof RecordPanel) {
                remove(component);
            }
        }

        // Only add RecordPanel if the content is an instrument panel
        boolean isInstrumentPanel = newContent instanceof com.music.view.piano.PianoPanel ||
                newContent instanceof com.music.view.xylophone.XylophonePanel ||
                newContent instanceof com.music.view.videogame.BitPanel ||
                newContent instanceof com.music.view.wood.WoodPanel ||
                newContent instanceof com.music.view.drumkit.DrumPanel;

        if (isInstrumentPanel) {
            RecordPanel recordPanel = new RecordPanel(controller);
            add(recordPanel, BorderLayout.EAST);
        }

        revalidate();
        repaint();
    }
}
