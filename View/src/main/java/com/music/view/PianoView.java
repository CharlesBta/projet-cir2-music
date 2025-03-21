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
    private static final int MAX_KEYS_PER_ROW = 21;
    private static final int WHITE_KEY_WIDTH = 60;
    private static final int BLACK_KEY_WIDTH = 40;
    private static final int BLACK_KEY_HEIGHT = 120;
    private static final int WHITE_KEY_HEIGHT = 200;
    private static final int ROW_HEIGHT = 250;
    private IController controller;
    private int numberOfOctaves = 2;
    private JLabel octaveLabel;
    private JPanel controlPanel;
    private JLayeredPane pianoKeysPanel;

    public PianoView(IController controller) {
        this.controller = controller;
        setTitle("Piano Virtuel");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        pianoKeysPanel = new JLayeredPane();
        pianoKeysPanel.setPreferredSize(new Dimension(900, 300));

        initializeKeys();

        mainPanel.add(pianoKeysPanel, gbc);
        add(mainPanel, BorderLayout.CENTER);

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

        String[] whiteNotes = {"C", "D", "E", "F", "G", "A", "B"};
        String[] blackNotes = {"Db", "Eb", null, "Gb", "Ab", "Bb", null};

        int numWhiteKeys = numberOfOctaves * 7;
        int numRows = (int) Math.ceil((double) numWhiteKeys / MAX_KEYS_PER_ROW);

        int startX = 0;
        int startY = 50;
        int row = 0;

        for (int octave = 0; octave < numberOfOctaves; octave++) {
            for (int i = 0; i < 7; i++) {
                if (startX >= MAX_KEYS_PER_ROW * WHITE_KEY_WIDTH) {
                    row++;
                    startX = 0;
                    startY += ROW_HEIGHT;
                }

                int keyCode = KeyEvent.VK_A + (octave * 7) + i;
                Note note = new Note(octave, whiteNotes[i]);
                JButton whiteKey = createKey(keyCode, Color.WHITE, startX, startY, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
                keyToNote.put(keyCode, note);
                pianoKeysPanel.add(whiteKey, Integer.valueOf(1));

                if (blackNotes[i] != null) {
                    int blackKeyX = startX + (WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2);
                    int blackKeyCode = KeyEvent.VK_G + (octave * 5) + i;
                    Note blackNote = new Note(octave, blackNotes[i]);
                    JButton blackKey = createKey(blackKeyCode, Color.BLACK, blackKeyX, startY, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
                    keyToNote.put(blackKeyCode, blackNote);
                    pianoKeysPanel.add(blackKey, Integer.valueOf(2));
                }

                startX += WHITE_KEY_WIDTH;
            }
        }

        pianoKeysPanel.setPreferredSize(new Dimension(Math.min(numWhiteKeys, MAX_KEYS_PER_ROW) * WHITE_KEY_WIDTH, numRows * ROW_HEIGHT));
        pianoKeysPanel.revalidate();
        pianoKeysPanel.repaint();
    }

    private JButton createKey(int keyCode, Color color, int x, int y, int width, int height) {
        JButton key = new JButton();
        key.setBackground(color);
        key.setForeground(color == Color.WHITE ? Color.BLACK : Color.WHITE);
        key.setFocusPainted(false);
        key.setBounds(x, y, width, height);
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
        key.setBackground(key.getBounds().width == WHITE_KEY_WIDTH ? Color.WHITE : Color.BLACK);
    }

    private void changeOctaves(int change) {
        numberOfOctaves = Math.max(1, Math.min(7, numberOfOctaves + change));
        octaveLabel.setText("Octaves : " + numberOfOctaves);
        //controller.setOctave(this.numberOfOctaves);

        initializeKeys();

        int newWidth = Math.min(numberOfOctaves * 420, 1280);
        int newHeight = pianoKeysPanel.getPreferredSize().height + 150;
        setSize(newWidth, newHeight);

        requestFocusInWindow();
    }
}
