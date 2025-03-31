package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OuvrirPartition extends JFrame {

    private JComboBox<String> instrumentSelector;
    private JLabel fileNameLabel;
    private JButton playButton;
    private JButton pauseButton;
    private JButton replayButton;

    public OuvrirPartition() {
        // Initialisation de la fenêtre
        setTitle("Ouvrir Partition");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centre la fenêtre
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Création des étiquettes
        JLabel instrumentLabel = new JLabel("Instrument:");
        JLabel nameLabel = new JLabel("Name:");
        instrumentLabel.setFont(new Font("Arial", Font.BOLD, 40));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 40));

        // Création du sélecteur d'instruments
        String[] instruments = {"Piano", "Guitare", "Violon", "Flûte"};
        instrumentSelector = new JComboBox<>(instruments);
        instrumentSelector.addActionListener(new InstrumentSelectionListener());
        instrumentSelector.setFont(new Font("Arial", Font.PLAIN, 20));

        // Configuration du sélecteur d'instruments et de son étiquette
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(instrumentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(instrumentSelector, gbc);

        // Création du label pour afficher le nom du fichier
        fileNameLabel = new JLabel("Aucun fichier ouvert", SwingConstants.CENTER);
        fileNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        // Configuration du label du fichier et de son étiquette
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(fileNameLabel, gbc);

        // Chargement des images pour les boutons
        ImageIcon playIcon = new ImageIcon("path/to/play.png");
        ImageIcon pauseIcon = new ImageIcon("path/to/pause.png");
        ImageIcon replayIcon = new ImageIcon("path/to/replay.png");

        // Création des boutons de contrôle avec des icônes
        playButton = new JButton(playIcon);
        pauseButton = new JButton(pauseIcon);
        replayButton = new JButton(replayIcon);

        // Configuration des boutons
        JPanel controlPanel = new JPanel();
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(replayButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(controlPanel, gbc);
    }

    private class InstrumentSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedInstrument = (String) instrumentSelector.getSelectedItem();
            JOptionPane.showMessageDialog(OuvrirPartition.this, "Vous avez sélectionné : " + selectedInstrument);
            // Ajoutez ici le code pour ouvrir un fichier ou effectuer une action spécifique
            fileNameLabel.setText("Fichier ouvert : " + selectedInstrument + ".mid");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OuvrirPartition frame = new OuvrirPartition();
            frame.setVisible(true);
        });
    }
}
