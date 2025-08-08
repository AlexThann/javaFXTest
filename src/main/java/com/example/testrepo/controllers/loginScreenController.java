package com.example.testrepo.controllers;

import javafx.fxml.FXML;
import com.example.testrepo.util.DbConnection;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class loginScreenController {

    @FXML
    private PasswordField hiddenPasswordField;
    @FXML
    private TextField showedPasswordField;
    @FXML
    private Button togglePasswordVisibilityButton;

    @FXML
    public void initialize() {
        showedPasswordField.setManaged(false); // so it doesn’t take space when invisible
        showedPasswordField.setVisible(false);
        hiddenPasswordField.textProperty().bindBidirectional(showedPasswordField.textProperty());
        showedPasswordField.setVisible(false);
    }
    public void togglePasswordVisibility() {
        if (showedPasswordField.isVisible()) {
            // Hide plain text, show password field
            showedPasswordField.setVisible(false);
            showedPasswordField.setManaged(false);
            hiddenPasswordField.setVisible(true);
            hiddenPasswordField.setManaged(true);
            togglePasswordVisibilityButton.setText("Show Password");
        } else {
            // Show plain text, hide password field
            showedPasswordField.setVisible(true);
            showedPasswordField.setManaged(true);
            hiddenPasswordField.setVisible(false);
            hiddenPasswordField.setManaged(false);
            togglePasswordVisibilityButton.setText("Hide Password");
        }
    }

    public void loginUser() {
        DbConnection dbConnection = new DbConnection();
        Connection con = dbConnection.getConnection();

    }
}