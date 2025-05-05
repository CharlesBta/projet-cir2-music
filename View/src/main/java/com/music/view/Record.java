package com.music.view;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.music.Note;
import com.music.controller.IController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.*;

public class Record {
    private List<IAction> actions = new ArrayList<>();
    private Timestamp previousTimestamp = new Timestamp(System.currentTimeMillis());
    private IController controller;
    private Boolean isRecording = false;
    private JLayeredPane panel;
    private Set<Character> pressedKeys = new HashSet<>();

    public Record(IController controller, JLayeredPane panel) {
        this.controller = controller;
        this.panel = panel;
    }

    public void record() {
        System.out.println("Recording started");
        isRecording = true;
        actions.clear();
        if (panel != null) {
            System.out.println("Key listener removed");
            panel.setFocusable(true);
            panel.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // Non utilisé
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    System.out.println("Key pressed: " + e.getKeyChar());
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
            panel.requestFocus();
        }
    }

    public void stop() {
        System.out.println("Recording stopped");
        isRecording = false;
        if (panel.getKeyListeners().length > 0) {
            panel.removeKeyListener(panel.getKeyListeners()[0]);
        }
        panel.setFocusable(false);
        Gson gson = new Gson();
        String actionsJson = gson.toJson(actions);
        System.out.println("Actions en JSON : " + actionsJson);
        if (actions.get(0) instanceof Pause) {
            actions.remove(0); // Remove the first pause if it exists
        }
        saveJsonToFile(actionsJson);
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

    private void saveJsonToFile(String jsonContent) {
        String directoryPath = "sujet";
        java.io.File directory = new java.io.File(directoryPath);

        // Crée le dossier s'il n'existe pas
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Génère un nom de fichier unique
        int fileIndex = 1;
        java.io.File file;
        do {
            file = new java.io.File(directory, "recording_" + fileIndex + ".json");
            fileIndex++;
        } while (file.exists());

        // Transforme les actions en JSON au format attendu
        Gson gson = new Gson();
        List<Map<String, Object>> formattedActions = new ArrayList<>();
        for (IAction action : actions) {
            Map<String, Object> actionMap = new HashMap<>();
            if (action instanceof Action) {
                Action act = (Action) action;
                actionMap.put("note", act.note().getNote());
                actionMap.put("octave", act.note().getOctave());
                actionMap.put("duration", act.duration());
            } else if (action instanceof Pause) {
                Pause pause = (Pause) action;
                actionMap.put("note", "Pause");
                actionMap.put("duration", pause.duration());
            }
            formattedActions.add(actionMap);
        }
        String formattedJsonContent = gson.toJson(formattedActions);

        // Écrit le contenu JSON dans le fichier
        try (java.io.FileWriter writer = new java.io.FileWriter(file)) {
            writer.write(formattedJsonContent);
            System.out.println("Fichier JSON enregistré : " + file.getAbsolutePath());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    private interface IAction {
    }

    private record Action(Note note, double duration) implements IAction {
    }

    private record Pause(String pause, double duration) implements IAction {
    }
}
