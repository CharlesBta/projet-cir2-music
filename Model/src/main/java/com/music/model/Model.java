package com.music.model;

import com.music.model.instrument.IInstrument;

/**
 * Implementation of the IModel interface that manages the musical instruments
 * and provides methods to play and stop notes.
 * This class is part of the model component in the MVC architecture.
 */
public class Model implements IModel {
    /** The current instrument used for playing notes */
    private IInstrument instrument;

    /**
     * Constructs a new Model with the specified instrument.
     * The instrument is automatically opened for use.
     *
     * @param instrument The instrument to use
     */
    public Model(final IInstrument instrument) {
        this.instrument = instrument;
        this.open();
    }

    /**
     * Constructs a new Model without an instrument.
     * An instrument must be set using setInstrument() before playing notes.
     */
    public Model() {
        this.instrument = null;
    }

    /**
     * Plays a musical note with the current instrument.
     * Delegates to the instrument's playSound method.
     * 
     * @param note The MIDI note number to play
     */
    @Override
    public void playNote(final int note) {
        this.instrument.playSound(note);
    }

    /**
     * Stops playing a musical note.
     * Delegates to the instrument's stopSound method.
     * 
     * @param note The MIDI note number to stop
     */
    @Override
    public void stopNote(final int note) {
        this.instrument.stopSound(note);
    }

    /**
     * Sets the current instrument for playing notes.
     * 
     * @param instrument The instrument to use
     */
    @Override
    public void setInstrument(IInstrument instrument) {
        this.instrument = instrument;
    }

    /**
     * Opens the current instrument for use.
     * This should be called before attempting to play notes.
     * Does nothing if no instrument is set.
     */
    @Override
    public void open() {
        if (this.instrument != null) this.instrument.open();
    }

    /**
     * Closes the current instrument.
     * This should be called when the instrument is no longer needed.
     * Does nothing if no instrument is set.
     */
    @Override
    public void close() {
        if (this.instrument != null) this.instrument.close();
    }
}
