package com.music.view.piano;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.KeyboardFocusManager;

public class PianoPanel extends JLayeredPane {
    private IController controller;
    private JPanel mainPanel;
    private JPanel pianoContainer;
    private int numberOfPanels;
    private JComboBox<String> pianoSelector;
    private PianoKeyPanel activePianoPanel;
    private JLabel noteLabel;

    public PianoPanel(IController controller) {
        this.controller = controller;
        this.numberOfPanels = 1; // Initialiser avec 1 octave
    }

    public void init(){
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        controlPanel.add(addButton);
        controlPanel.add(removeButton);

        pianoSelector = new JComboBox<>();
        pianoSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateActivePianoPanel();
            }
        });
        controlPanel.add(new JLabel("Sélectionnez le piano actif:"));
        controlPanel.add(pianoSelector);

        mainPanel.add(Box.createVerticalGlue()); // Espace flexible au-dessus
        mainPanel.add(controlPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Espace fixe entre les contrôles et le piano

        pianoContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        mainPanel.add(pianoContainer);

        // Add note display label
        noteLabel = new JLabel(" ", SwingConstants.CENTER);
        noteLabel.setFont(new Font("Arial", Font.BOLD, 18));
        noteLabel.setPreferredSize(new Dimension(100, 30)); // Fixed size to prevent flickering
        noteLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add padding
        noteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(noteLabel);

        mainPanel.add(Box.createVerticalGlue()); // Espace flexible en dessous

        addPianoKeyPanels(numberOfPanels);

        // Ajouter des écouteurs aux boutons d'ajout et de suppression
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPianoKeyPanel();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePianoKeyPanel();
            }
        });

        // Utiliser un KeyEventDispatcher pour capturer les événements de clavier au niveau global
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (activePianoPanel != null) {
                    if (e.getID() == KeyEvent.KEY_PRESSED) {
                        activePianoPanel.handleKeyPress(e);
                    } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                        activePianoPanel.handleKeyRelease(e);
                    }
                }
                return false;
            }
        });

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addPianoKeyPanels(int numberOfPanels) {
        for (int i = 0; i < numberOfPanels; i++) {
            PianoKeyPanel pianoKeyPanel = new PianoKeyPanel(controller, i, this);
            pianoContainer.add(pianoKeyPanel);
            pianoSelector.addItem("Piano " + (i + 1));
        }
        updateActivePianoPanel();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addPianoKeyPanel() {
        if (numberOfPanels >= 3 ) {
            return;
        }
        PianoKeyPanel pianoKeyPanel = new PianoKeyPanel(controller, numberOfPanels, this);
        pianoContainer.add(pianoKeyPanel);
        pianoSelector.addItem("Piano " + (numberOfPanels + 1));
        updateActivePianoPanel();
        mainPanel.revalidate();
        mainPanel.repaint();
        numberOfPanels++;
    }

    private void removePianoKeyPanel() {
        if (numberOfPanels > 1) {
            pianoContainer.remove(pianoContainer.getComponentCount() - 1);
            pianoSelector.removeItemAt(pianoSelector.getItemCount() - 1);
            updateActivePianoPanel();
            mainPanel.revalidate();
            mainPanel.repaint();
            numberOfPanels--;
        }
    }

    private void updateActivePianoPanel() {
        int selectedIndex = pianoSelector.getSelectedIndex();
        for (int i = 0; i < pianoContainer.getComponentCount(); i++) {
            PianoKeyPanel pianoKeyPanel = (PianoKeyPanel) pianoContainer.getComponent(i);
            pianoKeyPanel.setActive(i == selectedIndex);
        }
        if (selectedIndex != -1) {
            activePianoPanel = (PianoKeyPanel) pianoContainer.getComponent(selectedIndex);
        }
    }

    /**
     * Updates the note label with the given note
     * @param note The note to display, or empty string to clear
     */
    public void updateNoteLabel(String note, int octave) {
        if (noteLabel != null) {
            // Use a space instead of empty string to maintain label visibility
            if (octave == -1) {
                noteLabel.setText(" ");
            }
            noteLabel.setText(note.isEmpty() ? " " : note + (octave+1));
        }
    }
}
