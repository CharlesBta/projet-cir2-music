package com.music.view;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class RecupNote {
    public static void main(String[] args) {
        try {
            // Lire le fichier JSON
            String content = new String(Files.readAllBytes(Paths.get("Sujet/star_wars.json")));
            JSONArray notesArray = new JSONArray(content);

            // Parcourir le JSON
            for (int i = 0; i < notesArray.length(); i++) {
                JSONObject noteObj = notesArray.getJSONObject(i);
                String note = noteObj.getString("note");
                double duration = noteObj.getDouble("duration");

                // Vérifier si c'est une pause
                if (note.equals("Pause")) {
                    System.out.println("Pause | Durée: " + duration);
                    continue;
                }

                // Obtenir l'octave
                int octave = noteObj.getInt("octave");

                // Convertir la note en fréquence
                int frequency = getFrequency(note, octave);

                // Afficher le résultat
                System.out.println("Note: " + note + octave + " | Fréquence: " + frequency + " Hz | Durée: " + duration);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getFrequency(String note, int octave) {
        // Fréquences de base pour l'octave 0 (en Hz)
        int baseFreq = switch (note) {
            case "Do" -> 16;
            case "Do#" -> 17;
            case "Ré" -> 18;
            case "Ré#" -> 19;
            case "Mi" -> 21;
            case "Fa" -> 22;
            case "Fa#" -> 23;
            case "Sol" -> 25;
            case "Sol#" -> 26;
            case "La" -> 28;
            case "La#" -> 29;
            case "Si" -> 31;
            default -> 0;
        };

        // Calculer la fréquence pour l'octave donné
        return (int) (baseFreq * Math.pow(2, octave));
    }
}

