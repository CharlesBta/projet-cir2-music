package com.music;

public abstract class NoteOffsetCalculator {
    public static Integer convertToOffset(String note) {
        return switch (note) {

            case "Bit1","Bit8","Do", "B#" -> 0;  // B# est enharmonique de C/Do
            case "Do#", "Ré♭", "C#", "Db" -> 1;

            case "Bit2","Ré", "D" -> 2;
            case "Ré#", "Mi♭", "D#", "Eb" -> 3;

            case "Bit3","Mi", "E", "Fa♭", "Fb" -> 4;  // Fa♭ (Fb) est enharmonique de Mi
            case "Mi#" -> 5;  // Mi# (inhabituel) est enharmonique de Fa

            case "Bit4","Fa", "F" -> 5;
            case "Fa#", "Sol♭", "F#", "Gb" -> 6;

            case "Bit5","Sol", "G" -> 7;
            case "Sol#", "La♭", "G#", "Ab" -> 8;

            case "Bit6","La", "A" -> 9;
            case "La#", "Si♭", "A#", "Bb" -> 10;

            case "Bit7","Si", "B", "Do♭", "Cb" -> 11;  // Do♭ (Cb) est l'équivalent de Si
            case "Si#", "C" -> 0;  // Si# est enharmonique de Do

            default -> null; // Valeur par défaut pour les entrées invalides
        };
    }
}
