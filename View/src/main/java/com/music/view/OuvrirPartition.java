package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OuvrirPartition extends JFrame {

    private final JLabel fileNameLabel;
    private final JButton instrumentSelector;

    public OuvrirPartition() {

        setTitle("Ouvrir Partition");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel instrumentLabel = new JLabel("Instrument:");
        JLabel nameLabel = new JLabel("Name:");
        instrumentLabel.setFont(new Font("Arial", Font.BOLD, 40));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 40));

        instrumentSelector = new JButton("Instruments");
        styleAsButton(instrumentSelector);

        JPopupMenu popupMenu = new JPopupMenu();
        String[] instruments = {"Piano", "Xylophone", "Game Music"};
        for (String instrument : instruments) {
            JMenuItem item = new JMenuItem(instrument);
            item.addActionListener(new InstrumentSelectionListener(instrument));
            popupMenu.add(item);
        }

        instrumentSelector.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(instrumentSelector, 0, instrumentSelector.getHeight());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                instrumentSelector.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                instrumentSelector.setBackground(Color.LIGHT_GRAY);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(instrumentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(instrumentSelector, gbc);

        fileNameLabel = new JLabel("Aucun fichier ouvert", SwingConstants.CENTER);
        fileNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(fileNameLabel, gbc);

        JButton playButton = createStyledButtonWithIcon("view/src/images/play.png", 50, 50, "Play");
        JButton pauseButton = createStyledButtonWithIcon("view/src/images/pause.png", 50, 50, "Pause");
        JButton replayButton = createStyledButtonWithIcon("view/src/images/replay.png", 50, 50, "Replay");

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

    private static void styleAsButton(JComponent component) {
        component.setOpaque(true);
        component.setBackground(Color.LIGHT_GRAY);
        component.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        component.setPreferredSize(new Dimension(130, 30));
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private JButton createStyledButtonWithIcon(String path, int width, int height, String tooltip) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton button = new JButton(scaledIcon);

        button.setOpaque(true);
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.setPreferredSize(new Dimension(100, 80));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setToolTipText(tooltip);

        return button;
    }

    private class InstrumentSelectionListener implements ActionListener {
        private final String instrument;

        public InstrumentSelectionListener(String instrument) {
            this.instrument = instrument;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            instrumentSelector.setText(instrument);
        }
    }
}
