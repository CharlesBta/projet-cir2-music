package com.music.view;

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

    public ReaderJson(IController controller) {
        this.controller = controller;
    }

    public void play() {
        if (this.JsonString != null) {
            this.playFromString();
        } else {
            this.playFromFile();
        }
    }

    private void playFromString() {
        JsonArray jsonArray = JsonParser.parseString(this.JsonString).getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String note = jsonObject.get("note").getAsString();
            double duration = jsonObject.get("duration").getAsDouble();
            int octave = jsonObject.has("octave") ? jsonObject.get("octave").getAsInt() : -1; // Valeur par défaut si octave n'est pas présent

            if ("Pause".equals(note) || octave == -1) {
                try {
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                controller.setOctave(octave);
                controller.playNote(0, note);
                try {
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                controller.stopNote(0, note);
            }
        }
    }

    private void playFromFile() {
        try (Reader reader = new FileReader(this.filePath)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String note = jsonObject.get("note").getAsString();
                double duration = jsonObject.get("duration").getAsDouble();
                int octave = jsonObject.has("octave") ? jsonObject.get("octave").getAsInt() : -1; // Valeur par défaut si octave n'est pas présent

                if ("Pause".equals(note) || octave == -1) {
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
                } else {
                    controller.setOctave(octave);
                    controller.playNote(0, note);
                    Thread.sleep((long) (duration * 1000)); // Pause de la durée spécifiée
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