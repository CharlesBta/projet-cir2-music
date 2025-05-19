package com.music.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.music.controller.IController;
import com.music.view.instruments.drumkit.DrumPanel;
import com.music.view.instruments.piano.PianoPanel;
import com.music.view.instruments.videogame.BitPanel;
import com.music.view.instruments.wood.WoodPanel;
import com.music.view.instruments.xylophone.XylophonePanel;
import com.music.view.reader.OuvrirPartition;
import com.music.view.record.RecordPanel;

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
        setTitle("Music Lab");
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
        // Stop any music playback in OuvrirPartition instances and cleanup resources
        Component[] components = layeredPane.getComponents();
        for (Component component : components) {
            if (component instanceof OuvrirPartition) {
                ((OuvrirPartition) component).stopPlayback();
            }
            // Cleanup instrument panel resources
            if (component instanceof DrumPanel) {
                ((DrumPanel) component).cleanup();
            }
            if (component instanceof PianoPanel) {
                ((PianoPanel) component).cleanup();
            }
            if (component instanceof BitPanel) {
                ((BitPanel) component).cleanup();
            }
            if (component instanceof WoodPanel) {
                ((WoodPanel) component).cleanup();
            }
            if (component instanceof XylophonePanel) {
                ((XylophonePanel) component).cleanup();
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
        boolean isInstrumentPanel = newContent instanceof PianoPanel ||
                newContent instanceof XylophonePanel ||
                newContent instanceof BitPanel ||
                newContent instanceof WoodPanel ||
                newContent instanceof DrumPanel;

        if (isInstrumentPanel) {
            RecordPanel recordPanel = new RecordPanel(controller);
            add(recordPanel, BorderLayout.EAST);
        }

        revalidate();
        repaint();
    }
}
