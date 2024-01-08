package com.example.studentsync.controllers;

import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static com.example.studentsync.utils.DatabaseFunctions.*;

public class LoginController extends SceneController {

    @FXML
    public TextField username = new TextField();

    @FXML
    public TextField password = new TextField();

    @FXML
    private Label error = new Label();

    @FXML
    protected void switchToLoginScreen() {
        this.changeScene(SceneIdentifier.HELLO, 450, 550);

    }

    @FXML
    protected void loginButton(ActionEvent event) throws SQLException {
        if (username.getText().isEmpty()) {
            error.setText("Please enter a username");
            error.setVisible(true);
        }
        if (password.getText().isEmpty()) {
            error.setText("Please enter a password");
            error.setVisible(true);
        } else {
            if (!enterAccount(username.getText(), password.getText())) {
                error.setText("Incorrect account information");
                error.setVisible(true);
            } else {

                if (isAccountTeacher(username.getText(), password.getText())) {
                    if (doesTeacherHaveClasses(username.getText(), password.getText())) {
                        this.changeScene(SceneIdentifier.TEACHER_VIEW,1200,700);
                    } else {
                        this.changeScene(SceneIdentifier.CREATE_CLASSROOM, 450, 550);
                    }
                }
                else
                    this.changeScene(SceneIdentifier.STUDENT_VIEW,1200,700);
            }
        }
    }
}
