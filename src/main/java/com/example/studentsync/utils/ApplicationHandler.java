package com.example.studentsync.utils;

import com.example.studentsync.HelloApplication;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.example.studentsync.utils.DatabaseFunctions.getClassName;
import static com.example.studentsync.utils.DatabaseFunctions.getTeacherClasses;

public class ApplicationHandler {
    private final HashMap<SceneIdentifier, Pane> views = new HashMap<>();
    private Stage stage;

    private ApplicationHandler() {}

    public void startApplication(Stage stage){
        this.initializeViews();

        this.stage = stage;
        this.stage.setTitle(Environment.APP_TITLE);
        this.stage.setFullScreen(Environment.IS_FULLSCREEN);
        this.stage.setScene(new Scene(this.views.get(SceneIdentifier.HELLO)));
        this.stage.setResizable(false);
        this.stage.show();

        Logger.info("Application started..");
    }

    public void changeScene(SceneIdentifier newScene, int width, int height){
        this.stage.getScene().setRoot(views.get(newScene));
        this.stage.setResizable(false);
        this.stage.setMaxHeight(height);
        this.stage.setMaxWidth(width);
        this.stage.setMinHeight(height);
        this.stage.setMinWidth(width);
    }

    public void closeStage(){
        this.stage.close();
    }


    public void closeApplication(){
        Platform.exit();
        System.exit(0);
    }

    private void initializeViews() {
        try {
            for (SceneIdentifier value : SceneIdentifier.values()) {
                this.views.put(value, FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(value.label))));
            }
        } catch (IOException | NullPointerException exception) {
            Logger.error("Could not initialize views. Please check the resources folder.");
            this.closeApplication();
        }
    }

    public static ApplicationHandler _instance = null;

    public static ApplicationHandler getInstance() {
        if(ApplicationHandler._instance == null){
            ApplicationHandler._instance = new ApplicationHandler();
        }

        return ApplicationHandler._instance;
    }
}
