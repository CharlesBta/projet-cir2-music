package com.music.controller;

import com.music.NoteOffsetCalculator;
import com.music.model.instrument.InstrumentFactory;
import com.music.model.IModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Implementation of the IController interface that manages the interaction between
 * the user interface and the model in the MVC architecture.
 * This controller handles musical operations like playing notes and changing instruments.
 */
public class Controller implements IController {
    /** The model that this controller interacts with */
    private final IModel model;

    /** The base octave for note playing (default: 5) */
    @Setter
    @Getter
    private int octave;

    /** The velocity (volume) for note playing (0-127, default: 64) */
    @Setter
    private int velocity;

    /** Flag indicating if the application is in recording mode */
    private boolean isRecording = false;

    /** Flag indicating if the application is in saving mode */
    private boolean isSaving = false;

    /**
     * Constructs a new Controller with the specified model and default instrument.
     *
     * @param model The model to control
     * @param defaultInstrument The name of the default instrument to use
     */
    public Controller(final IModel model, final String defaultInstrument) {
        this.model = model;
        this.octave = 5;
        this.velocity = 64;
        this.model.setInstrument(InstrumentFactory.getInstrument(defaultInstrument, this.velocity));
        isSaving = false;
    }

    /**
     * Plays a musical note with the current instrument.
     * Converts the note name to a MIDI offset and calculates the final note value
     * based on the octave parameters.
     * 
     * @param idOctave The octave offset for the note
     * @param note The note to play (e.g., "C", "D#", "F")
     */
    @Override
    public void playNote(int idOctave, String note) {
        Integer noteOffset = NoteOffsetCalculator.convertToOffset(note);
        if (noteOffset != null) {
            this.model.playNote(
                    noteOffset + (idOctave + this.octave) * 12
            );
        }
    }

    /**
     * Stops playing a musical note.
     * Converts the note name to a MIDI offset and calculates the final note value
     * based on the octave parameters.
     * 
     * @param idOctave The octave offset for the note
     * @param note The note to stop (e.g., "C", "D#", "F")
     */
    @Override
    public void stopNote(int idOctave, String note) {
        Integer noteOffset = NoteOffsetCalculator.convertToOffset(note);
        if (noteOffset != null) {
            this.model.stopNote(
                    noteOffset + (idOctave + this.octave) * 12
            );
        }
    }

    /**
     * Changes the current instrument.
     * Closes the current instrument, creates a new one with the specified name,
     * and opens it for use.
     * 
     * @param instrument The name of the instrument to use
     */
    @Override
    public void setInstrument(String instrument) {
        this.model.close();
        this.model.setInstrument(InstrumentFactory.getInstrument(instrument, this.velocity));
        this.model.open();
    }

    /**
     * Checks if the application is in saving mode.
     * 
     * @return true if in saving mode, false otherwise
     */
    @Override
    public boolean isSaving() {
        return this.isSaving;
    }

    /**
     * Sets the saving mode.
     * 
     * @param isSaving true to enable saving mode, false to disable
     */
    @Override
    public void setIsSaving(final boolean isSaving) {
        this.isSaving = isSaving;
    }

    /**
     * Checks if the application is in recording mode.
     * 
     * @return true if in recording mode, false otherwise
     */
    @Override
    public boolean isRecording(){
        return this.isRecording;
    }

    /**
     * Sets the recording mode.
     * 
     * @param isRecording true to enable recording mode, false to disable
     */
    @Override
    public void setIsRecording(final boolean isRecording) {
        this.isRecording = isRecording;
    }
}
