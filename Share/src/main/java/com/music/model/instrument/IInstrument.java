package com.music.model.instrument;

/**
 * Interface defining the contract for musical instruments in the application.
 * All instruments must implement methods to play and stop sounds, and to open and close the instrument.
 */
public interface IInstrument {
    /**
     * Plays a musical note with the instrument.
     * 
     * @param note The MIDI note number to play
     */
    void playSound(final int note);

    /**
     * Stops playing a musical note.
     * 
     * @param note The MIDI note number to stop
     */
    void stopSound(final int note);

    /**
     * Opens the instrument for use.
     * This should be called before attempting to play notes.
     */
    void open();

    /**
     * Closes the instrument.
     * This should be called when the instrument is no longer needed.
     */
    void close();
}
