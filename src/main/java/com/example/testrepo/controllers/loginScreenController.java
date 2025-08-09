package com.example.testrepo.controllers;

import javafx.fxml.FXML;
import com.example.testrepo.util.DbConnection;

// extra
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.io.IOException;

// extras
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class loginScreenController {
    DbConnection dbConnection = new DbConnection();

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
    private Label loginErrorLabel;
    @FXML
    private Label  registerErrorLabel;
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
        initializeInputLimits();
        initPasswordVisibilityFields(hiddenLoginPasswordField, showedLoginPasswordField);
        initPasswordVisibilityFields(hiddenNewPasswordField, showedNewPasswordField);
        initPasswordVisibilityFields(hiddenConfirmPasswordField, showedConfirmPasswordField);
    }

    public TextFormatter<String> limitTextCharsFormatter(){
        // uses a TextFormatter which gets the complete text (getControlNewText()) checks the length and either returns the character pressed (change) or returns null.
        return new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 50 ? change : null);
    }

    public void initializeInputLimits() {
        loginUsernameTextField.setTextFormatter(limitTextCharsFormatter());
        registerFullnameTextField.setTextFormatter(limitTextCharsFormatter());
        registerUsernameTextField.setTextFormatter(limitTextCharsFormatter());
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

    public void clearRegisterFields(){
        // resets register input fields
        registerErrorLabel.setText("");
        registerFullnameTextField.setText("");
        registerUsernameTextField.setText("");
        hiddenNewPasswordField.setText("");
        showedNewPasswordField.setText("");
        hiddenConfirmPasswordField.setText("");
        showedConfirmPasswordField.setText("");
    }

    public void showLoginUI(){
        clearRegisterFields();
        // toggles visibility
        loginVBoxUI.setVisible(true);
        loginVBoxUI.setManaged(true);
        registerVBoxUI.setVisible(false);
        registerVBoxUI.setManaged(false);
    }

    public void showRegisterUI(){
        // resets login input fields
        loginErrorLabel.setText("");
        loginUsernameTextField.setText("");
        hiddenLoginPasswordField.setText("");
        // toggles visibility
        loginVBoxUI.setVisible(false);
        loginVBoxUI.setManaged(false);
        registerVBoxUI.setVisible(true);
        registerVBoxUI.setManaged(true);
    }

    public void loginUser() throws IOException {
        // DB Connection
        Connection con = dbConnection.getConnection();
        //Username and Password from UI
        String username = loginUsernameTextField.getText();
        String password = hiddenLoginPasswordField.getText();

        String roleName = null;
        String hashedPassword=null;
        // Checking for match and getting the role
        try {
            String sql = "SELECT u.password,r.role_name FROM users u JOIN roles r ON u.role_id = r.role_id WHERE u.username = ?";
            PreparedStatement ps = con.prepareStatement(sql); // No sql injection
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // user exists
                hashedPassword = rs.getString("password");
                if(BCrypt.checkpw(password,hashedPassword)){
                    roleName = rs.getString("role_name");
                }else {
                    loginErrorLabel.setText("Incorrect Username or Password!");
                    loginErrorLabel.setVisible(true);
                    loginErrorLabel.setManaged(true);
                    return;
                }
            } else { // no match
                loginErrorLabel.setText("Incorrect Username or Password!");
                loginErrorLabel.setVisible(true);
                loginErrorLabel.setManaged(true);
                return;
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
            return ;
        }

        Parent root = loader.load();
        Stage stage = (Stage) loginUsernameTextField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public boolean checkInvalidFields(){
        if(registerFullnameTextField.getText().isEmpty()){
            registerErrorLabel.setText("Full name field cannot be empty");
            return true;
        }else if(registerUsernameTextField.getText().isEmpty()){
            registerErrorLabel.setText("Username field cannot be empty");
            return true;
        }else if(showedNewPasswordField.getText().isEmpty() ||  showedConfirmPasswordField.getText().isEmpty()) {
            registerErrorLabel.setText("Password field cannot be empty");
            return true;
        }
        if(!showedNewPasswordField.getText().equals(showedConfirmPasswordField.getText())){
            registerErrorLabel.setText("Passwords do not match");
            return true;
        }
        return false;
    }

    public boolean userExists(Connection con, String username){
        try{
            String selectQuery = "SELECT * FROM USERS WHERE USERNAME = ?";
            PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }


    }

    public void registerUser() throws SQLException {
        if(checkInvalidFields())
            return ;

        String fullname = registerFullnameTextField.getText();
        String username = registerUsernameTextField.getText();
        String password = showedNewPasswordField.getText();
        Connection con = dbConnection.getConnection();

        if(userExists(con,username)){
            registerErrorLabel.setText("Username already exists");
            return ;
        }

        String hashedPassword=BCrypt.hashpw(password, BCrypt.gensalt(12));
        registerErrorLabel.setText("");
        String createUserQuery= "INSERT INTO USERS (fullname,username,password,role_id) VALUES (?,?,?,1)";
        PreparedStatement ps = con.prepareStatement(createUserQuery);
        ps.setString(1,fullname);
        ps.setString(2,username);
        ps.setString(3,hashedPassword);
        ps.executeUpdate();
        con.close();
        clearRegisterFields();
    }
}
