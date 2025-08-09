package com.example.testrepo.controllers;

import javafx.fxml.FXML;
import com.example.testrepo.util.DbConnection;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class loginScreenController {
    @FXML
    private PasswordField hiddenLoginPasswordField;
    @FXML
    private TextField showedLoginPasswordField;
    @FXML
    private PasswordField hiddenNewPasswordField;
    @FXML
    private TextField showedNewPasswordField;
    @FXML
    private PasswordField hiddenConfirmPasswordField;
    @FXML
    private TextField showedConfirmPasswordField;
    @FXML
    private Button togglePasswordVisibilityButton;
    @FXML
    private Button toggleNewPasswordVisibilityButton;
    @FXML
    private Button toggleConfirmPasswordVisibilityButton;


    @FXML
    public void initialize() {
        //Initialize Password Fields
        initPasswordVisibilityFields(hiddenLoginPasswordField, showedLoginPasswordField);
        initPasswordVisibilityFields(hiddenNewPasswordField, showedNewPasswordField);
        initPasswordVisibilityFields(hiddenConfirmPasswordField, showedConfirmPasswordField);
    }

    private void initPasswordVisibilityFields(PasswordField hidden, TextField showed) {
        showed.setManaged(false);
        showed.setVisible(false);
        hidden.textProperty().bindBidirectional(showed.textProperty());
        showed.setVisible(false);
    }

    @FXML
    public void togglePasswordVisibility() {
        togglePasswordFields(hiddenLoginPasswordField, showedLoginPasswordField, togglePasswordVisibilityButton);
    }
    @FXML
    public void toggleNewPasswordVisibility() {
        togglePasswordFields(hiddenNewPasswordField, showedNewPasswordField, toggleNewPasswordVisibilityButton);
    }
    @FXML
    public void toggleConfirmPasswordVisibility() {
        togglePasswordFields(hiddenConfirmPasswordField, showedConfirmPasswordField, toggleConfirmPasswordVisibilityButton);
    }

    public void togglePasswordFields(PasswordField hidden,TextField showed,Button toggleButton) {
        if (showed.isVisible()) {
            showed.setVisible(false);
            showed.setManaged(false);
            hidden.setVisible(true);
            hidden.setManaged(true);
            toggleButton.setText("Show Password");
        } else {
            showed.setVisible(true);
            showed.setManaged(true);
            hidden.setVisible(false);
            hidden.setManaged(false);
            toggleButton.setText("Hide Password");
        }
    }

    public void loginUser() {
        DbConnection dbConnection = new DbConnection();
        Connection con = dbConnection.getConnection();

    }
}