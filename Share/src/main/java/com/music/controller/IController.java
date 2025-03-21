package com.music.controller;

public interface IController {
    void playNote(int idOctave, String note);

    void stopNote(int idOctave, String note);

    void setInstrument(String instrument);

    void setVelocity(int velocity);
}
