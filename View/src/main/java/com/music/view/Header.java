package com.music.view;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class Header {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("Instrument Selector");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton headerButton = new JButton("Instruments");
        styleAsButton(headerButton);
        headerPanel.add(headerButton);

        JPopupMenu popupMenu = new JPopupMenu();
        String[] instruments = {"Piano", "Xylophone", "Guitare"};
        for (String instrument : instruments) {
            JMenuItem item = new JMenuItem(instrument);
            popupMenu.add(item);
        }

        headerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(headerButton, 0, headerButton.getHeight());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                headerButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                headerButton.setBackground(Color.LIGHT_GRAY);
            }
        });

        JButton openButton = new JButton("Ouvrir");
        styleAsButton(openButton);
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                // Create a file filter for .json and .txt files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON & Text Files", "json", "txt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    if (selectedFile.getName().endsWith(".json") || selectedFile.getName().endsWith(".txt")) {
                        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    } else {
                        JOptionPane.showMessageDialog(frame, "Erreur: Veuillez sélectionner un fichier .json ou .txt", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                openButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                openButton.setBackground(Color.LIGHT_GRAY);
            }
        });
        headerPanel.add(openButton);

        JButton menuButton = new JButton("Menu");
        styleAsButton(menuButton);
        menuButton.addActionListener(e -> System.out.println("menu"));
        headerPanel.add(menuButton);

        JButton quitButton = new JButton("Quit");
        styleAsButton(quitButton);
        quitButton.addActionListener(e -> System.exit(0));
        headerPanel.add(quitButton);

        frame.add(headerPanel, BorderLayout.WEST);

        frame.setVisible(true);
        frame.requestFocus();
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
}
