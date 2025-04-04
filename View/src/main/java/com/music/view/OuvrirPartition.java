package com.music.view;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OuvrirPartition extends JPanel {

    private static final int INSETS = 20;
    private static final int LABEL_FONT_SIZE = 30;
    private static final int BUTTON_FONT_SIZE = 24;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    private static final int ICON_WIDTH = 60;
    private static final int ICON_HEIGHT = 60;
    private static final int IMAGE_BUTTON_WIDTH = 120;
    private static final int IMAGE_BUTTON_HEIGHT = 90;
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_COLOR = new Color(200, 200, 200);
    private static final Color HOVER_COLOR = new Color(180, 180, 180);

    private final JLabel fileNameLabel;
    private final JButton instrumentSelector;
    private final IController controller;
    private ReaderJson reader;

    public OuvrirPartition(String fileName, final IController controller, ReaderJson reader) {
        this.controller = controller;
        this.reader = reader;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(INSETS, INSETS, INSETS, INSETS);

        JLabel instrumentLabel = new JLabel("Instrument:");
        JLabel nameLabel = new JLabel("Name:");
        instrumentLabel.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));
        nameLabel.setFont(new Font("Arial", Font.BOLD, LABEL_FONT_SIZE));

        instrumentSelector = new JButton("Instruments");
        styleAsButton(instrumentSelector);

        JPopupMenu popupMenu = new JPopupMenu();
        String[] instruments = {"Piano", "Xylophone", "Video Game"};
        for (String instrument : instruments) {
            JMenuItem item = new JMenuItem(instrument);
            item.addActionListener(e -> setInstrument(instrument));
            popupMenu.add(item);
        }

        instrumentSelector.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(instrumentSelector, 0, instrumentSelector.getHeight());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                instrumentSelector.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                instrumentSelector.setBackground(BUTTON_COLOR);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(instrumentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(instrumentSelector, gbc);

        fileNameLabel = new JLabel(fileName, SwingConstants.CENTER);
        fileNameLabel.setFont(new Font("Arial", Font.PLAIN, BUTTON_FONT_SIZE));

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(fileNameLabel, gbc);

        JButton playButton = createStyledButtonWithIcon("view/src/images/play.png", ICON_WIDTH, ICON_HEIGHT, "Play");

        playButton.addActionListener(
                e -> {
                    System.out.println("Play button clicked");
                    reader.play();
                }
        );

        JPanel controlPanel = new JPanel();
        controlPanel.add(playButton);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(controlPanel, gbc);

        setBackground(BACKGROUND_COLOR);
    }

    public void setInstrument(String instrument) {
        instrumentSelector.setText(instrument);
        System.out.println("Instrument selected: " + instrument);
        controller.setInstrument(instrument);
    }

    private static void styleAsButton(JComponent component) {
        component.setOpaque(true);
        component.setBackground(BUTTON_COLOR);
        component.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        component.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).setHorizontalAlignment(SwingConstants.CENTER);
            ((AbstractButton) component).setFont(new Font("Arial", Font.PLAIN, BUTTON_FONT_SIZE));
        }
    }

    private JButton createStyledButtonWithIcon(String path, int width, int height, String tooltip) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton button = new JButton(scaledIcon);

        button.setOpaque(true);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.setPreferredSize(new Dimension(IMAGE_BUTTON_WIDTH, IMAGE_BUTTON_HEIGHT));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setToolTipText(tooltip);
        button.setFont(new Font("Arial", Font.PLAIN, BUTTON_FONT_SIZE));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }
}
