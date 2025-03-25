package com.music.view;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class PianoKeyPanel extends JLayeredPane {
    private int octave = 0;

    private static final int[] whiteKeyPositions = {0, 60, 120, 180, 240, 300, 360};
    private static final int[] blackKeyPositions = {40, 100, -1, 220, 280, 340, -1};
    private static final String[] whiteKeyNotes = {"C", "D", "E", "F", "G", "A", "B"};
    private static final String[] blackKeyNotes = {"C#", "D#", null, "F#", "G#", "A#", null};

    private HashMap<String, JButton> noteToButton = new HashMap<>();
    private IController controller;

    public PianoKeyPanel(IController controller, int octave) {
        this.controller = controller;
        setLayout(null);
        setPreferredSize(new Dimension(420, 200));
        initializeKeys();

        this.octave = octave;
    }

    private void initializeKeys() {
        // Ajouter les touches blanches en premier
        for (int i = 0; i < whiteKeyPositions.length; i++) {
            JButton whiteKey = createKey(whiteKeyNotes[i], Color.WHITE, whiteKeyPositions[i], 60, 200);
            add(whiteKey, JLayeredPane.DEFAULT_LAYER);
        }

        // Ajouter les touches noires ensuite et les mettre au premier plan
        for (int i = 0; i < blackKeyPositions.length; i++) {
            if (blackKeyPositions[i] != -1) {
                JButton blackKey = createKey(blackKeyNotes[i], Color.BLACK, blackKeyPositions[i], 40, 120);
                add(blackKey, JLayeredPane.PALETTE_LAYER);
            }
        }

        this.revalidate();
        this.repaint();
    }

    private JButton createKey(String note, Color color, int x, int width, int height) {
        JButton key = new JButton();
        key.setBackground(color);
        key.setForeground(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        key.setFocusPainted(false);
        key.setBounds(x, 0, width, height);
        key.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        key.setOpaque(true); // Assurez-vous que le bouton est opaque

        noteToButton.put(note, key);

        key.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(key, note);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(key, note);
            }
        });

        return key;
    }

    private void handleMousePress(JButton key, String note) {
        key.setBackground(Color.GRAY);
        if (controller != null && note != null) {
            controller.playNote(this.octave, note); // Ajuster si nécessaire
        }
    }

    private void handleMouseRelease(JButton key, String note) {
        key.setBackground(key.getForeground() == Color.BLACK ? Color.WHITE : Color.BLACK);
        if (controller != null && note != null) {
            controller.stopNote(this.octave, note); // Ajuster si nécessaire
        }
    }
}