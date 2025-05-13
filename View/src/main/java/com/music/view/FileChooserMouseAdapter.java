package com.music.view;

import com.music.controller.IController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.prefs.Preferences;

public class FileChooserMouseAdapter extends MouseAdapter {

    private final Frame frame;
    private final IController controller;
    private File lastDirectory;

    public FileChooserMouseAdapter(Frame frame, IController controller) {
        this.frame = frame;
        this.controller = controller;
        loadLastDirectory();
    }

    private void loadLastDirectory() {
        Preferences prefs = Preferences.userNodeForPackage(Header.class);
        String path = prefs.get("lastDirectory", null);
        if (path != null) {
            lastDirectory = new File(path);
        }
    }

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

                ReaderJson readerJson = new ReaderJson(controller);
                if (selectedFile.getName().endsWith(".json")) {
                    readerJson.setFilePath(selectedFile.getAbsolutePath());
                }

                if (selectedFile.getName().endsWith(".txt")) {
                    ConverterTxtToJson converter = new ConverterTxtToJson();
                    converter.setFilePath(selectedFile.getAbsolutePath());
                    readerJson.setJsonString(converter.convertFileToJsonString());
                }

                SwingUtilities.invokeLater(() -> {
                    OuvrirPartition partitionPanel = new OuvrirPartition(selectedFile.getName(), controller, readerJson);
                    frame.updateFrameContent(partitionPanel);
                });
            } else {
                showErrorDialog("Erreur: Veuillez sélectionner un fichier .json ou .txt");
                SwingUtilities.invokeLater(() -> {
                    Menu menu = new Menu();
                    frame.updateFrameContent(menu);
                });
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            SwingUtilities.invokeLater(() -> {
                Menu menu = new Menu();
                frame.updateFrameContent(menu);
            });
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

    private boolean isJsonOrTxtFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".json") || fileName.endsWith(".txt");
    }

    private void saveLastDirectory() {
        Preferences prefs = Preferences.userNodeForPackage(Header.class);
        if (lastDirectory != null) {
            prefs.put("lastDirectory", lastDirectory.getAbsolutePath());
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(null, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
