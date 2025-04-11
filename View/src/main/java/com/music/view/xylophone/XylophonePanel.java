package com.music.view.xylophone;

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

public class XylophonePanel extends JPanel implements KeyListener, FocusListener {

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

    public XylophonePanel(IController controller) {
        this.controller = controller;
    }

    public void init(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(1280, 720)); // Taille similaire au piano
        setFocusable(true);
        addKeyListener(this);
        addFocusListener(this);
        initializeBars();

        // Utiliser un KeyEventDispatcher pour capturer les événements de clavier au niveau global
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (isFocusOwner()) {
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

    private void initializeBars() {
        int baseHeight = 300;
        int heightDecrease = 30;
        int fixedWidth = 150; // Largeur fixe pour toutes les lames

        JPanel barPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        for (int i = 0; i < NOTES.length; i++) {
            int height = baseHeight - (i * heightDecrease);
            JButton bar = createBar(NOTES[i], BAR_COLORS[i], fixedWidth, height);
            barPanel.add(bar);
        }

        add(Box.createVerticalGlue()); // Espace flexible au-dessus
        add(barPanel);
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
        }
    }

    private void handleBarPress(JButton bar, String note) {
        bar.setBackground(Color.GRAY);
        if (controller != null && note != null) {
            controller.playNote(0, note);
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
        char keyChar = e.getKeyChar();
        int index = getNoteIndexFromKeyChar(keyChar);
        if (index != -1) {
            String note = NOTES[index];
            JButton bar = noteToButton.get(note);
            if (bar != null) {
                handleBarPress(bar, note);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        char keyChar = e.getKeyChar();
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
}
