package com.music.controller;

import com.music.NoteOffsetCalculator;
import com.music.model.instrument.InstrumentFactory;
import com.music.model.IModel;
import lombok.Getter;
import lombok.Setter;

public class Controller implements IController {
    private final IModel model;
    @Setter
    @Getter
    private int octave;
    @Setter
    private int velocity;

    public Controller(final IModel model, final String defaultInstrument) {
        this.model = model;
        this.octave = 5;
        this.velocity = 64;
        this.model.setInstrument(InstrumentFactory.getInstrument(defaultInstrument, this.velocity));
    }

    @Override
    public void playNote(int idOctave, String note) {
        Integer noteOffset = NoteOffsetCalculator.convertToOffset(note);
        if (noteOffset != null) {
            this.model.playNote(
                    noteOffset + (idOctave + this.octave) * 12
            );
        }
    }

    @Override
    public void stopNote(int idOctave, String note) {
        Integer noteOffset = NoteOffsetCalculator.convertToOffset(note);
        if (noteOffset != null) {
            this.model.stopNote(
                    noteOffset + (idOctave + this.octave) * 12
            );
        }
    }

    @Override
    public void setInstrument(String instrument) {
        this.model.close();
        this.model.setInstrument(InstrumentFactory.getInstrument(instrument, this.velocity));
        this.model.open();
    }

}
