package com.music;

import lombok.Getter;

@Getter
public class Note {
    int octave;
    String note;

    public Note(int octave, String note) {
        this.octave = octave;
        this.note = note;
    }

    public Note(int octave, char keyPressed) {
        this.octave = octave;
        this.note = getNoteFromKeyChar(keyPressed);
    }

    public String getNoteFromKeyChar(final char keyChar) {
        switch (keyChar) {
            case 'q':
                return "C";
            case 'z':
                return "C#";
            case 's':
                return "D";
            case 'e':
                return "D#";
            case 'd':
                return "E";
            case 'f':
                return "F";
            case 't':
                return "F#";
            case 'g':
                return "G";
            case 'y':
                return "G#";
            case 'h':
                return "A";
            case 'u':
                return "A#";
            case 'j':
                return "B";
            default:
                return "Pause";
        }
    }
}
