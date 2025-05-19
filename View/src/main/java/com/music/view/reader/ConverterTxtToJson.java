package com.music.view.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.music.NoteOffsetCalculator;
import lombok.Setter;


@Setter
public class ConverterTxtToJson {
    private String filePath;

    public String convertFileToJsonString() {
        List<Map<String, Object>> notes = convertFileToJson(filePath);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(notes);
    }

    private List<Map<String, Object>> convertFileToJson(String filePath) {
        List<Map<String, Object>> notes = new ArrayList<>();
        Map<String, String> noteMapping = createNoteMapping();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String note = parts[0];
                double duration = Double.parseDouble(parts[1]);

                Map<String, Object> noteMap = new HashMap<>();
                if (note.equals("0")) {
                    noteMap.put("note", "Pause");
                } else {
                    String noteName = noteMapping.get(note.substring(0, note.length() - 1));
                    int octave = Integer.parseInt(note.substring(note.length() - 1));
                    noteMap.put("note", noteName);
                    noteMap.put("octave", octave);

                    // Calculate and add the offset
                    Integer offset = NoteOffsetCalculator.convertToOffset(noteName);
                    if (offset != null) {
                        noteMap.put("offset", offset);
                    }
                }
                noteMap.put("duration", duration);
                notes.add(noteMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return notes;
    }

    private static Map<String, String> createNoteMapping() {
        Map<String, String> noteMapping = new HashMap<>();
        noteMapping.put("C", "Do");
        noteMapping.put("D", "Ré");
        noteMapping.put("E", "Mi");
        noteMapping.put("F", "Fa");
        noteMapping.put("G", "Sol");
        noteMapping.put("A", "La");
        noteMapping.put("B", "Si");
        noteMapping.put("A#", "La#");
        noteMapping.put("C#", "Do#");
        noteMapping.put("D#", "Ré#");
        noteMapping.put("F#", "Fa#");
        noteMapping.put("G#", "Sol#");
        noteMapping.put("Bb", "Si♭");
        noteMapping.put("Db", "Ré♭");
        noteMapping.put("Eb", "Mi♭");
        noteMapping.put("Gb", "Fa♭");
        noteMapping.put("Ab", "La♭");
        noteMapping.put("Cb", "Do♭");
        return noteMapping;
    }
}