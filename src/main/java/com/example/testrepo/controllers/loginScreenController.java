package com.example.testrepo.controllers;

import javafx.fxml.FXML;
import com.example.testrepo.util.DbConnection;

// extra
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

// extras
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


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
    private Label loginErrorLabel;

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

    public void loginUser() throws IOException {
        // DB Connection
        DbConnection dbConnection = new DbConnection();
        Connection con = dbConnection.getConnection();
        //Username and Password from UI
        String username = loginUsernameTextField.getText();
        String password = hiddenLoginPasswordField.getText();

        String roleName = null;
        // Checking for match and getting the role
        try {
            String sql = "SELECT r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.username = ? AND u.password = ?";
            PreparedStatement ps = con.prepareStatement(sql); // No sql injection
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // user exists
                roleName = rs.getString("role_name");
            } else { // no match
                loginErrorLabel.setText("Incorrect Username or Password!");
                loginErrorLabel.setVisible(true);
                loginErrorLabel.setManaged(true);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Load the correct FXML based on role
        FXMLLoader loader = null ;

        if ("ADMIN".equalsIgnoreCase(roleName)) { // ADMIN
            loader = new FXMLLoader(getClass().getResource("/com/example/testrepo/fxml/admin.fxml"));
        } else if ("USER".equalsIgnoreCase(roleName)) { // USER
            loader = new FXMLLoader(getClass().getResource("/com/example/testrepo/fxml/user.fxml"));
        } else {
            System.out.println("Not valid role: " + roleName);
        }

        Parent root = loader.load();
        Stage stage = (Stage) loginUsernameTextField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}