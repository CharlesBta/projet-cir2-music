package com.music;

import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jm.music.data.*;
import jm.util.Play;

public class PianoApp {
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
            key.addActionListener(e -> playSound(pitch));
            pianoPanel.add(key);
        }

        // Ajouter le panneau à la fenêtre
        frame.add(pianoPanel);
        frame.setVisible(true);
    }

    private static void playSound2(int pitch, double duration) {
        // Créer un score avec une note
        Score score = new Score("Note");
        Part part = new Part("Piano", jm.JMC.PIANO);
        Phrase phrase = new Phrase();
        phrase.addNote(new Note(pitch, duration)); // Note avec durée de 1 seconde
        part.addPhrase(phrase);
        score.addPart(part);

        // Jouer la note
        Play.midi(score);
    }

    // Fonction pour jouer un son à une hauteur donnée (exemple simplifié)
    private static void playSound(int pitch) {
        System.out.println("Joue le son de la note MIDI : " + pitch);
        // Ici, tu peux intégrer la génération de son avec Java Sound API ou JMusic.
        playSound2(pitch, 0.01);

    }
}
