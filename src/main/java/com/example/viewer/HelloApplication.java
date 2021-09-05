package com.example.viewer;

import com.example.viewer.stages.MainStage;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    private static HostServices services;
    @Override
    public void start(Stage stage) throws IOException {
        services = getHostServices();
        var mainStage = new MainStage();

        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}