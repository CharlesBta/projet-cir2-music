package com.music.view;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import com.music.controller.IController;
import lombok.Getter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Header {

    @Getter
    private JPanel headerPanel;
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
        addPopupMenuToButton(headerButton, new String[]{"Piano", "Xylophone", "Guitare"});

        JButton openButton = createStyledButton("Ouvrir");
        openButton.addMouseListener(new FileChooserMouseAdapter(frame, controller));
        headerPanel.add(openButton);

        JButton menuButton = createStyledButton("Menu");
        menuButton.addActionListener(e -> System.out.println("menu"));
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
            menuItem.addActionListener(e -> controller.setInstrument(item));
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
