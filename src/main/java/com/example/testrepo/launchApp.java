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
        FXMLLoader fxmlLoader = new FXMLLoader(launchApp.class.getResource("/com/example/testrepo/fxml/user/user.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Cinema Management System");
        stage.setScene(scene);
        stage.show();
        // get focus away from first field
        scene.getRoot().requestFocus();

        // calculate sizes for window borders + title bar
        double decorationWidth = stage.getWidth() - scene.getWidth();
        double decorationHeight = stage.getHeight() - scene.getHeight();
        // min window size
        stage.setMinWidth(800 + decorationWidth);
        stage.setMinHeight(500 + decorationHeight);
    }

    public static void main(String[] args) {
        launch();
    }
}


