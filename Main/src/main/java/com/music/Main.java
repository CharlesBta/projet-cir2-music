package com.music;

import com.music.Note;
import com.music.controller.Controller;
import com.music.controller.IController;
import com.music.model.Model;
import com.music.view.Record;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model, "Piano");

        // Création du panneau pour capturer les événements clavier
        JPanel panel = new JPanel();
        JFrame frame = new JFrame("Test Record");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        // Initialisation de la classe Record
        Record record = new Record(controller, panel);

        // Ajout d'un KeyListener pour démarrer et arrêter l'enregistrement
        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Non utilisé
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'r') {
                    System.out.println("Enregistrement démarré...");
                    new Thread(record::record).start();
                } else if (e.getKeyChar() == 's') {
                    System.out.println("Enregistrement arrêté.");
                    record.stop();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Non utilisé
            }
        });

        frame.setVisible(true);
    }
}