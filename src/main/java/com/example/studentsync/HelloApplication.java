package com.example.studentsync;

import com.example.studentsync.utils.ApplicationHandler;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        ApplicationHandler.getInstance().startApplication(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}