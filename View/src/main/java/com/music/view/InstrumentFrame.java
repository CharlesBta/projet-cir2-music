package com.music.view;

import com.music.controller.IController;
import com.music.view.piano.PianoPanel;
import com.music.view.videogame.BitPanel;
import com.music.view.xylophone.XylophonePanel;

import javax.swing.*;
import java.awt.*;

public class InstrumentFrame extends JFrame {
    private IController controller;
    private JPanel currentInstrumentPanel;

    public InstrumentFrame(IController controller) {
        this.controller = controller;
        setTitle("Instruments Virtuels");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Ajouter un sélecteur d'instrument
        JComboBox<String> instrumentSelector = new JComboBox<>(new String[]{"Piano", "Xylophone", "Bit"});
        instrumentSelector.addActionListener(e -> switchInstrument((String) instrumentSelector.getSelectedItem()));
        add(instrumentSelector, BorderLayout.NORTH);

        // Initialiser avec le piano par défaut
        switchInstrument("Piano");

        setVisible(true);
    }

    private void switchInstrument(String instrument) {
        // Supprimer l'ancien panneau d'instrument s'il existe
        if (currentInstrumentPanel != null) {
            remove(currentInstrumentPanel);
        }

        // Créer un nouveau panneau d'instrument en fonction de la sélection
        switch (instrument) {
            case "Piano":
                currentInstrumentPanel = new PianoPanel(controller);
                ((PianoPanel) currentInstrumentPanel).init();
                controller.setInstrument("Piano");
                break;
            case "Xylophone":
                currentInstrumentPanel = new XylophonePanel(controller);
                ((XylophonePanel) currentInstrumentPanel).init();
                controller.setInstrument("Xylophone");
                break;
            case "Bit":
                currentInstrumentPanel = new BitPanel(controller);
                ((BitPanel) currentInstrumentPanel).init();
                controller.setInstrument("Video Game");
                break;
        }

        // Ajouter le nouveau panneau et rafraîchir l'interface
        add(currentInstrumentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}