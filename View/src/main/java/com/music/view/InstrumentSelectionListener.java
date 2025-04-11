package com.music.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstrumentSelectionListener implements ActionListener {
    private final String instrument;
    private final OuvrirPartition partitionPanel;

    public InstrumentSelectionListener(String instrument, OuvrirPartition partitionPanel) {
        this.instrument = instrument;
        this.partitionPanel = partitionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        partitionPanel.setInstrument(instrument);
    }
}
