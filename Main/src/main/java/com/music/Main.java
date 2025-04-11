package com.music;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.Frame;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.InstrumentFrame;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model, "Piano");
        Frame frame = new Frame(controller);
    }
}