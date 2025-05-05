package com.music;

import com.music.Note;
import com.music.controller.Controller;
import com.music.controller.IController;
import com.music.model.Model;
import com.music.view.Frame;
import com.music.view.Record;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model, "Piano");
        Frame frame = new Frame(controller);
    }
}