package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class adminMoviesController {


    private ObservableList<Movie> movies = FXCollections.observableArrayList();
    private Movie currentMovie;

    @FXML
    private ImageView imagePoster;

    @FXML
    private void initialize() {
        currentMovie = new Movie();
    }

    private Stage getStage(){
        return (Stage) imagePoster.getScene().getWindow();
    }


    @FXML
    private void importMoviePoster() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG Files", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );

        File selectedFile = fileChooser.showOpenDialog(getStage());
        // could be kinda useless. will see
        if (selectedFile != null) {
            if (currentMovie == null) currentMovie = new Movie();
            String path = selectedFile.getAbsolutePath();
            currentMovie.setPosterUrl(path);
            System.out.println(currentMovie.getPosterUrl());
            imagePoster.setImage(new Image(path, imagePoster.getFitWidth(), imagePoster.getFitHeight(), false, false));
        }
    }
}
