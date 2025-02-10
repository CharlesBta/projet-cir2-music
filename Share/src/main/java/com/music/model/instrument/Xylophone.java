package com.music.model.instrument;

public class Xylophone extends SinusAudio {

    // Variable pour stocker la note actuellement jouée
    private int currentNote = -1;

    @Override
    public void playSound(final int note) {
        if (note >= 0 && note < 8) { // Supposons que vous jouez des notes de 0 à 7
            currentNote = note;
            new Thread(() -> {
                playing = true;
                double frequency = 440.0 * Math.pow(2, (note - 3) / 12.0); // Calculer la fréquence
                byte[] buffer = generateWave(frequency, 500, "dampedSine"); // Onde amortie de 500ms
                int offset = 0;
                while (playing && offset < buffer.length) {
                    int chunkSize = Math.min(1024, buffer.length - offset); // Taille du morceau à écrire
                    line.write(buffer, offset, chunkSize); // Écrire le morceau dans la ligne audio
                    offset += chunkSize; // Mettre à jour l'offset
                }
                playing = false; // Arrêter après avoir joué le son
            }).start();
        }
    }

    @Override
    public void stopSound(int note) {
        super.stopSound(note);
        if (note == currentNote) {
            currentNote = -1;
        }
    }

    // Fonction pour générer un son de Xylophone (sinusoïdal amortie)
    protected byte[] generateWave(double frequency, int durationMs, String waveType) {
        return super.generateWave(frequency, durationMs, waveType);
    }
}