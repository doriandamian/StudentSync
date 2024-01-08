package com.example.studentsync.controllers;


import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;

import static com.example.studentsync.utils.DatabaseFunctions.*;

public class TeacherViewController extends SceneController {

    @FXML
    private VBox classesVbox = new VBox(10);
    @FXML
    private ScrollPane studentTablePane = new ScrollPane();
    @FXML
    TableView<ObservableList<String>> studentTable = new TableView<>();
    @FXML
    ComboBox<String> studentSelect;
    @FXML
    ComboBox<Integer> studentGrade;
    @FXML
    TextField gradeTextfield;
    @FXML
    Label gradeErrorLabel;
    @FXML
    DatePicker attendanceDate;
    @FXML
    TextField messageTextfield;
    @FXML
    VBox messageVbox;
    @FXML
    ScrollPane scrollPane;
    @FXML
    Button deleteButton;
    @FXML
    Button addButton;
    @FXML
    Button attendanceButton;
    @FXML
    Button sendButton;

    static Font customFont = new Font("Kollektif Bold",20);
    static Font customTimestampFont = new Font("Kollektif Bold",14);

    private Date startDate;
    private Date endDate;
    private Integer selectedClass;
    private TableColumn<ObservableList<String>, String> studentNameColumn;
    private HashMap<Integer, List<Integer>> attendanceMap;
    private HashMap<Integer,String> studentList = null;
    private HashMap<Timestamp, String> messageList = null;

    @FXML
    private void loadButtonAction() throws SQLException {
        List<Integer> classes = getTeacherClasses();
        classesVbox.getChildren().clear();
        for (Integer aClass : classes) {
            System.out.println("Loading class");
            Button classButton = new Button();

            classButton.setText(getClassName(aClass));
            classButton.setFont(customFont);
            classButton.setTextFill(Color.web("#004aad"));
            classButton.setOnAction(event -> {
                gradeErrorLabel.setVisible(false);
                studentSelect.getSelectionModel().clearSelection();
                studentGrade.getSelectionModel().clearSelection();
                this.selectedClass = aClass;
                gradeTextfield.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue,
                                        String newValue) {
                        if (!newValue.matches("\\d*")) {
                            gradeTextfield.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });

                studentTablePane.setVisible(true);
                studentSelect.setVisible(true);
                studentGrade.setVisible(true);
                gradeTextfield.setVisible(true);
                attendanceDate.setVisible(true);
                scrollPane.setVisible(true);
                messageTextfield.setVisible(true);
                deleteButton.setVisible(true);
                addButton.setVisible(true);
                attendanceButton.setVisible(true);
                sendButton.setVisible(true);
                studentTable.getColumns().clear();
                studentTable.getItems().clear();
                TableColumn<ObservableList<String>, String> studentNameColumn = new TableColumn<>("Student");
                studentNameColumn.setPrefWidth(200);
                studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
                studentTable.getColumns().add(studentNameColumn);
                int numberOfDays;
                try {
                    attendanceMap = getAttendance(aClass);
                    studentList = getStudentList(aClass);
                    messageList = loadMessages(aClass);
                    startDate = getStartDate(aClass);
                    endDate = getEndDate(aClass);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    numberOfDays = getNumberofDays(aClass);
                    ObservableList<TableColumn<ObservableList<String>, ?>> columns = studentTable.getColumns();
                    for(int i = 1; i <= numberOfDays; i++){
                        TableColumn<ObservableList<String>,String> dayColumn = new TableColumn<>("Day "+i);
                        dayColumn.setPrefWidth(50);
                        final int dayNumber = i;
                        dayColumn.setCellValueFactory(cellData -> {
                            ObservableList<String> row = cellData.getValue();
                            return new SimpleStringProperty(row.get(dayNumber));
                        });
                        columns.add(dayColumn);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
                for(int i:studentList.keySet()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    try {
                        row.add(getStudentName(i));
                        if (attendanceMap.containsKey(i)) {
                            for (int day = 1; day <= numberOfDays; day++) {
                                if (attendanceMap.get(i).contains(day))
                                    row.add("x");
                                else
                                    row.add(" ");
                            }
                        } else {
                            for (int day = 1; day <= numberOfDays; day++) {
                                row.add(" ");
                            }
                        }
                        tableData.add(row);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                studentTable.setItems(tableData);
                studentTable.getSortOrder().add(studentNameColumn);

                ObservableList<String> temp = FXCollections.observableArrayList(studentList.values());
                FXCollections.sort(temp);
                studentSelect.setItems(temp);

                attendanceDate.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);

                        if (date.isBefore(startDate.toLocalDate()) || date.isAfter(endDate.toLocalDate())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                });

                printMessages();
            });
            classesVbox.setMargin(classButton, new Insets(10, 20, 10, 20));
            classesVbox.getChildren().add(classButton);
        }
    }

    @FXML
    private void getSelectedStudentsGrades() throws SQLException {
        ObservableList<Integer> temp = FXCollections.observableList(getStudentGrades(getKeyByValue(studentList, studentSelect.getValue()), this.selectedClass));
        FXCollections.sort(temp);
        studentGrade.setItems(temp);
    }

    @FXML
    private void deleteGradeButton() throws SQLException {
        deleteGrade(getKeyByValue(studentList, studentSelect.getValue()), this.selectedClass, studentGrade.getValue());
        gradeErrorLabel.setText("GRADE DELETED");
        gradeErrorLabel.setVisible(true);
        getSelectedStudentsGrades();
    }

    @FXML
    private void addGradeButton() throws SQLException {
        if(gradeTextfield.getText().equals("")){
            gradeErrorLabel.setText("ENTER GRADE");
            gradeErrorLabel.setVisible(true);
        }
        else if(studentSelect.getValue() == null){
            gradeErrorLabel.setText("SELECT STUDENT");
            gradeErrorLabel.setVisible(true);
        }
        else if(Integer.valueOf(gradeTextfield.getText()) <= 10 && Integer.valueOf(gradeTextfield.getText()) >= 1){
            addGrade(getKeyByValue(studentList, studentSelect.getValue()), this.selectedClass, Integer.valueOf(gradeTextfield.getText()));
            gradeTextfield.clear();
            gradeErrorLabel.setText("GRADE ADDED");
            gradeErrorLabel.setVisible(true);
            getSelectedStudentsGrades();
        }
        else{
            gradeErrorLabel.setText("INVALID GRADE");
            gradeErrorLabel.setVisible(true);
        }
    }

    @FXML
    private void addAttendanceButton() throws SQLException{
        if(studentSelect.getValue() == null){
            gradeErrorLabel.setText("SELECT STUDENT");
            gradeErrorLabel.setVisible(true);
        } else if (attendanceDate.getValue() == null) {
            gradeErrorLabel.setText("SELECT DATE");
            gradeErrorLabel.setVisible(true);
        } else if (doesAttendanceExist(getKeyByValue(studentList, studentSelect.getValue()), this.selectedClass, Date.valueOf(attendanceDate.getValue()))) {
            gradeErrorLabel.setText("ATTENDANCE EXIST");
            gradeErrorLabel.setVisible(true);
        }
        else {
            addAttendance(getKeyByValue(studentList, studentSelect.getValue()), this.selectedClass, Date.valueOf(attendanceDate.getValue()));
            gradeErrorLabel.setText("ATTENDANCE ADDED");
            gradeErrorLabel.setVisible(true);
            reloadTable();
        }
    }

    private void reloadTable() throws SQLException {
        studentTable.getColumns().clear();
        studentTable.getItems().clear();
        attendanceMap = getAttendance(selectedClass);
        if (studentNameColumn == null) {
            studentNameColumn = new TableColumn<>("Student");
            studentNameColumn.setPrefWidth(200);
            studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        }
        studentTable.getColumns().add(studentNameColumn);
        int numberOfDays = getNumberofDays(selectedClass);
        ObservableList<TableColumn<ObservableList<String>, ?>> columns = studentTable.getColumns();
        for (int i = 1; i <= numberOfDays; i++) {
            TableColumn<ObservableList<String>, String> dayColumn = new TableColumn<>("Day " + i);
            dayColumn.setPrefWidth(50);
            final int dayNumber = i;
            dayColumn.setCellValueFactory(cellData -> {
                ObservableList<String> row = cellData.getValue();
                return new SimpleStringProperty(row.get(dayNumber));
            });
            columns.add(dayColumn);
        }

        ObservableList<ObservableList<String>> tableData = FXCollections.observableArrayList();
        for (int i : studentList.keySet()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            try {
                row.add(getStudentName(i));
                if (attendanceMap.containsKey(i)) {
                    for (int day = 1; day <= numberOfDays; day++) {
                        if (attendanceMap.get(i).contains(day))
                            row.add("x");
                        else
                            row.add(" ");
                    }
                } else {
                    for (int day = 1; day <= numberOfDays; day++) {
                        row.add(" ");
                    }
                }
                tableData.add(row);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        studentTable.setItems(tableData);
        studentTable.getSortOrder().add(studentTable.getColumns().get(0));
    }

    @FXML
    private void sendMessageButton() throws SQLException {
        sendMessage(this.selectedClass, messageTextfield.getText());
        messageList = loadMessages(this.selectedClass);
        messageTextfield.clear();
        printMessages();
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

    @FXML
    private void createClassButton(){
        this.changeScene(SceneIdentifier.CREATE_CLASSROOM, 450, 550);
    }
}
