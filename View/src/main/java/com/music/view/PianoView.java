package com.music.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PianoView extends JFrame implements ActionListener {
    private JPanel pianoPanel;
    private JButton addPianoButton;
    private JButton removePianoButton;

    public PianoView() {
        setTitle("Piano View");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        pianoPanel = new PianoPanel();
        addPianoButton = new JButton("+");
        removePianoButton = new JButton("-");

        addPianoButton.addActionListener(this);
        removePianoButton.addActionListener(this);

        add(pianoPanel);
        add(addPianoButton);
        add(removePianoButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPianoButton) {
            addPiano();
        } else if (e.getSource() == removePianoButton) {
            removePiano();
        }
    }

    private void addPiano() {
        PianoPanel newPianoPanel = new PianoPanel();
        add(newPianoPanel);
        revalidate();
        repaint();
    }

    private void removePiano() {
        if (getContentPane().getComponentCount() > 3) {
            getContentPane().remove(getContentPane().getComponentCount() - 1);
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PianoView pianoView = new PianoView();
            pianoView.setVisible(true);
        });
    }
}
