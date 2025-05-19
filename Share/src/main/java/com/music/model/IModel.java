package com.music.model;

import com.music.model.instrument.IInstrument;

/**
 * Interface defining the model component of the MVC architecture for the music application.
 * The model handles the business logic for playing musical notes and managing instruments.
 */
public interface IModel {
    /**
     * Plays a musical note with the current instrument.
     * 
     * @param note The MIDI note number to play
     */
    void playNote(final int note);

    /**
     * Stops playing a musical note.
     * 
     * @param note The MIDI note number to stop
     */
    void stopNote(final int note);

    /**
     * Sets the current instrument for playing notes.
     * 
     * @param instrument The instrument to use
     */
    void setInstrument(IInstrument instrument);

    /**
     * Opens the current instrument for use.
     * This should be called before attempting to play notes.
     */
    void open();

    /**
     * Closes the current instrument.
     * This should be called when the instrument is no longer needed.
     */
    void close();
}
