package com.example.studentsync.controllers;

import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.studentsync.utils.DatabaseFunctions.createClassroom;
import static com.example.studentsync.utils.DatabaseFunctions.doesClassroomNameExists;

public class CreateClassroomController extends  SceneController{

    @FXML
    private Label errorLabel = new Label();

    @FXML
    private TextField className = new TextField();

    @FXML
    private TextField password = new TextField();

    @FXML
    private DatePicker startDate = new DatePicker();

    @FXML
    private DatePicker endDate = new DatePicker();

    @FXML
    private void returnToWelcome(){
        this.changeScene(SceneIdentifier.HELLO, 450, 550);
    }

    @FXML
    private void createClassroomButton(ActionEvent event) throws SQLException {
        if(className.getText().isEmpty()){
            errorLabel.setText("Please enter a class name");
            errorLabel.setVisible(true);
        }
        else if(password.getText().isEmpty()){
            errorLabel.setText("Please enter a password");
            errorLabel.setVisible(true);
        }
        else if(startDate.getValue() == null) {
            errorLabel.setText("Please enter a start date");
            errorLabel.setVisible(true);
        }
        else if(endDate.getValue() == null) {
            errorLabel.setText("Please enter a end date");
            errorLabel.setVisible(true);
        } else if (doesClassroomNameExists(className.getText())) {
            errorLabel.setText("Classroom name already exists");
            errorLabel.setVisible(true);
        } else{
            try {
                createClassroom(className.getText(), password.getText(), startDate.getValue(), endDate.getValue());
                this.changeScene(SceneIdentifier.TEACHER_VIEW, 1200, 700);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
