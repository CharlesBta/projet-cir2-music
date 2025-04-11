package com.music;


import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.InstrumentFrame;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Model());
        controller.setInstrument("Xylophone");
        InstrumentFrame instrumentFrame = new InstrumentFrame(controller);
    }
}