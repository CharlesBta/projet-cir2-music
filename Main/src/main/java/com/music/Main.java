package com.music;

import com.music.controller.Controller;
import com.music.model.Model;
import com.music.view.ConverterTxtToJson;
import com.music.view.ReaderJson;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);

        controller.setInstrument("Piano");

        ConverterTxtToJson converter = new ConverterTxtToJson();
        converter.setFilePath("/Users/charlesbta/Documents/GitHub/projet-cir2-music/Sujet/bella_ciao.txt");


        ReaderJson playJson = new ReaderJson(controller);
//        playJson.setJsonString(converter.convertFileToJsonString());
        playJson.setFilePath("/Users/charlesbta/Documents/GitHub/projet-cir2-music/Sujet/mario.json");


        playJson.play();
    }
}

