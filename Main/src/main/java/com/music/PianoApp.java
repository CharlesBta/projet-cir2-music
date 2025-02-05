package com.music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;

public class PianoApp {
    private static Synthesizer synthesizer;
    private static MidiChannel channel;

    // Ensemble pour suivre les touches enfoncées afin d'éviter les répétitions
    private static HashSet<Integer> activeKeys = new HashSet<>();

    public static void main(String[] args) {
        try {
            // Initialiser le synthétiseur MIDI
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            MidiChannel[] channels = synthesizer.getChannels();
            channel = channels[0]; // Utilise le premier canal MIDI

            // Créer la fenêtre principale
            JFrame frame = new JFrame("Piano Virtuel avec MIDI");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 300);

            // Panneau contenant les touches du piano
            JPanel pianoPanel = new JPanel();
            pianoPanel.setLayout(new GridLayout(1, 12)); // 12 touches pour une octave

            // Map pour associer les touches du clavier aux notes MIDI
            HashMap<Integer, Integer> keyToPitchMap = new HashMap<>();
            keyToPitchMap.put(KeyEvent.VK_A, 60); // 'A' correspond à la note Do
            keyToPitchMap.put(KeyEvent.VK_S, 62); // 'S' correspond à la note Ré
            keyToPitchMap.put(KeyEvent.VK_D, 64); // 'D' correspond à la note Mi
            keyToPitchMap.put(KeyEvent.VK_F, 65); // 'F' correspond à la note Fa
            keyToPitchMap.put(KeyEvent.VK_G, 67); // 'G' correspond à la note Sol
            keyToPitchMap.put(KeyEvent.VK_H, 69); // 'H' correspond à la note La
            keyToPitchMap.put(KeyEvent.VK_J, 71); // 'J' correspond à la note Si
            keyToPitchMap.put(KeyEvent.VK_K, 72); // 'K' correspond à la note Do (octave supérieure)

            // Ajouter les touches blanches
            for (int i = 0; i < 12; i++) {
                JButton key = new JButton(" ");
                key.setBackground(Color.WHITE);
                key.setForeground(Color.BLACK);
                key.setFocusPainted(false);
                key.setPreferredSize(new Dimension(60, 200));
                pianoPanel.add(key);
            }

            // Ajouter le panneau à la fenêtre
            frame.add(pianoPanel);

            // Activer les événements clavier en forçant le focus sur la fenêtre
            frame.setFocusable(true);
            frame.requestFocusInWindow();

            // Ajouter un KeyListener pour détecter les pressions clavier
            frame.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    Integer pitch = keyToPitchMap.get(e.getKeyCode());
                    if (pitch != null && !activeKeys.contains(e.getKeyCode())) {
                        activeKeys.add(e.getKeyCode()); // Ajouter la touche active
                        playSound(pitch); // Joue la note
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    Integer pitch = keyToPitchMap.get(e.getKeyCode());
                    if (pitch != null) {
                        activeKeys.remove(e.getKeyCode()); // Retirer la touche active
                        stopSound(pitch); // Arrête la note
                    }
                }
            });

            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Fonction pour jouer une note MIDI
    private static void playSound(int pitch) {
        if (channel != null) {
            channel.noteOn(pitch, 90); // Démarre la note avec une vélocité de 90
            System.out.println("Joue le son de la note MIDI : " + pitch);
        }
    }

    // Fonction pour arrêter une note MIDI
    private static void stopSound(int pitch) {
        if (channel != null) {
            channel.noteOff(pitch); // Arrête la note
            System.out.println("Arrête le son de la note MIDI : " + pitch);
        }
    }
}