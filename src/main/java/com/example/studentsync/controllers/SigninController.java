package com.example.studentsync.controllers;

import com.example.studentsync.utils.SceneController;
import com.example.studentsync.utils.enums.SceneIdentifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import java.sql.*;
import java.time.LocalDate;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static com.example.studentsync.utils.DatabaseFunctions.*;

public class SigninController extends SceneController {



    private List<Integer> dayLIST = new ArrayList<Integer>(initializeDays());
    private ObservableList<Integer> days = FXCollections.observableList(dayLIST);

    ObservableList<String> monthList = FXCollections.observableArrayList( "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December");

    private List<Integer> yearLIST = new ArrayList<Integer>(initializeYears());
    private ObservableList<Integer> years = FXCollections.observableList(yearLIST);

    @FXML
    private Label errorLabel = new Label();

    @FXML
    private ComboBox<Integer> birthdayDay;
    @FXML
    private ComboBox<String> birthdayMonth;
    @FXML
    private ComboBox<Integer> birthdayYear;
    @FXML
    private TextField firstName = new TextField();
    @FXML
    private TextField lastName = new TextField();
    @FXML
    private TextField username = new TextField();
    @FXML
    private TextField email = new TextField();
    @FXML
    private TextField phoneNumber = new TextField();
    @FXML
    private TextField address = new TextField();
    @FXML
    private PasswordField password = new PasswordField();
    @FXML
    private PasswordField repeatPassword = new PasswordField();
    @FXML
    private RadioButton studentButton = new RadioButton();
    @FXML
    private RadioButton teacherButton = new RadioButton();

    private ToggleGroup toggleGroup;
    private boolean student;
    private Date birthday;

    Font customFont = new Font("Kollektif Bold",15);

    public SigninController() {
    }

    private List<Integer> initializeDays(){
        List<Integer> daysTEMP = new ArrayList<Integer>();
        for(int day = 1; day <= 31; day++)
            daysTEMP.add(day);
        return daysTEMP;
    }

    private List<Integer> initializeYears(){
        Integer currentYear = Year.now().getValue();
        List<Integer> yearsTEMP = new ArrayList<Integer>();
        for(int year=currentYear; year >= 1920; year--)
            yearsTEMP.add(year);
        return yearsTEMP;
    }

    @FXML
    private void initialize() throws SQLException {
        System.out.println("Initializing SignInController");
        birthdayDay.setItems(days);
        birthdayDay.setStyle("-fx-font: " + customFont.getSize() + " " + customFont.getFamily() + ";");
        birthdayMonth.setItems(monthList);
        birthdayMonth.setStyle("-fx-font: " + customFont.getSize() + " " + customFont.getFamily() + ";");
        birthdayYear.setItems(years);
        birthdayYear.setStyle("-fx-font: " + customFont.getSize() + " " + customFont.getFamily() + ";");
        toggleGroup = new ToggleGroup();
        studentButton.setToggleGroup(toggleGroup);
        teacherButton.setToggleGroup(toggleGroup);
    }

    @FXML
    protected void changeToWelcome(){
        this.changeScene(SceneIdentifier.HELLO, 450, 550);
    }

    protected boolean emptyTextFields(){
        if(firstName.getText().isEmpty()) return true;
        if(lastName.getText().isEmpty()) return true;
        if(username.getText().isEmpty()) return true;
        if(email.getText().isEmpty()) return true;
        if(phoneNumber.getText().isEmpty()) return true;
        if(address.getText().isEmpty()) return true;
        if(password.getText().isEmpty()) return true;
        if(repeatPassword.getText().isEmpty()) return true;
        if(!(studentButton.isSelected() || teacherButton.isSelected())) return true;
        if(birthdayDay.getValue() == null || birthdayMonth.getValue() == null || birthdayYear.getValue() == null ) return true;
        return false;
    }

    private boolean isStudent(){
        if(studentButton.isSelected())
            return true;
        if(teacherButton.isSelected())
            return false;
        System.out.println("Error finding if the person is a teacher or a student");
        return false;
    }

    protected Date convertToSqlDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        Date sqlDate = Date.valueOf(localDate);
        return sqlDate;
    }

    protected int convertToMonth(String month) {
        switch (month.toLowerCase()) {
            case "january":
                return 1;
            case "february":
                return 2;
            case "march":
                return 3;
            case "april":
                return 4;
            case "may":
                return 5;
            case "june":
                return 6;
            case "july":
                return 7;
            case "august":
                return 8;
            case "september":
                return 9;
            case "october":
                return 10;
            case "november":
                return 11;
            case "december":
                return 12;
        }
        return 0;
    }

    @FXML
    protected void signinButton() throws SQLException {
        if(!emptyTextFields()) {
            errorLabel.setVisible(false);
            birthday = convertToSqlDate(birthdayYear.getValue(), convertToMonth(birthdayMonth.getValue()), birthdayDay.getValue());
            student = isStudent();
            String error = checkIfAccountIsValid(username.getText(), email.getText(), phoneNumber.getText());
            if(password.getText().equals(repeatPassword.getText())) {
                if(error.equals("OK")){
                    try {
                        addUser(firstName, lastName, username, email, phoneNumber, address, password, birthday, student);
                        errorLabel.setText("Account created successfully");
                        errorLabel.setVisible(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    errorLabel.setText(error);
                    errorLabel.setVisible(true);
                }
            }
            else {
                errorLabel.setText("Passwords do not match");
                errorLabel.setVisible(true);
            }
        }
        else{
            errorLabel.setText("Please fill out all fields");
            errorLabel.setVisible(true);
        }
    }
}
