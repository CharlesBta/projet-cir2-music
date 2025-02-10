package com.music.model;

import com.music.model.instrument.IInstrument;

public class Model implements IModel {
    private IInstrument instrument;

    public Model(final IInstrument instrument) {
        this.instrument = instrument;
        this.open();
    }

    public Model() {
        this.instrument = null;
    }

    @Override
    public void playNote(final int note) {
        this.instrument.playSound(note);
    }

    @Override
    public void stopNote(final int note) {
        this.instrument.stopSound(note);
    }

    @Override
    public void setInstrument(IInstrument instrument) {
        this.instrument = instrument;
    }

    @Override
    public void open() {
        if (this.instrument != null) this.instrument.open();
    }

    @Override
    public void close() {
        if (this.instrument != null) this.instrument.close();
    }
}
