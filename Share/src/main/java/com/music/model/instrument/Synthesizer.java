package com.music.model.instrument;

public class Synthesizer extends MidiInstrument{
    private int idMidiInstrument = 0;

    public Synthesizer(int velocity, Integer idMidiInstrument) {
        super(velocity);
        if(idMidiInstrument != null) this.idMidiInstrument = idMidiInstrument;
    }

    public Synthesizer(Integer idMidiInstrument) {
        super();
        if(idMidiInstrument != null) this.idMidiInstrument = idMidiInstrument;
    }

    public Synthesizer() {
        super();
    }

    @Override
    public void setMidiInstrument() {
        this.channel.programChange(this.idMidiInstrument);
    }
}
