package com.music.controller;

import com.music.model.instrument.IInstrument;

/**
 * Interface defining the controller component of the MVC architecture for the music application.
 * The controller handles user interactions and updates the model accordingly.
 */
public interface IController {
    /**
     * Plays a musical note with the current instrument.
     * 
     * @param idOctave The octave offset for the note
     * @param note The note to play (e.g., "C", "D#", "F")
     */
    void playNote(int idOctave, String note);

    /**
     * Stops playing a musical note.
     * 
     * @param idOctave The octave offset for the note
     * @param note The note to stop (e.g., "C", "D#", "F")
     */
    void stopNote(int idOctave, String note);

    /**
     * Changes the current instrument.
     * 
     * @param instrument The name of the instrument to use
     */
    void setInstrument(String instrument);

    /**
     * Sets the base octave for note playing.
     * 
     * @param octave The octave value to set
     */
    void setOctave(int octave);

    /**
     * Gets the current base octave.
     * 
     * @return The current octave value
     */
    int getOctave();

    /**
     * Sets the velocity (volume) for note playing.
     * 
     * @param velocity The velocity value to set (0-127)
     */
    void setVelocity(int velocity);

    /**
     * Checks if the application is in saving mode.
     * 
     * @return true if in saving mode, false otherwise
     */
    boolean isSaving();

    /**
     * Sets the saving mode.
     * 
     * @param isSaving true to enable saving mode, false to disable
     */
    void setIsSaving(boolean isSaving);

    /**
     * Sets the recording mode.
     * 
     * @param isRecording true to enable recording mode, false to disable
     */
    void setIsRecording(boolean isRecording);

    /**
     * Checks if the application is in recording mode.
     * 
     * @return true if in recording mode, false otherwise
     */
    boolean isRecording();
}
