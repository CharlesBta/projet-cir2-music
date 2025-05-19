package com.music.model.instrument;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of a drum kit instrument that uses audio samples.
 * This class loads and plays drum sound samples for different MIDI note numbers.
 * It manages a pool of audio clips for efficient playback of drum sounds.
 */
public class DrumKit implements IInstrument {
    private static final Logger LOGGER = Logger.getLogger(DrumKit.class.getName());

    /** Pool of audio clips for each note, allowing multiple simultaneous playback */
    private final Map<Integer, List<Clip>> clipPool;

    /** Mapping of MIDI note numbers to drum sound file names */
    private final Map<Integer, String> drumSounds;

    /** Flag indicating if the instrument is open and ready to play */
    private boolean isOpen;

    /**
     * Constructs a new DrumKit with default drum sound mappings.
     * The drum kit is initially closed and must be opened before use.
     */
    public DrumKit() {
        clipPool = new HashMap<>();
        drumSounds = new HashMap<>();

        // Map MIDI note numbers to drum sound file names
        drumSounds.put(35, "kick.wav");          // Acoustic Bass Drum
        drumSounds.put(36, "kick.wav");          // Bass Drum 1
        drumSounds.put(38, "snare.wav");         // Acoustic Snare
        drumSounds.put(40, "snare.wav");         // Electric Snare
        drumSounds.put(42, "hithat_closed.wav"); // Closed Hi-Hat
        drumSounds.put(46, "hithat_open.wav");   // Open Hi-Hat
        drumSounds.put(49, "crash.wav");         // Crash Cymbal 1
        drumSounds.put(51, "ride.wav");          // Ride Cymbal 1

        isOpen = false;
    }

    /**
     * Plays a drum sound corresponding to the specified MIDI note number.
     * Gets an available audio clip from the pool and plays it.
     * 
     * @param note The MIDI note number to play
     */
    @Override
    public void playSound(final int note) {
        if (!isOpen || !drumSounds.containsKey(note)) {
            return;
        }

        try {
            Clip clip = getAvailableClip(note);
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();

                // Add a listener to return the clip to the pool when it finishes playing
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
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
            LOGGER.log(Level.SEVERE, "Error playing drum sound for note " + note, e);
        }
    }

    /**
     * Gets an available audio clip for the specified note from the pool.
     * If no clip is available, creates a new one.
     * 
     * @param note The MIDI note number
     * @return An audio clip ready to play, or null if creation failed
     */
    private Clip getAvailableClip(int note) {
        synchronized (clipPool) {
            if (!clipPool.containsKey(note)) {
                clipPool.put(note, new ArrayList<>());
            }

            List<Clip> clips = clipPool.get(note);

            if (!clips.isEmpty()) {
                return clips.remove(0);
            }

            return createNewClip(note);
        }
    }

    /**
     * Creates a new audio clip for the specified note.
     * Loads the corresponding sound file and prepares it for playback.
     * 
     * @param note The MIDI note number
     * @return A new audio clip, or null if creation failed
     */
    private Clip createNewClip(int note) {
        try {
            String soundFile = drumSounds.get(note);
            InputStream is = null;

            // Try different approaches to load the resource
            is = getClass().getClassLoader().getResourceAsStream("drums/" + soundFile);

            if (is == null) {
                is = getClass().getClassLoader().getResourceAsStream("/drums/" + soundFile);
            }

            if (is == null) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream("drums/" + soundFile);
            }

            if (is == null) {
                is = ClassLoader.getSystemResourceAsStream("drums/" + soundFile);
            }

            if (is == null) {
                LOGGER.log(Level.WARNING, "Could not find sound file: drums/" + soundFile);
                return null;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(is);
            AudioFormat format = audioInputStream.getFormat();

            // Convert the audio stream to a supported format if necessary
            if (!AudioSystem.isConversionSupported(AudioFormat.Encoding.PCM_SIGNED, format)) {
                throw new IllegalArgumentException("Audio format conversion not supported");
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
            LOGGER.log(Level.SEVERE, "Error creating audio clip for note " + note, e);
            return null;
        }
    }

    /**
     * Stops playing a drum sound.
     * Note: This implementation doesn't actually stop currently playing clips
     * because they are not in the pool while playing. They will automatically
     * stop when they finish playing and return to the pool.
     * 
     * @param note The MIDI note number to stop
     */
    @Override
    public void stopSound(final int note) {
        if (!isOpen || !clipPool.containsKey(note)) {
            return;
        }

        synchronized (clipPool) {
            // We can't stop clips that are currently playing because they're not in the pool
            // They'll be added back to the pool when they finish playing
        }
    }

    /**
     * Opens the drum kit for use.
     * Preloads audio clips for each drum sound to minimize latency during playback.
     */
    @Override
    public void open() {
        isOpen = true;

        // Preload clips for each note
        for (Integer note : drumSounds.keySet()) {
            synchronized (clipPool) {
                List<Clip> clips = new ArrayList<>();

                // Create multiple clips for each note to handle rapid button presses
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

    /**
     * Closes the drum kit.
     * Stops and releases all audio clips to free up resources.
     */
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
