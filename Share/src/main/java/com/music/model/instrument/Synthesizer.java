package com.music.model.instrument;

import lombok.Setter;

public class Synthesizer extends MidiInstrument {
    private int idMidiInstrument = 0;

    public Synthesizer(int velocity, int idMidiInstrument) {
        super(velocity);
        this.idMidiInstrument = idMidiInstrument;
    }

    public Synthesizer(int idMidiInstrument) {
        super();
        this.idMidiInstrument = idMidiInstrument;
    }

    public Synthesizer() {
        super();
    }

    @Override
    public void setMidiInstrument() {
        this.channel.programChange(this.idMidiInstrument);
    }
}
