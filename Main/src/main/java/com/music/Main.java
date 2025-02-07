package com.music;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.PianoView;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        Controller controller = new Controller(new Model());
        PianoView pianoView = new PianoView(controller);

        controller.setInstrument("Drum");
    }
}