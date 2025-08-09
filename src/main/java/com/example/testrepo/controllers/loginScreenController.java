package com.example.testrepo.controllers;

import javafx.fxml.FXML;
import com.example.testrepo.util.DbConnection;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    private TextField loginUsernameTextField;

    @FXML
    private TextField registerFullnameTextField;
    @FXML
    private TextField registerUsernameTextField;

    @FXML
    private Button togglePasswordVisibilityButton;
    @FXML
    private Button toggleNewPasswordVisibilityButton;
    @FXML
    private Button toggleConfirmPasswordVisibilityButton;
    @FXML
    private VBox loginVBoxUI;
    @FXML
    private VBox registerVBoxUI;


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

    public void showLoginUI(){
        registerFullnameTextField.setText("");
        registerUsernameTextField.setText("");
        hiddenNewPasswordField.setText("");
        showedNewPasswordField.setText("");
        hiddenConfirmPasswordField.setText("");
        showedConfirmPasswordField.setText("");
        loginVBoxUI.setVisible(true);
        loginVBoxUI.setManaged(true);
        registerVBoxUI.setVisible(false);
        registerVBoxUI.setManaged(false);
    }

    public void showRegisterUI(){
        loginUsernameTextField.setText("");
        hiddenLoginPasswordField.setText("");
        loginVBoxUI.setVisible(false);
        loginVBoxUI.setManaged(false);
        registerVBoxUI.setVisible(true);
        registerVBoxUI.setManaged(true);
    }

    public void loginUser() {
        DbConnection dbConnection = new DbConnection();
        Connection con = dbConnection.getConnection();

    }
}