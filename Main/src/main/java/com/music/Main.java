package com.music;


import com.music.controller.Controller;
import com.music.model.Model;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Model());
        controller.setInstrument("Piano");
    }
}