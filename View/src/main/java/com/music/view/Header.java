package com.music.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.music.controller.IController;
import com.music.view.piano.PianoPanel;
import com.music.view.videogame.BitPanel;
import com.music.view.xylophone.XylophonePanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Header {

    @Getter
    private JPanel headerPanel;
    private JPanel controlPanel;
    private Frame frame;
    private IController controller;

    public Header(Frame frame, IController controller) {
        this.frame = frame;
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setLookAndFeel();
        createHeaderPanel();

    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(Color.WHITE);

        JButton headerButton = createStyledButton("Instruments");
        headerPanel.add(headerButton);
        addPopupMenuToButton(headerButton, new String[]{"Piano", "Xylophone", "Video Game"});

        JButton openButton = createStyledButton("Ouvrir");
        openButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Utiliser FileChooserMouseAdapter pour gérer l'action d'ouverture
                new FileChooserMouseAdapter(frame, controller).mouseClicked(e);

                // Masquer le menu et afficher un autre contenu
                JLayeredPane newContent = new JLayeredPane();
                newContent.setBackground(Color.WHITE);
                newContent.add(new JLabel("Ouvrir Partition", SwingConstants.CENTER));
                frame.updateFrameContent(newContent);
            }
        });
        headerPanel.add(openButton);

        JButton menuButton = createStyledButton("Menu");
        menuButton.addActionListener(e -> {
            Menu menu = new Menu();
            frame.updateFrameContent(menu);
        });
        headerPanel.add(menuButton);

        JButton quitButton = createStyledButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        headerPanel.add(quitButton);


    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        styleAsButton(button);
        return button;
    }

    private void addPopupMenuToButton(JButton button, String[] items) {
        JPopupMenu popupMenu = new JPopupMenu();
        for (String item : items) {
            JMenuItem menuItem = new JMenuItem(item);
            menuItem.addActionListener(e -> {
                controller.setInstrument(item);
                switch (item) {
                    case "Piano":
                        PianoPanel pianoPanel = new PianoPanel(controller);
                        pianoPanel.init();
                        frame.updateFrameContent(pianoPanel);
                        break;
                    case "Xylophone":
                        XylophonePanel xylophonePanel = new XylophonePanel(controller);
                        xylophonePanel.init();
                        frame.updateFrameContent(xylophonePanel);
                        break;
                    case "Video Game":
                        BitPanel bitPanel = new BitPanel(controller);
                        bitPanel.init();
                        frame.updateFrameContent(bitPanel);
                        break;
                }
            });
            popupMenu.add(menuItem);
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popupMenu.show(button, 0, button.getHeight());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.LIGHT_GRAY);
            }
        });
    }

    private static void styleAsButton(JComponent component) {
        component.setOpaque(true);
        component.setBackground(Color.LIGHT_GRAY);
        component.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        component.setPreferredSize(new Dimension(130, 30));
        if (component instanceof AbstractButton) {
            ((AbstractButton) component).setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

}
