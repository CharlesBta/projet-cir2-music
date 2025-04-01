package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class PianoPanel extends JPanel implements KeyListener, MouseListener {
    private Set<Integer> pressedKeys = new HashSet<>();

    public PianoPanel() {
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPiano(g);
    }

    private void drawPiano(Graphics g) {
        int whiteKeyWidth = getWidth() / 7;
        int blackKeyWidth = whiteKeyWidth / 2;
        int keyHeight = getHeight() / 3;

        g.setColor(Color.WHITE);
        for (int i = 0; i < 7; i++) {
            g.fillRect(i * whiteKeyWidth, 0, whiteKeyWidth, keyHeight * 3);
        }

        g.setColor(Color.BLACK);
        int[] blackKeyPositions = {1, 3, -1, 5, 7, 9, 11};
        for (int position : blackKeyPositions) {
            int x = (position * whiteKeyWidth) + (whiteKeyWidth / 2) - (blackKeyWidth / 2);
            g.fillRect(x, 0, blackKeyWidth, keyHeight * 2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Handle mouse click to play piano key
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
