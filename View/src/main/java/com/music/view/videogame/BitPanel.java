package com.music.view.videogame;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;

public class BitPanel extends JLayeredPane implements KeyListener, FocusListener {

    private static final String[] BIT_NOTES = {"Bit1", "Bit2", "Bit3", "Bit4", "Bit5", "Bit6", "Bit7", "Bit8"};
    private static final char[] KEY_MAPPINGS = {'q', 's', 'd', 'f', 'g', 'h', 'j', 'k'};

    private HashMap<String, JButton> noteToButton = new HashMap<>();
    private IController controller;

    public BitPanel(IController controller) {
        this.controller = controller;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow(); // Demande le focus lorsque le composant est ajouté à la hiérarchie
    }

    public void init() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(1280, 720));
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);
        initializeBits();

        // Utiliser un KeyEventDispatcher pour capturer les événements de clavier au niveau global
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                synchronized (BitPanel.this) { // ou XylophonePanel.this
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        keyPressed(e);
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        keyReleased(e);
                    }
                }
                return false;
            }
        });
    }


    private void initializeBits() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        for (int i = 0; i < BIT_NOTES.length; i++) {
            JButton bit = createBit(BIT_NOTES[i]);
            gbc.gridx = i;
            add(bit, gbc);
        }
    }

    private JButton createBit(String note) {
        JButton bit = new JButton();
        bit.setPreferredSize(new Dimension(150, 150));
        bit.setFocusPainted(false);
        bit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        bit.setOpaque(true);

        String imagePath = "/" + note + ".png";
        URL imageUrl = getClass().getResource(imagePath);
        if (imageUrl != null) {
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            bit.setIcon(scaledIcon);
        }

        bit.setHorizontalAlignment(SwingConstants.CENTER);
        bit.setVerticalAlignment(SwingConstants.CENTER);

        noteToButton.put(note, bit);

        bit.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleBitPress(bit, note);
                requestFocusInWindow();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleBitRelease(bit, note);
                requestFocusInWindow();
            }
        });

        return bit;
    }

    private void handleBitRelease(JButton bit, String note) {
        bit.setBackground(null);
        if (controller != null && note != null) {
            controller.stopNote(0, note);
        }
    }

    private void handleBitPress(JButton bit, String note) {
        bit.setBackground(Color.GRAY);
        if (controller != null && note != null) {
            controller.playNote(0, note);
        }

        Timer timer = new Timer(200, e -> bit.setBackground(null));
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int index = getNoteIndexFromKeyChar(keyChar);
        if (index != -1) {
            String note = BIT_NOTES[index];
            JButton bit = noteToButton.get(note);
            if (bit != null) {
                handleBitPress(bit, note);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char keyChar = e.getKeyChar();
        int index = getNoteIndexFromKeyChar(keyChar);
        if (index != -1) {
            String note = BIT_NOTES[index];
            JButton bit = noteToButton.get(note);
            if (bit != null) {
                handleBitRelease(bit, note);
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
    }
}
