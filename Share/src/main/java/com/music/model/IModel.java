package com.music.model;

import com.music.model.instrument.IInstrument;

public interface IModel {
    void playNote(final int note);
    void stopNote(final int note);

    void setInstrument(IInstrument instrument);

    void open();
    void close();
}
