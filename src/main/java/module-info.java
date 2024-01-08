module com.example.studentsync {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.studentsync to javafx.fxml;
    exports com.example.studentsync;
    exports com.example.studentsync.controllers;
    opens com.example.studentsync.controllers to javafx.fxml;
}