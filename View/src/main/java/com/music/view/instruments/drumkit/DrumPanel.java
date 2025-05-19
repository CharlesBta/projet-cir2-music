package com.music.view.instruments.drumkit;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DrumPanel extends JLayeredPane implements KeyListener, FocusListener {
    private final IController controller;
    private final Map<JButton, DrumSound> drumButtons;
    private final Map<Character, JButton> keyToButton;
    private final HashSet<Character> pressedKeys = new HashSet<>();
    private KeyEventDispatcher keyEventDispatcher;

    // Class to hold drum sound information
    private static class DrumSound {
        final int octave;
        final String note;
        final String name;

        DrumSound(int octave, String note, String name) {
            this.octave = octave;
            this.note = note;
            this.name = name;
        }
    }

    public DrumPanel(IController controller) {
        this.controller = controller;
        this.drumButtons = new HashMap<>();
        this.keyToButton = new HashMap<>();
    }

    public void init() {
        // Create a main panel with GridLayout for the drum buttons
        JPanel mainPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize the drum buttons on the main panel
        initializeDrumButtons(mainPanel);

        // Add the main panel to this layered pane
        mainPanel.setBounds(0, 0, 500, 400);
        setPreferredSize(new Dimension(500, 400));
        add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        // Set up key listener
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);

        // Use a KeyEventDispatcher to capture keyboard events globally
        keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                synchronized (DrumPanel.this) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        keyPressed(e);
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        keyReleased(e);
                    }
                }
                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // Request focus when the component is added to the hierarchy
    }

    private void initializeDrumButtons(JPanel panel) {
        // Create drum buttons with corresponding octave and note
        addDrumButton(panel, "Kick", -2, "C", 'q');           // Bass Drum 1 (MIDI 36)
        addDrumButton(panel, "Snare", -2, "D", 's');          // Acoustic Snare (MIDI 38)
        addDrumButton(panel, "Closed Hi-Hat", -2, "F#", 'd'); // Closed Hi-Hat (MIDI 42)
        addDrumButton(panel, "Open Hi-Hat", -2, "A#", 'f');   // Open Hi-Hat (MIDI 46)
        addDrumButton(panel, "Crash", -1, "C#", 'g');         // Crash Cymbal 1 (MIDI 49)
        addDrumButton(panel, "Ride", -1, "D#", 'h');          // Ride Cymbal 1 (MIDI 51)
    }

    private void addDrumButton(JPanel panel, String name, int octave, String note, char keyChar) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(200, 200, 200));
        button.setFocusPainted(false);

        DrumSound drumSound = new DrumSound(octave, note, name);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Play the drum sound when the button is clicked
                handleDrumButtonPress(button, drumSound);
                requestFocusInWindow();
            }
        });

        drumButtons.put(button, drumSound);
        keyToButton.put(keyChar, button);
        panel.add(button);
    }

    private void handleDrumButtonPress(JButton button, DrumSound drumSound) {
        // Play the drum sound
        controller.playNote(drumSound.octave, drumSound.note);

        // Visual feedback
        button.setBackground(new Color(150, 150, 150));
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.setBackground(new Color(200, 200, 200));
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void handleDrumButtonRelease(JButton button) {
        // No need to do anything here as the timer in handleDrumButtonPress
        // will reset the button color after a short delay
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (controller.isSaving()) {
            return; // Ignore key events if saving
        }

        char keyChar = e.getKeyChar();
        // Only process the key if it's not already in the pressedKeys set
        if (!pressedKeys.contains(keyChar)) {
            JButton button = keyToButton.get(keyChar);
            if (button != null) {
                // Add the key to the set to prevent repeated triggering
                pressedKeys.add(keyChar);
                // Get the drum sound and play it
                DrumSound drumSound = drumButtons.get(button);
                if (drumSound != null) {
                    // Use the common method to handle drum button press
                    handleDrumButtonPress(button, drumSound);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char keyChar = e.getKeyChar();
        pressedKeys.remove(keyChar);

        // Get the button associated with this key
        JButton button = keyToButton.get(keyChar);
        if (button != null) {
            // Use the common method to handle drum button release
            handleDrumButtonRelease(button);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    /**
     * Cleanup resources when the panel is no longer in use
     */
    public void cleanup() {
        if (keyEventDispatcher != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
            keyEventDispatcher = null;
        }
    }
}
