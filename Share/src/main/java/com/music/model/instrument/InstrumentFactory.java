package com.music.model.instrument;

/**
 * Factory class for creating instrument instances.
 * Provides methods to create different types of instruments based on their names.
 */
public abstract class InstrumentFactory {
    /**
     * Creates an instrument with the specified name and default velocity.
     * 
     * @param instrument The name of the instrument to create
     * @return The created instrument, or null if the instrument name is not recognized
     */
    public static IInstrument getInstrument(String instrument) {
        return getInstrument(instrument, null);
    }

    /**
     * Creates an instrument with the specified name and velocity.
     * 
     * @param instrument The name of the instrument to create
     * @param velocity The velocity (volume) for note playing (0-127), or null for default
     * @return The created instrument, or null if the instrument name is not recognized
     */
    public static IInstrument getInstrument(String instrument, Integer velocity) {
        return switch (instrument) {
            case "Xylophone" -> new Synthesizer(velocity, 13);
            case "Piano" -> new Synthesizer(velocity, 0);
            case "Acoustic Guitar" -> new Synthesizer(velocity, 24);
            case "Electric Bass" -> new Synthesizer(velocity, 33);
            case "Video Game" -> new VideoGame();
            case "Drum Kit" -> new DrumKit();
            case "Wood Instrument" -> new Synthesizer(velocity, 116);
            default -> null;
        };
    }
}
