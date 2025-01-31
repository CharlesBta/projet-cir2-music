package com.music;

import java.awt.*;
import java.awt.event.*;

public class KeyPressExample extends Frame implements KeyListener {

    public KeyPressExample() {
        // Configurer la fenêtre
        setTitle("Appui sur une touche");
        setSize(400, 200);
        setLayout(new FlowLayout());
        Label label = new Label("Appuyez sur une touche !");
        add(label);

        // Ajout du KeyListener
        addKeyListener(this);

        // Fermer la fenêtre lorsque demandé
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    // Gestion de la touche pressée
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Touche pressée : " + e.getKeyChar());
    }

    // Gestion de la touche relâchée
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Touche relâchée : " + e.getKeyChar());
    }

    // Gestion d'une touche tapée
    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Touche tapée : " + e.getKeyChar());
    }

    public static void main(String[] args) {
        KeyPressExample example = new KeyPressExample();
        example.setVisible(true);
    }
}
