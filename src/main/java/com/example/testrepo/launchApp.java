package com.example.testrepo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class launchApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // path starts from resources folder.
        FXMLLoader fxmlLoader = new FXMLLoader(launchApp.class.getResource("/com/example/testrepo/fxml/login-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cinema Management System");
        stage.setScene(scene);
        //setting the minimum size of the window
        stage.setMinWidth(850);
        stage.setMinHeight(550);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}