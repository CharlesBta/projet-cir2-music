package com.music;


import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.ConverterTxtToJson;
import com.music.view.PianoView;
import com.music.view.ReaderJson;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);

        controller.setInstrument("Piano");

        PianoView pianoView = new PianoView(controller);
        pianoView.setVisible(true);

    }
}
