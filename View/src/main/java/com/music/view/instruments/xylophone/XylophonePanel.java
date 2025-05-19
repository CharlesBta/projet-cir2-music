package com.music.view.instruments.xylophone;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

public class XylophonePanel extends JLayeredPane implements KeyListener, FocusListener {

    private static final Color[] BAR_COLORS = {
            new Color(255, 0, 0),   // C
            new Color(255, 128, 0), // D
            new Color(255, 255, 0), // E
            new Color(0, 255, 0),   // F
            new Color(0, 255, 255), // G
            new Color(0, 0, 255),   // A
            new Color(128, 0, 255)  // B
    };

    private static final String[] NOTES = {"C", "D", "E", "F", "G", "A", "B"};
    private static final char[] KEY_MAPPINGS = {'q', 's', 'd', 'f', 'g', 'h', 'j'};

    private HashMap<String, JButton> noteToButton = new HashMap<>();
    private IController controller;
    private JLabel noteLabel;
    private HashSet<Character> pressedKeys = new HashSet<>();
    private KeyEventDispatcher keyEventDispatcher;

    public XylophonePanel(IController controller) {
        this.controller = controller;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // Demande le focus lorsque le composant est ajouté à la hiérarchie
    }

    public void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(1280, 700)); // Taille similaire au piano
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);
        initializeBars();

        // Utiliser un KeyEventDispatcher pour capturer les événements de clavier au niveau global
        keyEventDispatcher = new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                synchronized (XylophonePanel.this) {
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
        setBackground(Color.WHITE);
    }

    private void initializeBars() {
        int baseHeight = 500;
        int heightDecrease = 40;
        int fixedWidth = 150; // Largeur fixe pour toutes les lames

        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        barPanel.setBackground(Color.WHITE);
        for (int i = 0; i < NOTES.length; i++) {
            int height = baseHeight - (i * heightDecrease);
            JButton bar = createBar(NOTES[i], BAR_COLORS[i], fixedWidth, height);
            barPanel.add(bar);
        }

        add(Box.createVerticalGlue()); // Espace flexible au-dessus
        add(barPanel);

        // Add note display label
        noteLabel = new JLabel(" ", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.BOLD, 18));
        noteLabel.setPreferredSize(new Dimension(100, 30)); // Fixed size to prevent flickering
        noteLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(noteLabel);

        add(Box.createVerticalGlue()); // Espace flexible en dessous
    }

    private JButton createBar(String note, Color color, int width, int height) {
        JButton bar = new JButton();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(width, height));
        bar.setFocusPainted(false);
        bar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bar.setOpaque(true);

        noteToButton.put(note, bar);

        bar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleBarPress(bar, note);
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleBarRelease(bar, note);
                requestFocusInWindow();
            }
        });

        return bar;
    }

    private void handleBarRelease(JButton bar, String note) {
        bar.setBackground(getOriginalColor(bar));
        if (controller != null && note != null) {
            controller.stopNote(0, note);
            // Use a space instead of empty string to maintain label visibility
            if (pressedKeys.isEmpty()){
                noteLabel.setText(" ");
            }
        }
    }

    private void handleBarPress(JButton bar, String note) {
        bar.setBackground(Color.GRAY);
        if (controller != null && note != null) {
            controller.playNote(0, note);
            // Update the label to show the played note
            noteLabel.setText(note);
        }

        Timer timer = new Timer(200, e -> bar.setBackground(getOriginalColor(bar)));
        timer.setRepeats(false);
        timer.start();
    }

    private Color getOriginalColor(JButton bar) {
        for (int i = 0; i < NOTES.length; i++) {
            if (noteToButton.get(NOTES[i]) == bar) {
                return BAR_COLORS[i];
            }
        }
        return Color.BLACK;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Pas utilisé
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (controller.isSaving()) {
            return; // Ignore les événements de touche si l'enregistrement est en cours
        }
        char keyChar = e.getKeyChar();

        // Only process the key if it's not already pressed
        if (!pressedKeys.contains(keyChar)) {
            int index = getNoteIndexFromKeyChar(keyChar);
            if (index != -1) {
                String note = NOTES[index];
                JButton bar = noteToButton.get(note);
                if (bar != null) {
                    pressedKeys.add(keyChar); // Add to pressed keys set
                    handleBarPress(bar, note);
                    // Always update the note label with the most recently pressed key
                    noteLabel.setText(note);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char keyChar = e.getKeyChar();
        pressedKeys.remove(keyChar); // Remove from pressed keys set

        int index = getNoteIndexFromKeyChar(keyChar);
        if (index != -1) {
            String note = NOTES[index];
            JButton bar = noteToButton.get(note);
            if (bar != null) {
                handleBarRelease(bar, note);
            }
        }
    }

    private int getNoteIndexFromKeyChar(final char keyChar) {
        for (int i = 0; i < KEY_MAPPINGS.length; i++) {
            if (KEY_MAPPINGS[i] == Character.toLowerCase(keyChar)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void focusGained(FocusEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Pas utilisé
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
