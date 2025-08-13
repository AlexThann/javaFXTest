package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//scaling
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import java.util.List;
import java.util.ArrayList;

import java.io.File;

public class adminMoviesController {


    private ObservableList<Movie> movies = FXCollections.observableArrayList();

    private String posterUrlPath= null;
    @FXML
        private AnchorPane rootAnchorPane;
    @FXML
    private TextField movieTitleField,genreField,durationField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private Label errorMovieLabel;

    @FXML
    private ImageView imagePoster;
    @FXML
    private VBox MovieEditingTextFieldsVBox;

    @FXML
    private HBox moviesHBox,movieEditingButtonsHBox;

    private final double baseWidth = 900;  // your design width
    private final double baseHeight = 600; // your design height
    @FXML
    private void initialize() {
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            double scaleY = rootAnchorPane.getHeight() / baseHeight;
            double scaleX = rootAnchorPane.getWidth() / baseWidth;
            MovieEditingTextFieldsVBox.setScaleY(scaleY);// text fields change only height
        };
        rootAnchorPane.heightProperty().addListener(sizeListener);
        rootAnchorPane.widthProperty().addListener(sizeListener);
        double initialScale = rootAnchorPane.getHeight() / baseHeight;
        MovieEditingTextFieldsVBox.setScaleY(initialScale); // textfields change only height

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
