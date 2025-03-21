package com.music;

import lombok.Getter;

@Getter
public class Note {
    @Getter
    int octave;
    String note;

    public Note(int octave, String note) {
        this.octave = octave;
        this.note = note;
    }
}
