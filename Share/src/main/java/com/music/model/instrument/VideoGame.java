package com.music.model.instrument;

public class VideoGame extends SinusAudio {
    @Override
    public void playSound(final int note) {
        new Thread(() -> {
            playing = true;
            double frequency = 110.0 * Math.pow(2, (note - 49) / 12.0);
            byte[] buffer = generateWave(frequency, 200, "square"); // Onde carrée de 200ms
            int offset = 0;
            while (playing && offset < buffer.length) {
                int chunkSize = Math.min(1024, buffer.length - offset);
                line.write(buffer, offset, chunkSize);
                offset += chunkSize;
            }
        }).start();
    }
}
