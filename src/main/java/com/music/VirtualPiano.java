package com.music;

import jm.music.data.*;
import jm.util.Play;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VirtualPiano {
    private static Thread playingThread; // Thread pour gérer le son en continu

    public static void main(String[] args) {
        // Créer la fenêtre principale
        JFrame frame = new JFrame("Piano Virtuel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 300);

        // Panneau contenant les touches du piano
        JPanel pianoPanel = new JPanel();
        pianoPanel.setLayout(new GridLayout(1, 12)); // 12 touches pour une octave

        // Ajouter les touches blanches
        for (int i = 0; i < 12; i++) {
            JButton key = new JButton(" ");
            key.setBackground(Color.WHITE);
            key.setForeground(Color.BLACK);
            key.setFocusPainted(false);
            key.setPreferredSize(new Dimension(60, 200));

            int pitch = 60 + i; // Hauteur MIDI (Do central + décalage)

            // Ajouter un écouteur pour détecter l'appui et le relâchement
            key.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    playNoteContinuously(pitch);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    stopNote();
                }
            });

            pianoPanel.add(key);
        }

        // Ajouter le panneau à la fenêtre
        frame.add(pianoPanel);
        frame.setVisible(true);
    }

    // Fonction pour jouer une note en continu
    private static void playNoteContinuously(int pitch) {
        stopNote(); // Arrêter tout son en cours

        playingThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Créer une note avec une très courte durée (0.1s)
                    Score score = new Score("Note");
                    Part part = new Part("Piano", jm.JMC.PIANO);
                    Phrase phrase = new Phrase();
                    phrase.addNote(new Note(pitch, 0.1)); // Note avec une courte durée
                    part.addPhrase(phrase);
                    score.addPart(part);

                    // Jouer la note
                    Play.midi(score);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        playingThread.start();
    }

    // Fonction pour arrêter le son
    private static void stopNote() {
        if (playingThread != null && playingThread.isAlive()) {
            playingThread.interrupt(); // Arrêter le thread en cours
        }
    }
}
