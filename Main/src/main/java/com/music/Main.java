package com.music;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.Frame;

/**
 * Main entry point for the music application.
 * Initializes the MVC components (Model, Controller, and View) to start the application.
 */
public class Main {
    /**
     * Main method that creates and connects the MVC components.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model, "Piano");
        Frame frame = new Frame(controller);
    }
}
