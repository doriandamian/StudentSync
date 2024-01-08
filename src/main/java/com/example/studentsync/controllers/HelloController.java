package com.example.studentsync.controllers;

import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.fxml.FXML;

public class HelloController extends SceneController {

    @FXML
    protected void switchSceneToLogin(){
        this.changeScene(SceneIdentifier.LOGIN, 600, 400);
    }

    @FXML
    protected void switchSceneToSignIn(){
        this.changeScene(SceneIdentifier.SIGNIN, 1000, 620);
    }

}