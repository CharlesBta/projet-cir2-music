package com.music.model.instrument;

public abstract class InstrumentFactory {
    public static IInstrument getInstrument(String instrument, Integer velocity) {
        return switch (instrument) {
            case "Xylophone" -> new Synthesizer(velocity, 13);
            case "Piano" -> new Synthesizer(velocity, 0);
            case "Acoustic Guitar" -> new Synthesizer(velocity, 24);
            case "Electric Bass" -> new Synthesizer(velocity, 33);
            default -> null;
        };
    }

    public static IInstrument getInstrument(String instrument) {
        return getInstrument(instrument, null);
    }
}
