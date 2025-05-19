package com.music.model.instrument;

/**
 * Implementation of a synthesizer instrument using MIDI.
 * This class allows playing different MIDI instruments by specifying the MIDI program number.
 */
public class Synthesizer extends MidiInstrument {
    /** The MIDI program (instrument) number to use */
    private int idMidiInstrument = 0;

    /**
     * Constructs a new Synthesizer with the specified velocity and MIDI instrument.
     * 
     * @param velocity The velocity (volume) for note playing (0-127)
     * @param idMidiInstrument The MIDI program number (0-127) or null for default (0)
     */
    public Synthesizer(int velocity, Integer idMidiInstrument) {
        super(velocity);
        if(idMidiInstrument != null) this.idMidiInstrument = idMidiInstrument;
    }

    /**
     * Constructs a new Synthesizer with default velocity and the specified MIDI instrument.
     * 
     * @param idMidiInstrument The MIDI program number (0-127) or null for default (0)
     */
    public Synthesizer(Integer idMidiInstrument) {
        super();
        if(idMidiInstrument != null) this.idMidiInstrument = idMidiInstrument;
    }

    /**
     * Constructs a new Synthesizer with default velocity and MIDI instrument (0).
     */
    public Synthesizer() {
        super();
    }

    /**
     * Sets the MIDI instrument to use.
     * Changes the MIDI program (instrument) on the channel.
     */
    @Override
    public void setMidiInstrument() {
        this.channel.programChange(this.idMidiInstrument);
    }
}
