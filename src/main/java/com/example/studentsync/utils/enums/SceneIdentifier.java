package com.example.studentsync.utils.enums;

public enum SceneIdentifier{
    HELLO("hello-view.fxml"),
    LOGIN("login.fxml"),
    SIGNIN("signin.fxml"),
    CREATE_CLASSROOM("createClassroom.fxml"),
    TEACHER_VIEW("teacherView.fxml"),
    STUDENT_VIEW("studentView.fxml"),
    JOIN_CLASS("joinClass.fxml");
    public final String label;

    SceneIdentifier(String label) {
        this.label = label;
    }
}
