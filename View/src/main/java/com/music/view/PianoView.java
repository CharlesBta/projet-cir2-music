package com.music.view;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PianoView extends JFrame {
    private IController controller;
    private JPanel mainPanel;
    private JPanel pianoContainer;
    private int numberOfPanels;

    public PianoView(IController controller) {
        this.controller = controller;
        this.numberOfPanels = 3; // Initialiser avec 3 octaves
        setTitle("Piano Virtuel");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout()); // Utiliser GridBagLayout
        add(mainPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        controlPanel.add(addButton);
        controlPanel.add(removeButton);
        add(controlPanel, BorderLayout.NORTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPianoKeyPanel();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePianoKeyPanel();
            }
        });

        pianoContainer = new JPanel();
        pianoContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Utiliser FlowLayout sans écart

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        mainPanel.add(pianoContainer, gbc);

        addPianoKeyPanels(numberOfPanels);

        setVisible(true);
    }

    private void addPianoKeyPanels(int numberOfPanels) {
        for (int i = 0; i < numberOfPanels; i++) {
            PianoKeyPanel pianoKeyPanel = new PianoKeyPanel(controller, i);
            pianoContainer.add(pianoKeyPanel);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addPianoKeyPanel() {
        if (numberOfPanels < 3) { // Limiter à 3 panneaux
            PianoKeyPanel pianoKeyPanel = new PianoKeyPanel(controller, numberOfPanels);
            pianoContainer.add(pianoKeyPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
            numberOfPanels++;
        }
    }

    private void removePianoKeyPanel() {
        if (numberOfPanels > 0) {
            if (pianoContainer.getComponentCount() > 0) {
                pianoContainer.remove(pianoContainer.getComponentCount() - 1);
                mainPanel.revalidate();
                mainPanel.repaint();
                numberOfPanels--;
            }
        }
    }
}