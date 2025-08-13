package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class adminMoviesController {


    private ObservableList<Movie> movies = FXCollections.observableArrayList();

    private String posterUrlPath= null;

    @FXML
    private TextField movieTitleField,genreField,releaseDateField,durationField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Label errorMovieLabel;

    @FXML
    private ImageView imagePoster;

    @FXML
    private void initialize() {
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
            posterUrlPath = selectedFile.getAbsolutePath();
            imagePoster.setImage(new Image(posterUrlPath, imagePoster.getFitWidth(), imagePoster.getFitHeight(), false, false));
        }
    }

    @FXML
    private void addMovie(){
        System.out.println("HELLO");
    }

}
