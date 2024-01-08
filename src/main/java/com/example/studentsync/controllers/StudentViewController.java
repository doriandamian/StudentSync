package com.example.studentsync.controllers;

import com.example.studentsync.utils.enums.SceneIdentifier;
import com.example.studentsync.utils.SceneController;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.example.studentsync.utils.DatabaseFunctions.*;

public class StudentViewController extends SceneController {

    @FXML
    private Label attendanceLabel;
    @FXML
    private Label gradesLabel;
    @FXML
    private Label classMessagesLabel;
    @FXML
    private VBox classesVbox;
    @FXML
    private VBox messageVbox;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView studentTable;
    @FXML
    private TableView gradesTable;

    private Integer studentId;
    private Integer selectedClass;
    private Integer classNumberOfDays;
    List<Integer> studentGrades;
    List<Integer> studentAttendance;
    private HashMap<Timestamp, String> messageList = null;


    static Font customFont = new Font("Kollektif Bold",20);
    static Font customTimestampFont = new Font("Kollektif Bold",14);


    @FXML
    private void loadButtonAction() throws SQLException {
        List<Integer> classes = getStudentClasses();
        studentId = getAccountID();
        classesVbox.getChildren().clear();
        for(Integer aClass : classes) {
            System.out.println("Loading class");
            Button classButton = new Button();
            classButton.setText(getClassName(aClass));
            classButton.setFont(customFont);
            classButton.setTextFill(Color.web("#004aad"));
            classButton.setOnAction(event ->{
                attendanceLabel.setVisible(true);
                gradesLabel.setVisible(true);
                classMessagesLabel.setVisible(true);
                messageVbox.getChildren().clear();
                studentTable.getColumns().clear();
                gradesTable.getColumns().clear();
                this.selectedClass = aClass;
                TableColumn<ObservableList<String>, String> studentNameColumn = new TableColumn<>("Student");
                studentNameColumn.setPrefWidth(200);
                studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
                studentTable.getColumns().add(studentNameColumn);
                gradesTable.getColumns().add(studentNameColumn);

                try{
                    classNumberOfDays = getNumberofDays(aClass);
                    studentGrades = getStudentGrades(studentId ,aClass);
                    studentAttendance = getStudentAttendance(studentId ,aClass);
                    messageList = loadMessages(aClass);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                ObservableList<TableColumn<ObservableList<String>, ?>> columns = studentTable.getColumns();
                ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
                ObservableList<String> rowData = FXCollections.observableArrayList();
                try {
                    rowData.add(getStudentName(studentId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for(int i = 1; i <= classNumberOfDays; i++){
                    TableColumn<ObservableList<String>,String> dayColumn = new TableColumn<>("Day "+i);
                    dayColumn.setPrefWidth(50);
                    final int dayNumber = i;
                    dayColumn.setCellValueFactory(cellData -> {
                        ObservableList<String> row = cellData.getValue();
                        return new SimpleStringProperty(row.get(dayNumber));
                    });
                    columns.add(dayColumn);
                    if(studentAttendance.contains(i))
                        rowData.add("x");
                    else
                        rowData.add(" ");

                }
                tableData.add(rowData);
                studentTable.setItems(tableData);

                ObservableList<TableColumn<ObservableList<String>, ?>> gradeColumns = gradesTable.getColumns();
                ObservableList<ObservableList<String>> gradesTableData = FXCollections.observableArrayList();
                ObservableList<String> gradesRowData = FXCollections.observableArrayList();
                try {
                    gradesRowData.add(getStudentName(studentId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                for(int gradeNumber = 1; gradeNumber <= studentGrades.size(); gradeNumber++){
                    TableColumn<ObservableList<String>,String> gradeColumnTEMP = new TableColumn<>(String.valueOf(gradeNumber));
                    gradeColumnTEMP.setPrefWidth(50);
                    final int dayNumber = gradeNumber;
                    gradeColumnTEMP.setCellValueFactory(cellData -> {
                        ObservableList<String> row = cellData.getValue();
                        return new SimpleStringProperty(row.get(dayNumber));
                    });
                    gradeColumns.add(gradeColumnTEMP);
                    gradesRowData.add(String.valueOf(studentGrades.get(gradeNumber-1)));
                }
                gradesTableData.add(gradesRowData);
                gradesTable.setItems(gradesTableData);

                printMessages();
            });


            classesVbox.setMargin(classButton, new Insets(10, 20, 10, 20));
            classesVbox.getChildren().add(classButton);
        }
    }

    @FXML
    private void joinClassButton(){
        this.changeScene(SceneIdentifier.JOIN_CLASS, 450, 550);
    }

    private void printMessages(){
        messageVbox.getChildren().clear();
        List<Timestamp> sortedTimestamps = new ArrayList<>(messageList.keySet());
        sortedTimestamps.sort(Comparator.naturalOrder());
        for(Timestamp messageTimestamp : sortedTimestamps){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Label messageTimestampLabel = new Label(dateFormat.format(messageTimestamp));
            messageTimestampLabel.setFont(customTimestampFont);
            messageTimestampLabel.setTextFill(Color.web("#004aad"));
            messageTimestampLabel.setWrapText(true);
            messageVbox.getChildren().add(messageTimestampLabel);
            Label messageLabel = new Label(messageList.get(messageTimestamp));
            messageLabel.setMinHeight(Region.USE_PREF_SIZE);
            messageLabel.setPrefHeight(Region.USE_COMPUTED_SIZE);
            messageLabel.setFont(customFont);
            messageLabel.setTextFill(Color.web("#004aad"));
            messageLabel.setWrapText(true);
            messageVbox.getChildren().add(messageLabel);
            Label emptyLabel = new Label("   ");
            emptyLabel.setFont(customFont);
            emptyLabel.setWrapText(true);
            messageVbox.getChildren().add(emptyLabel);
        }
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
