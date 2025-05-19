package com.music.view.record;

import com.google.gson.Gson;
import com.music.Note;
import com.music.controller.IController;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class responsible for recording user's musical performance.
 * Records key presses and converts them to musical notes with timing information.
 * Allows saving the recorded performance to a JSON file.
 */
public class Record {
    private static final Logger LOGGER = Logger.getLogger(Record.class.getName());

    /** List of recorded actions (notes and pauses) */
    private List<IAction> actions = new ArrayList<>();

    /** Timestamp of the previous action, used to calculate durations */
    private Timestamp previousTimestamp = new Timestamp(System.currentTimeMillis());

    /** Controller for interacting with the model */
    private IController controller;

    /** Flag indicating if recording is in progress */
    private Boolean isRecording = false;

    /** The panel that receives key events */
    private JLayeredPane panel;

    /** Set of currently pressed keys to prevent duplicate events */
    private Set<Character> pressedKeys = new HashSet<>();

    /**
     * Constructs a new Record with the specified controller and panel.
     *
     * @param controller The controller to interact with the model
     * @param panel The panel that will receive key events
     */
    public Record(IController controller, JLayeredPane panel) {
        this.controller = controller;
        this.panel = panel;
    }

    /**
     * Starts recording the user's musical performance.
     * Sets up key listeners to capture key presses and releases.
     */
    public void record() {
        isRecording = true;
        controller.setIsRecording(isRecording);
        actions.clear();
        if (panel != null) {
            panel.setFocusable(true);
            panel.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // Not used
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    if (Character.isDefined(keyChar) && !pressedKeys.contains(keyChar)) {
                        pressedKeys.add(keyChar);
                        addPause();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    if (Character.isDefined(keyChar)) {
                        pressedKeys.remove(keyChar);
                        Note newNote = new Note(controller.getOctave(), keyChar);
                        addAction(newNote);
                    }
                }
            });
            panel.requestFocus();
        }
    }

    /**
     * Stops recording and saves the recorded performance.
     * Removes key listeners and prompts the user to save the recording as a JSON file.
     */
    public void stop() {
        if (!isRecording) {
            return;
        }
        isRecording = false;
        controller.setIsRecording(isRecording);
        if (actions.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "No actions recorded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (panel.getKeyListeners().length > 0) {
            panel.removeKeyListener(panel.getKeyListeners()[0]);
        }
        panel.setFocusable(false);
        Gson gson = new Gson();
        String actionsJson = gson.toJson(actions);
        if (actions.getFirst() instanceof Pause) {
            actions.removeFirst();
        }
        saveJsonToFile(actionsJson);
    }

    /**
     * Adds a note action to the recording.
     * Calculates the duration since the previous action.
     * 
     * @param note The musical note to add
     */
    private void addAction(Note note) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        double duration = (timestamp.getTime() - previousTimestamp.getTime()) * 0.001;
        actions.add(new Action(note, duration));
        previousTimestamp = timestamp;
    }

    /**
     * Adds a pause action to the recording.
     * Calculates the duration since the previous action.
     */
    private void addPause() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        double duration = (timestamp.getTime() - previousTimestamp.getTime()) * 0.001;
        actions.add(new Pause("Pause", duration));
        previousTimestamp = timestamp;
    }

    /**
     * Saves the recorded actions to a JSON file.
     * Prompts the user to choose a file location and name.
     * 
     * @param jsonContent The JSON content to save (not used directly, reformatted internally)
     */
    private void saveJsonToFile(String jsonContent) {
        controller.setIsSaving(true);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save As");

        int userSelection = fileChooser.showSaveDialog(panel);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Add .json extension if not already present
            if (!fileToSave.getName().toLowerCase().endsWith(".json")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
            }

            // Transform actions to JSON in the expected format
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

            // Write JSON content to the file
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(formattedJsonContent);
                LOGGER.info("JSON file saved: " + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error saving JSON file", e);
                JOptionPane.showMessageDialog(panel, "Error saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        controller.setIsSaving(false);
    }

    /**
     * Interface for actions in the recording.
     * Serves as a marker interface for both Note actions and Pause actions.
     */
    private interface IAction {
    }

    /**
     * Record representing a musical note action with its duration.
     * 
     * @param note The musical note
     * @param duration The duration in seconds
     */
    private record Action(Note note, double duration) implements IAction {
    }

    /**
     * Record representing a pause action with its duration.
     * 
     * @param pause The pause identifier (always "Pause")
     * @param duration The duration in seconds
     */
    private record Pause(String pause, double duration) implements IAction {
    }
}
