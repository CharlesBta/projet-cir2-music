package com.music.view;

import com.music.Note;
import com.music.controller.IController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Record {
    private List<IAction> actions = new ArrayList<>();
    private Timestamp previousTimestamp = new Timestamp(System.currentTimeMillis());
    private IController controller;
    private Boolean isRecording = false;
    private JPanel panel;
    private Set<Character> pressedKeys = new HashSet<>();

    public Record(IController controller, JPanel panel) {
        this.controller = controller;
        this.panel = panel;
    }

    public void record() {
        isRecording = true;
        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Non utilisé
            }

            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isDefined(keyChar) && !pressedKeys.contains(keyChar)) {
                    System.out.println("Key pressed: " + keyChar);
                    pressedKeys.add(keyChar);
                    addPause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isDefined(keyChar)) {
                    System.out.println("Key released: " + keyChar);
                    pressedKeys.remove(keyChar);
                    Note newNote = new Note(controller.getOctave(), keyChar);
                    addAction(newNote);
                }
            }
        });
    }

    public void stop() {
        isRecording = false;
        if (panel.getKeyListeners().length > 0) {
            panel.removeKeyListener(panel.getKeyListeners()[0]);
        }
        panel.setFocusable(false);

    }

    private void addAction(Note note) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        double duration = (timestamp.getTime() - previousTimestamp.getTime()) * 0.001;
        actions.add(new Action(note, duration));
        previousTimestamp = timestamp; // Update the previous timestamp
    }

    private void addPause() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        double duration = (timestamp.getTime() - previousTimestamp.getTime()) * 0.001;
        actions.add(new Pause("Pause", duration));
        previousTimestamp = timestamp; // Update the previous timestamp
    }

    private interface IAction {
    }

    private record Action(Note note, double duration) implements IAction {
    }

    private record Pause(String pause, double duration) implements IAction {
    }
}
