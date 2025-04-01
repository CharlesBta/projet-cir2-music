package com.music;


import com.music.view.Frame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Frame::new);
    }
}
