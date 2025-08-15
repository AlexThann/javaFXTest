package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import com.example.testrepo.util.DbConnection;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
//import javafx.scene.control.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import java.io.File;

public class adminMoviesController {

    DbConnection dbConnection = new DbConnection();
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
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Date> releaseDate;
    @FXML
    private TableColumn<Movie, Integer> durationColumn;


    private final double baseWidth = 900;  // your design width
    private final double baseHeight = 600; // your design height
    @FXML
    private void initialize() {
        DbConnection dbConnection = new DbConnection();
        initializeMoviesTableView();
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

    public void initializeMoviesUI(){
        //ObservableList<Movie> movieList = Movie.getMovieDataFromDB();
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

    private void initializeMoviesTableView() {
        // Fetch fresh data from Movie class
        ObservableList<Movie> movieList = getMovieDataFromDB();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));

        movieTable.setItems(movieList);
    }

    public ObservableList<Movie> getMovieDataFromDB() {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT title, description,genre, duration_minutes, release_date, image_url FROM movies";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setTitle(rs.getString("title"));
                movie.setDescription(rs.getString("description"));
                movie.setGenre(rs.getString("genre"));
                movie.setDurationMinutes(rs.getInt("duration_minutes"));
                movie.setReleaseDate(rs.getDate("release_date"));
                movie.setPosterUrl(rs.getString("image_url"));
                movieList.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movieList;
    }

}
