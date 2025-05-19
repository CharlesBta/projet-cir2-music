package com.music.model.instrument;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for MIDI-based instruments.
 * Provides common functionality for playing and stopping sounds using the Java MIDI API.
 * Subclasses must implement the setMidiInstrument method to specify the MIDI instrument to use.
 */
public abstract class MidiInstrument implements IInstrument {
    private static final Logger LOGGER = Logger.getLogger(MidiInstrument.class.getName());

    /** The MIDI synthesizer used to generate sounds */
    private Synthesizer synthesizer;

    /** The MIDI channel used to play notes */
    protected MidiChannel channel;

    /** The velocity (volume) for note playing (0-127) */
    private int velocity;

    /**
     * Constructs a new MidiInstrument with the specified velocity.
     * 
     * @param velocity The velocity (volume) for note playing (0-127)
     */
    public MidiInstrument(int velocity) {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            this.velocity = velocity;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize MIDI synthesizer", e);
        }
    }

    /**
     * Constructs a new MidiInstrument with default velocity (90).
     */
    public MidiInstrument() {
        this(90);
    }

    /**
     * Opens the instrument for use.
     * Initializes the MIDI synthesizer and channel, and sets the MIDI instrument.
     */
    @Override
    public void open() {
        try {
            this.synthesizer.open();
            MidiChannel[] channels = synthesizer.getChannels();
            this.channel = channels[0];
            this.setMidiInstrument();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to open MIDI instrument", e);
        }
    }

    /**
     * Sets the specific MIDI instrument to use.
     * This method must be implemented by subclasses to specify the MIDI program (instrument) number.
     */
    public abstract void setMidiInstrument();

    /**
     * Closes the instrument.
     * Releases the MIDI channel and closes the synthesizer.
     */
    @Override
    public void close() {
        this.channel = null;
        this.synthesizer.close();
    }

    /**
     * Plays a musical note with the instrument.
     * Uses the MIDI channel to turn on a note with the specified velocity.
     * 
     * @param note The MIDI note number to play
     */
    @Override
    public void playSound(final int note) {
        if (this.channel != null) {
            this.channel.noteOn(note, this.velocity);
        }
    }

    /**
     * Stops playing a musical note.
     * Uses the MIDI channel to turn off a note.
     * 
     * @param note The MIDI note number to stop
     */
    @Override
    public void stopSound(final int note) {
        if (this.channel != null) {
            this.channel.noteOff(note);
        }
    }
}
