package com.music.model.instrument;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public abstract class MidiInstrument implements IInstrument {
    private Synthesizer synthesizer;
    MidiChannel channel;
    private int velocity;

    public MidiInstrument(int velocity) {
        try {
            this.synthesizer = MidiSystem.getSynthesizer();
            this.velocity = velocity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MidiInstrument() {
        this(90);
    }

    @Override
    public void open() {
        try {
            this.synthesizer.open();
            MidiChannel[] channels = synthesizer.getChannels();
            this.channel = channels[0];
            this.setMidiInstrument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void setMidiInstrument();

    @Override
    public void close() {
        this.channel = null;
        this.synthesizer.close();
    }

    @Override
    public void playSound(final int note) {
        if (this.channel != null) {
            this.channel.noteOn(note, this.velocity);
        }
    }

    @Override
    public void stopSound(final int note) {
        if (this.channel != null) {
            this.channel.noteOff(note);
        }
    }
}
