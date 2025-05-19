package com.music.view.instruments.piano;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

public class PianoKeyPanel extends JLayeredPane {

    private int octave = 0;
    private static final Color ACTIVE_WHITE = new Color(255, 200, 200); // Légèrement rosé
    private static final Color ACTIVE_BLACK = new Color(100, 0, 0); // Rouge foncé
    private static final Color PRESSED_COLOR = Color.GRAY; // Couleur lorsque la touche est pressée

    private static final int[] whiteKeyPositions = {0, 60, 120, 180, 240, 300, 360};
    private static final int[] blackKeyPositions = {40, 100, -1, 220, 280, 340, -1};
    private static final String[] whiteKeyNotes = {"C", "D", "E", "F", "G", "A", "B"};
    private static final String[] blackKeyNotes = {"C#", "D#", null, "F#", "G#", "A#", null};

    private HashMap<String, JButton> noteToButton = new HashMap<>();
    private IController controller;
    private boolean isActive = false;
    private PianoPanel parentPanel;
    private HashSet<Character> pressedKeys = new HashSet<>();

    public PianoKeyPanel(IController controller, int octave, PianoPanel parentPanel) {
        this.controller = controller;
        this.parentPanel = parentPanel;
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
        key.setBackground(PRESSED_COLOR);
        if (controller != null && note != null) {
            controller.playNote(this.octave, note); // Ajuster si nécessaire
            // Update the note label in the parent panel
            if (parentPanel != null) {
                parentPanel.updateNoteLabel(note, octave);
            }
        }
    }

    private void handleMouseRelease(JButton key, String note) {
        if (isActive) {
            key.setBackground(key.getForeground() == Color.BLACK ? ACTIVE_WHITE : ACTIVE_BLACK);
        } else {
            key.setBackground(key.getForeground() == Color.BLACK ? Color.WHITE : Color.BLACK);
        }
        if (controller != null && note != null) {
            controller.stopNote(this.octave, note); // Ajuster si nécessaire
            // Clear the note label in the parent panel
            if (parentPanel != null && pressedKeys.isEmpty()) {
                parentPanel.updateNoteLabel("", -1);
            }
        }
    }

    public void handleKeyPress(KeyEvent e) {
        if (!controller.isSaving()) {
            char keyChar = e.getKeyChar();

            // Only process the key if it's not already pressed
            if (!pressedKeys.contains(keyChar)) {
                String note = getNoteFromKeyChar(Character.toLowerCase(keyChar));
                if (note != null) {
                    JButton keyButton = noteToButton.get(note);
                    if (keyButton != null) {
                        pressedKeys.add(keyChar); // Add to pressed keys set

                        // Change the visual appearance of the key
                        keyButton.setBackground(PRESSED_COLOR);

                        // Play the note using the controller with this panel's octave
                        if (controller != null) {
                            controller.playNote(this.octave, note);
                        }

                        // Always update the parent panel's note display with the most recently pressed key
                        if (parentPanel != null) {
                            parentPanel.updateNoteLabel(note, octave);
                        }
                    }
                }
            }
        }
    }

    public void handleKeyRelease(KeyEvent e) {
        char keyChar = e.getKeyChar();
        pressedKeys.remove(keyChar); // Remove from pressed keys set

        String note = getNoteFromKeyChar(Character.toLowerCase(keyChar));
        if (note != null) {
            JButton keyButton = noteToButton.get(note);
            if (keyButton != null) {
                // Restore the key's appearance
                if (isActive) {
                    keyButton.setBackground(keyButton.getForeground() == Color.BLACK ? ACTIVE_WHITE : ACTIVE_BLACK);
                } else {
                    keyButton.setBackground(keyButton.getForeground() == Color.BLACK ? Color.WHITE : Color.BLACK);
                }

                // Stop the note using the controller with this panel's octave
                if (controller != null) {
                    controller.stopNote(this.octave, note);
                }

                // Clear the note label in the parent panel if no keys are pressed
                if (parentPanel != null && pressedKeys.isEmpty()) {
                    parentPanel.updateNoteLabel("", -1);
                }
            }
        }
    }

    private String getNoteFromKeyChar(final char keyChar) {
        switch (keyChar) {
            case 'q':
                return "C";
            case 'z':
                return "C#";
            case 's':
                return "D";
            case 'e':
                return "D#";
            case 'd':
                return "E";
            case 'f':
                return "F";
            case 't':
                return "F#";
            case 'g':
                return "G";
            case 'y':
                return "G#";
            case 'h':
                return "A";
            case 'u':
                return "A#";
            case 'j':
                return "B";
            default:
                return null;
        }
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        for (JButton key : noteToButton.values()) {
            if (key.getBackground() == Color.WHITE || key.getBackground() == ACTIVE_WHITE) {
                key.setBackground(isActive ? ACTIVE_WHITE : Color.WHITE);
            } else if (key.getBackground() == Color.BLACK || key.getBackground() == ACTIVE_BLACK) {
                key.setBackground(isActive ? ACTIVE_BLACK : Color.BLACK);
            }
        }
    }
}
