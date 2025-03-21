package com.music.model.instrument;

import javax.sound.sampled.*;

public abstract class SinusAudio implements IInstrument {
    protected static final int SAMPLE_RATE = 44100;
    protected SourceDataLine line;
    protected volatile boolean playing = false;

    @Override
    public void stopSound(int note) {
        playing = false;
        if (line != null) {
            line.flush();
        }
    }

    @Override
    public void open() {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 8, 1, true, false); // Format par défaut pour les ondes carrées
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (line != null) {
            line.drain();
            line.close();
        }
    }

    protected byte[] generateWave(double frequency, int durationMs, String waveType) {
        int numSamples = (int) ((durationMs / 1000.0) * SAMPLE_RATE);
        byte[] buffer = new byte[numSamples];

        for (int i = 0; i < numSamples; i++) {
            double cyclePosition = (i * frequency / SAMPLE_RATE) % 1.0;
            switch (waveType) {
                case "square":
                    buffer[i] = (byte) (cyclePosition < 0.5 ? 127 : -127); // Onde carrée
                    break;
                case "sine":
                    double angle = 2.0 * Math.PI * cyclePosition;
                    buffer[i] = (byte) (Math.sin(angle) * 127); // Onde sinusoïdale
                    break;
                case "dampedSine":
                    double dampedAngle = 2.0 * Math.PI * cyclePosition;
                    double envelope = Math.exp(-3.0 * i / numSamples); // Atténuation exponentielle
                    buffer[i] = (byte) (Math.sin(dampedAngle) * envelope * 127); // Onde sinusoïdale amortie
                    break;
            }
        }

        return buffer;
    }
}
