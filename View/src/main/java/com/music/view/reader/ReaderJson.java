package com.music.view.reader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.music.controller.IController;

import java.io.FileReader;
import java.io.Reader;

public class ReaderJson {
    private String filePath;
    private String JsonString;
    private final IController controller;
    private Thread playbackThread;
    private volatile boolean stopPlayback = false;

    public ReaderJson(IController controller) {
        this.controller = controller;
    }

    public void play() {
        // Stop any existing playback
        stop();

        // Reset the stop flag
        stopPlayback = false;

        // Start playback in a new thread
        playbackThread = new Thread(() -> {
            if (this.JsonString != null) {
                this.playFromString();
            } else {
                this.playFromFile();
            }
        });

        playbackThread.start();
    }

    public void stop() {
        // Set the flag to stop playback
        stopPlayback = true;

        // Interrupt the thread if it's running
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt();
            try {
                // Wait for the thread to finish
                playbackThread.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void playFromString() {
        JsonArray jsonArray = JsonParser.parseString(this.JsonString).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            // Check if playback should stop
            if (stopPlayback) {
                break;
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String note = jsonObject.get("note").getAsString();
            double duration = jsonObject.get("duration").getAsDouble();
            int octave = jsonObject.has("octave") ? jsonObject.get("octave").getAsInt() : -1; // Valeur par défaut si octave n'est pas présent

            if ("Pause".equals(note) || octave == -1) {
                try {
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                } catch (InterruptedException e) {
                    // If interrupted, stop playback
                    return;
                }
            } else {
                controller.setOctave(octave);
                controller.playNote(0, note);
                try {
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                } catch (InterruptedException e) {
                    // If interrupted, stop the note and stop playback
                    controller.stopNote(0, note);
                    return;
                }
                // Check if playback should stop before stopping the note
                if (stopPlayback) {
                    controller.stopNote(0, note);
                    break;
                }
                controller.stopNote(0, note);
            }
        }
    }

    private void playFromFile() {
        try (Reader reader = new FileReader(this.filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                // Check if playback should stop
                if (stopPlayback) {
                    break;
                }

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String note = jsonObject.get("note").getAsString();
                double duration = jsonObject.get("duration").getAsDouble();
                int octave = jsonObject.has("octave") ? jsonObject.get("octave").getAsInt() : -1; // Valeur par défaut si octave n'est pas présent

                if ("Pause".equals(note) || octave == -1) {
                    try {
                        Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                    } catch (InterruptedException e) {
                        // If interrupted, stop playback
                        return;
                    }
                } else {
                    controller.setOctave(octave);
                    controller.playNote(0, note);
                    try {
                        Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                    } catch (InterruptedException e) {
                        // If interrupted, stop the note and stop playback
                        controller.stopNote(0, note);
                        return;
                    }
                    // Check if playback should stop before stopping the note
                    if (stopPlayback) {
                        controller.stopNote(0, note);
                        break;
                    }
                    controller.stopNote(0, note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.JsonString = null;
    }

    public void setJsonString(String JsonString) {
        this.JsonString = JsonString;
        this.filePath = null;
    }
}
