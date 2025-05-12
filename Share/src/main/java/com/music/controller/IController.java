package com.music.controller;

import com.music.model.instrument.IInstrument;

public interface IController {
    void playNote(int idOctave, String note);
    void stopNote(int idOctave, String note);
    void setInstrument(String instrument);

    void setOctave(int octave);
    int getOctave();
    void setVelocity(int velocity);
    boolean isSaving();
    void setIsSaving(boolean isSaving);
    void setIsRecording(boolean isRecording);
    boolean isRecording();
}
