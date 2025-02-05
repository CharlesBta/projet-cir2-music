package com.music;

import com.music.model.instrument.IInstrument;
import com.music.model.instrument.*;

public abstract class InstrumentFactory {
    public static IInstrument getInstrument(String instrument, int velocity) {
        return switch (instrument) {
            case "Xylophone" -> new Synthesizer(velocity, 13);
            case "Piano" -> new Synthesizer(velocity, 0);
            default -> null;
        };
    }

    public static IInstrument getInstrument(String instrument) {
        return switch (instrument) {
            case "Xylophone" -> new Synthesizer(13);
            case "Piano" -> new Synthesizer(0);
            default -> null;
        };
    }
}
