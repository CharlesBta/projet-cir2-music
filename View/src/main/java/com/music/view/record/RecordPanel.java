package com.music.view.record;

import com.music.controller.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RecordPanel extends JLayeredPane {
    private static final int ICON_WIDTH = 50;
    private static final int ICON_HEIGHT = 50;
    private static final int IMAGE_BUTTON_WIDTH = 50;
    private static final int IMAGE_BUTTON_HEIGHT = 50;
    private static final Color BUTTON_COLOR = new Color(255, 255, 255);
    private static final Color HOVER_COLOR = new Color(255, 255, 255);
    private static final Color RECORDING_INDICATOR_COLOR = new Color(255, 0, 0);
    private Record record;
    private IController controller;
    private JLabel recordingIndicator;

    public RecordPanel(IController controller) {
        setLayout(new BorderLayout());
        this.controller = controller;
        record = new Record(controller, this);
        JButton recordButton = createStyledButtonWithIcon("/record.png", ICON_WIDTH, ICON_HEIGHT, "Record");
        JButton stopButton = createStyledButtonWithIcon("/stop.png", ICON_WIDTH, ICON_HEIGHT, "Stop");

        // Create recording indicator
        recordingIndicator = new JLabel("● RECORDING");
        recordingIndicator.setForeground(RECORDING_INDICATOR_COLOR);
        recordingIndicator.setFont(new Font(recordingIndicator.getFont().getName(), Font.BOLD, 14));
        recordingIndicator.setHorizontalAlignment(SwingConstants.CENTER);
        recordingIndicator.setAlignmentX(Component.CENTER_ALIGNMENT); // Center horizontally in BoxLayout
        recordingIndicator.setVisible(controller.isRecording());

        recordButton.addActionListener(
                e -> {
                    System.out.println("Record button clicked");
                    record.record();
                    updateRecordingIndicator();
                }
        );

        stopButton.addActionListener(
                e -> {
                    System.out.println("Stop button clicked");
                    record.stop();
                    updateRecordingIndicator();
                }
        );

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS)); // Stack buttons vertically
        controlPanel.add(recordButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add some space between buttons
        controlPanel.add(stopButton);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space before indicator
        controlPanel.add(recordingIndicator);

        // Add padding to the right to shift buttons to the left
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        controlPanel.setBackground(Color.WHITE);

        add(controlPanel, BorderLayout.WEST);
    }

    private JButton createStyledButtonWithIcon(String path, int width, int height, String tooltip) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JButton button = new JButton(scaledIcon);

        button.setOpaque(true);
        button.setBackground(BUTTON_COLOR);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setPreferredSize(new Dimension(IMAGE_BUTTON_WIDTH, IMAGE_BUTTON_HEIGHT));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setToolTipText(tooltip);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    private void updateRecordingIndicator() {
        recordingIndicator.setVisible(controller.isRecording());
    }
}
