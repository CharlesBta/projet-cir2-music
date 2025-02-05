package com.music.model.instrument;

public interface IInstrument {
    void playSound(final int note);
    void stopSound(final int note);

    void open();
    void close();
}
