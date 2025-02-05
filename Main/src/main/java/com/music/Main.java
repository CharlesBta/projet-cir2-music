package com.music;

import com.music.controller.Controller;
import com.music.model.Model;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        Controller controller = new Controller(new Model());

        controller.setInstrument("Electric Bass");
        controller.setOctave(-1);
        controller.playNote(4, "Si");
//        controller.playNote(4, "D");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        controller.stopNote(4, "C");
        controller.stopNote(4, "D");

    }
}