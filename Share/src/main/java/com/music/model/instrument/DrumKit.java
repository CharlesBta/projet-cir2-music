package com.music.model.instrument;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class DrumKit implements IInstrument {
    private final Map<Integer, List<Clip>> clipPool;
    private final Map<Integer, String> drumSounds;
    private boolean isOpen;

    public DrumKit() {
        clipPool = new HashMap<>();
        drumSounds = new HashMap<>();

        // Map notes to drum sound file names
        drumSounds.put(35, "kick.wav");       // Acoustic Bass Drum
        drumSounds.put(36, "kick.wav");       // Bass Drum 1
        drumSounds.put(38, "snare.wav");      // Acoustic Snare
        drumSounds.put(40, "snare.wav");      // Electric Snare
        drumSounds.put(42, "hithat_closed.wav"); // Closed Hi-Hat
        drumSounds.put(46, "hithat_open.wav");   // Open Hi-Hat
        drumSounds.put(49, "crash.wav");      // Crash Cymbal 1
        drumSounds.put(51, "ride.wav");       // Ride Cymbal 1

        isOpen = false;
    }

    @Override
    public void playSound(final int note) {
        if (!isOpen || !drumSounds.containsKey(note)) {
            return;
        }

        try {
            // Get an available clip or create a new one
            Clip clip = getAvailableClip(note);
            if (clip != null) {
                // Reset and play the clip
                clip.setFramePosition(0);
                clip.start();

                // Add a listener to handle when the clip finishes playing
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        // Make the clip available for reuse
                        synchronized (clipPool) {
                            List<Clip> clips = clipPool.get(note);
                            if (clips != null) {
                                clips.add(clip);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip getAvailableClip(int note) {
        synchronized (clipPool) {
            // Initialize the list if it doesn't exist
            if (!clipPool.containsKey(note)) {
                clipPool.put(note, new ArrayList<>());
            }

            List<Clip> clips = clipPool.get(note);

            // Check if there's an available clip
            if (!clips.isEmpty()) {
                return clips.remove(0);
            }

            // Create a new clip if none are available
            return createNewClip(note);
        }
    }

    private Clip createNewClip(int note) {
        try {
            String soundFile = drumSounds.get(note);
            // Try multiple approaches to load the resource
            InputStream is = null;

            // Approach 1: Using class loader
            is = getClass().getClassLoader().getResourceAsStream("drums/" + soundFile);

            // Approach 2: Using absolute path
            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("/drums/" + soundFile);
            }

            // Approach 3: Using Thread context class loader
            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("drums/" + soundFile);
            }

            // Approach 4: Using system class loader
            if (is == null) {
                is = ClassLoader.getSystemResourceAsStream("drums/" + soundFile);
            }

            if (is == null) {
                System.err.println("Could not find sound file: drums/" + soundFile);
                // Print the classpath for debugging
                System.err.println("Classpath: " + System.getProperty("java.class.path"));
                return null;
            }

            System.out.println("Successfully loaded sound file: drums/" + soundFile);

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
            AudioFormat format = audioInputStream.getFormat();

            // Convert the audio stream to a supported format if necessary
            if (!AudioSystem.isConversionSupported(AudioFormat.Encoding.PCM_SIGNED, format)) {
                throw new IllegalArgumentException("Conversion not supported");
            }

            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    format.getSampleRate(),
                    16,
                    format.getChannels(),
                    format.getChannels() * 2,
                    format.getSampleRate(),
                    false
            );

            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);

            Clip clip = AudioSystem.getClip();
            clip.open(convertedStream);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void stopSound(final int note) {
        if (!isOpen || !clipPool.containsKey(note)) {
            return;
        }

        synchronized (clipPool) {
            List<Clip> clips = clipPool.get(note);
            // We can't stop clips that are currently playing because they're not in the pool
            // They'll be added back to the pool when they finish playing
        }
    }

    @Override
    public void open() {
        isOpen = true;
        // Preload clips for each note
        for (Integer note : drumSounds.keySet()) {
            synchronized (clipPool) {
                List<Clip> clips = new ArrayList<>();
                // Create a few clips for each note to handle rapid button presses
                for (int i = 0; i < 3; i++) {
                    Clip clip = createNewClip(note);
                    if (clip != null) {
                        clips.add(clip);
                    }
                }
                clipPool.put(note, clips);
            }
        }
    }

    @Override
    public void close() {
        isOpen = false;
        // Close and release all clips
        synchronized (clipPool) {
            for (List<Clip> clips : clipPool.values()) {
                for (Clip clip : clips) {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.close();
                }
                clips.clear();
            }
            clipPool.clear();
        }
    }
}
