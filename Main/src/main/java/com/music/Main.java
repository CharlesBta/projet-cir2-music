package com.music;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.PianoView;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.setInstrument("Piano");
        PianoView view = new PianoView(controller);
        view.setVisible(true);
    }
}