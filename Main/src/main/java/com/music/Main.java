package com.music;


import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.ConverterTxtToJson;
import com.music.view.PianoView;
import com.music.view.ReaderJson;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Model());
        PianoView pianoView = new PianoView(controller);
        controller.setInstrument("Piano");


    }
}