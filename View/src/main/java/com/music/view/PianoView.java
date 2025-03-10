package com.music.view;

import com.music.Note;
import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;

public class PianoView extends JFrame {
    private static final HashMap<Integer, Note> keyToNote = new HashMap<>();
    private static final HashMap<Integer, JButton> keyToButton = new HashMap<>();
    private static final HashSet<Integer> activeKeys = new HashSet<>();
    static int[] whiteKeyCodes = {KeyEvent.VK_A, KeyEvent.VK_Z, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_T, KeyEvent.VK_Y, KeyEvent.VK_U, KeyEvent.VK_I, KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F};
    static int[] blackKeyCodes = {KeyEvent.VK_G, KeyEvent.VK_H, -1, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, -1, KeyEvent.VK_M, KeyEvent.VK_W, -1, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V, -1};

    static {
        keyToNote.put(whiteKeyCodes[0], new Note(0, "C"));
        keyToNote.put(blackKeyCodes[0], new Note(0, "Db"));
        keyToNote.put(whiteKeyCodes[1], new Note(0, "D"));
        keyToNote.put(blackKeyCodes[1], new Note(0, "Eb"));
        keyToNote.put(whiteKeyCodes[2], new Note(0, "E"));
        keyToNote.put(whiteKeyCodes[3], new Note(0, "F"));
        keyToNote.put(blackKeyCodes[3], new Note(0, "Gb"));
        keyToNote.put(whiteKeyCodes[4], new Note(0, "G"));
        keyToNote.put(blackKeyCodes[4], new Note(0, "Ab"));
        keyToNote.put(whiteKeyCodes[5], new Note(0, "A"));
        keyToNote.put(blackKeyCodes[5], new Note(0, "Bb"));
        keyToNote.put(whiteKeyCodes[6], new Note(0, "B"));

        keyToNote.put(whiteKeyCodes[7], new Note(1, "C"));
        keyToNote.put(blackKeyCodes[7], new Note(1, "Db"));
        keyToNote.put(whiteKeyCodes[8], new Note(1, "D"));
        keyToNote.put(blackKeyCodes[8], new Note(1, "Eb"));
        keyToNote.put(whiteKeyCodes[9], new Note(1, "E"));
        keyToNote.put(whiteKeyCodes[10], new Note(1, "F"));
        keyToNote.put(blackKeyCodes[10], new Note(1, "Gb"));
        keyToNote.put(whiteKeyCodes[11], new Note(1, "G"));
        keyToNote.put(blackKeyCodes[11], new Note(1, "Ab"));
        keyToNote.put(whiteKeyCodes[12], new Note(1, "A"));
        keyToNote.put(blackKeyCodes[12], new Note(1, "Bb"));
        keyToNote.put(whiteKeyCodes[13], new Note(1, "B"));
    }

    private IController controller;
    private int numberOfOctaves = 2;
    private JLabel octaveLabel;
    private JPanel controlPanel;
    private JLayeredPane pianoKeysPanel;

    public PianoView(IController controller) {
        this.controller = controller;
        setTitle("Piano Virtuel");
        // Taille de la fenêtre ajustée pour mieux correspondre à l'écran
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Créer le panneau principal avec GridBagLayout pour centrer le piano
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Créer un JLayeredPane pour les touches de piano
        pianoKeysPanel = new JLayeredPane();
        pianoKeysPanel.setPreferredSize(new Dimension(900, 300)); // Réduire la largeur du piano pour l'adapter à la fenêtre

        // Initialiser les touches du piano
        initializeKeys();

        mainPanel.add(pianoKeysPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);

        // Créer le panneau de contrôle avec des boutons pour changer le nombre d'octaves
        controlPanel = new JPanel();
        octaveLabel = new JLabel("Octaves : " + numberOfOctaves);
        JButton increaseButton = new JButton("+");
        JButton decreaseButton = new JButton("-");

        increaseButton.addActionListener(e -> changeOctaves(1));
        decreaseButton.addActionListener(e -> changeOctaves(-1));

        controlPanel.add(decreaseButton);
        controlPanel.add(octaveLabel);
        controlPanel.add(increaseButton);
        add(controlPanel, BorderLayout.NORTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e.getKeyCode());
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeKeys() {
        pianoKeysPanel.removeAll();
        keyToButton.clear();

        int[] whiteKeyPositions = {0, 60, 120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720, 780};
        int[] blackKeyPositions = {40, 100, -1, 220, 280, 340, -1, 460, 520, -1, 640, 700, 760, -1};

        // Créer les touches blanches
        for (int i = 0; i < whiteKeyPositions.length; i++) {
            JButton whiteKey = createKey(whiteKeyCodes[i], Color.WHITE, whiteKeyPositions[i], 60, 200);
            pianoKeysPanel.add(whiteKey, Integer.valueOf(1));
        }

        // Créer les touches noires
        for (int i = 0; i < blackKeyPositions.length; i++) {
            if (blackKeyPositions[i] != -1) {
                JButton blackKey = createKey(blackKeyCodes[i], Color.BLACK, blackKeyPositions[i], 40, 120);
                pianoKeysPanel.add(blackKey, Integer.valueOf(2));
            }
        }

        // Mettre à jour le panneau et redessiner les touches
        pianoKeysPanel.revalidate();
        pianoKeysPanel.repaint();
    }

    private JButton createKey(int keyCode, Color color, int x, int width, int height) {
        JButton key = new JButton();
        key.setBackground(color);
        key.setForeground(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        key.setFocusPainted(false);
        key.setBounds(x, 50, width, height);
        key.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        keyToButton.put(keyCode, key);

        key.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(key, keyCode);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseRelease(key, keyCode);
            }
        });

        return key;
    }

    private void handleKeyPress(int keyCode) {
        if (!activeKeys.contains(keyCode)) {
            activeKeys.add(keyCode);
            JButton keyButton = keyToButton.get(keyCode);
            if (keyButton != null) keyButton.setBackground(Color.GRAY);
            if (keyToNote.get(keyCode) != null)
                this.controller.playNote(keyToNote.get(keyCode).getOctave(), keyToNote.get(keyCode).getNote());
        }
    }

    private void handleKeyRelease(int keyCode) {
        activeKeys.remove(keyCode);
        JButton keyButton = keyToButton.get(keyCode);
        if (keyButton != null) resetKeyColor(keyButton);
        if (keyToNote.get(keyCode) != null)
            this.controller.stopNote(keyToNote.get(keyCode).getOctave(), keyToNote.get(keyCode).getNote());
    }

    private void handleMousePress(JButton key, int keyCode) {
        key.setBackground(Color.GRAY);
        requestFocusInWindow();
        if (keyToNote.get(keyCode) != null)
            this.controller.playNote(keyToNote.get(keyCode).getOctave(), keyToNote.get(keyCode).getNote());
    }

    private void handleMouseRelease(JButton key, int keyCode) {
        resetKeyColor(key);
        requestFocusInWindow();
        if (keyToNote.get(keyCode) != null)
            this.controller.stopNote(keyToNote.get(keyCode).getOctave(), keyToNote.get(keyCode).getNote());
    }

    private void resetKeyColor(JButton key) {
        key.setBackground(key.getBounds().width == 60 ? Color.WHITE : Color.BLACK);
    }

    private void changeOctaves(int change) {
        numberOfOctaves = Math.max(-2, Math.min(7, numberOfOctaves + change));
        octaveLabel.setText("Octaves : " + numberOfOctaves);
        controller.setOctave(this.numberOfOctaves);
        initializeKeys();
        requestFocusInWindow();
    }
}