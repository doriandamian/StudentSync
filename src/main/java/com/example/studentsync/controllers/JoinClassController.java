package com.example.studentsync.controllers;

import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

import static com.example.studentsync.utils.DatabaseFunctions.doesClassroomExists;
import static com.example.studentsync.utils.DatabaseFunctions.joinClassroom;

public class JoinClassController extends SceneController {

    @FXML
    TextField className;
    @FXML
    TextField password;
    @FXML
    Label errorLabel;

    @FXML
    private void returnToStudentView() {
        this.changeScene(SceneIdentifier.STUDENT_VIEW,1200,700);
    }

    @FXML
    private void joinClassroomButton() throws SQLException {
        if(doesClassroomExists(className.getText(), password.getText())){
            joinClassroom(className.getText());
            this.changeScene(SceneIdentifier.STUDENT_VIEW,1200,700);
        }
        else{
            errorLabel.setText("Classroom does not exist");
            errorLabel.setVisible(true);
        }
    }
}
