package com.example.testrepo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

//proxeiro
import javafx.scene.control.*;

public class HelloController {
//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
    @FXML
    private PasswordField hiddenpassword;
    @FXML
    private TextField showedpassword;
    @FXML
    private Button eyebutton;

    @FXML
    public void initialize() {
        showedpassword.setManaged(false); // so it doesn’t take space when invisible
        showedpassword.setVisible(false);
        hiddenpassword.textProperty().bindBidirectional(showedpassword.textProperty());
        showedpassword.setVisible(false);
    }
    public void toggle() {
        if (showedpassword.isVisible()) {
            // Hide plain text, show password field
            showedpassword.setVisible(false);
            showedpassword.setManaged(false);
            hiddenpassword.setVisible(true);
            hiddenpassword.setManaged(true);
            eyebutton.setText("show password");
        } else {
            // Show plain text, hide password field
            showedpassword.setVisible(true);
            showedpassword.setManaged(true);
            hiddenpassword.setVisible(false);
            hiddenpassword.setManaged(false);
            eyebutton.setText("hide password");
        }
    }
}