package com.music.view;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.prefs.Preferences;

public class Header {

    private JPanel headerPanel;
    private Frame frame;
    private File lastDirectory;
    private static final Preferences prefs = Preferences.userNodeForPackage(Header.class);

    public Header(Frame frame) {
        this.frame = frame;
        initializeUI();
    }

    private void initializeUI() {
        setLookAndFeel();
        createHeaderPanel();
        loadLastDirectory();
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(Color.WHITE);

        JButton headerButton = createStyledButton("Instruments");
        headerPanel.add(headerButton);
        addPopupMenuToButton(headerButton, new String[]{"Piano", "Xylophone", "Guitare"});

        JButton openButton = createStyledButton("Ouvrir");
        openButton.addMouseListener(new FileChooserMouseAdapter());
        headerPanel.add(openButton);

        JButton menuButton = createStyledButton("Menu");
        menuButton.addActionListener(e -> System.out.println("menu"));
        headerPanel.add(menuButton);

        JButton quitButton = createStyledButton("Quit");
        quitButton.addActionListener(e -> {
            saveLastDirectory();
            System.exit(0);
        });
        headerPanel.add(quitButton);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        styleAsButton(button);
        return button;
    }

    private void addPopupMenuToButton(JButton button, String[] items) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (String item : items) {
            popupMenu.add(new JMenuItem(item));
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(button, 0, button.getHeight());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
    }

    private class FileChooserMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            if (lastDirectory != null) {
                fileChooser.setCurrentDirectory(lastDirectory);
            }
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON & Text Files", "json", "txt"));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (isJsonOrTxtFile(selectedFile)) {
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    lastDirectory = selectedFile.getParentFile();
                    saveLastDirectory();
                    SwingUtilities.invokeLater(() -> {
                        OuvrirPartition partitionPanel = new OuvrirPartition();
                        frame.updateFrameContent(partitionPanel);
                    });
                } else {
                    showErrorDialog("Erreur: Veuillez sélectionner un fichier .json ou .txt");
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(Color.GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            ((JButton) e.getSource()).setBackground(Color.LIGHT_GRAY);
        }
    }

    private boolean isJsonOrTxtFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".json") || fileName.endsWith(".txt");
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
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

    private void loadLastDirectory() {
        String path = prefs.get("lastDirectory", null);
        if (path != null) {
            lastDirectory = new File(path);
        }
    }

    private void saveLastDirectory() {
        if (lastDirectory != null) {
            prefs.put("lastDirectory", lastDirectory.getAbsolutePath());
        }
    }

    public JPanel getHeaderPanel() {
        return headerPanel;
    }
}
