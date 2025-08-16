package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import com.example.testrepo.util.DbConnection;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.util.Objects;

public class adminMoviesController {

    DbConnection dbConnection = new DbConnection();
    private ObservableList<Movie> movies = FXCollections.observableArrayList();

    private String posterUrlPath= null;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private TextField movieTitleField,movieGenreField,movieDurationField;
    @FXML
    private DatePicker movieReleaseDateField;
    @FXML
    private TextArea movieDescriptionField;
    @FXML
    private Label movieErrorLabel;
    @FXML
    private ImageView moviePosterImage;
    @FXML
    private VBox MovieEditingTextFieldsVBox,mainVBox,bottomVBox;
    @FXML
    private HBox movieEditingButtonsHBox,addNewMovieButtonsHBox,moviesHBox;
    @FXML
    private TableView<Movie> movieTable;
    @FXML
    private TableColumn<Movie, String> titleColumn;
    @FXML
    private TableColumn<Movie, Date> releaseDate;
    @FXML
    private TableColumn<Movie, Integer> durationColumn;
    @FXML
    private Button newMovieEntryButton;
    @FXML
    private StackPane stackPane;

    Image defaultMoviePoster = new Image(
            Objects.requireNonNull(getClass().getResource("/com/example/testrepo/images/default-poster.png")).toExternalForm()
    );
//    private final double baseWidth = 800;  // your design width
//    private final double baseHeight = 600;// your design height
    double subtleFactor = 0.3;
    @FXML
    private void initialize() {
        initializeMoviesTableView();
        movieTable.getSelectionModel().selectFirst();
    }

    private Stage getStage(){
        return (Stage) moviePosterImage.getScene().getWindow();
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
        try {
            posterUrlPath = selectedFile.toURI().toURL().toString();
            moviePosterImage.setImage(new Image(posterUrlPath, moviePosterImage.getFitWidth(), moviePosterImage.getFitHeight(), false, false));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            movieErrorLabel.setStyle("-fx-text-fill: red;");
            movieErrorLabel.setText("Failed to load poster image.");
        }
        }
    }

    public void changeEditingAddingButtons (HBox hidden,HBox showed) {
            showed.setVisible(true);
            showed.setManaged(true);
            hidden.setVisible(false);
            hidden.setManaged(false);
    }

    private void clearMovieFields(){
        if (moviePosterImage != null) {
            moviePosterImage.setImage(defaultMoviePoster);
        } else {
            System.out.println("moviePosterImage is null");
        }
        movieTitleField.clear();
        movieGenreField.clear();
        movieReleaseDateField.setValue(null); // cause of weird bug
        movieDurationField.clear();
        movieDescriptionField.clear();
       // posterUrlPath = null;
    }

    private void initializeMoviesTableView() {
        // Fetch fresh data from Movie class
        ObservableList<Movie> movieList = getMovieDataFromDB();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseDate.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("durationMinutes"));
        movieTable.setItems(movieList);

        // Listener so when something is selected it appears on the text fields.
        movieTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                changeEditingAddingButtons(addNewMovieButtonsHBox,movieEditingButtonsHBox);
                movieTitleField.setText(newValue.getTitle());
                movieGenreField.setText(newValue.getGenre());
                movieReleaseDateField.setValue(newValue.getReleaseDate().toLocalDate());
                movieDurationField.setText(String.valueOf(newValue.getDurationMinutes()));
                movieDescriptionField.setText(newValue.getDescription());
                if (newValue.getPosterUrl() != null && !newValue.getPosterUrl().isEmpty()) {
                    moviePosterImage.setImage(new Image(newValue.getPosterUrl(),moviePosterImage.getFitWidth(),moviePosterImage.getFitHeight(),false,false ));
                } else {
                    moviePosterImage.setImage(null);
                }
            }
        });
        // Listen for the table becoming empty
        movieList.addListener((javafx.collections.ListChangeListener<Movie>) change -> {
            if (movieList.isEmpty()) {
                addNewMovieEntry(); // call your method
            }
        });

        // Optional: if the table is already empty on startup
        if (movieList.isEmpty()) {
            addNewMovieEntry();
        }

    }

    public ObservableList<Movie> getMovieDataFromDB() {
        ObservableList<Movie> movieList = FXCollections.observableArrayList();
        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT movie_id, title, description,genre, duration_minutes, release_date, image_url FROM movies";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
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


    @FXML
    public void deleteMovieEntry(){
        Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
        System.out.println( selectedMovie.getTitle());
        if (selectedMovie == null) {
            movieErrorLabel.setText("No movie selected for editing!");
            return;
        }
        try (Connection conn = dbConnection.getConnection()) {
            String deleteQuery = """
            DELETE FROM movies
            WHERE movie_id = ?;
            """;
            try (PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
                ps.setInt(1, selectedMovie.getMovieId());
                System.out.println("Deleting movie with ID: " + selectedMovie.getMovieId());

                movieErrorLabel.setText(selectedMovie.getTitle() + " has been deleted!");
                ps.executeUpdate();
                movieErrorLabel.setStyle("-fx-text-fill:#DC1A23 ;");
                initializeMoviesTableView();
                PauseTransition delay = new PauseTransition(Duration.seconds(2));
                delay.setOnFinished(event -> movieErrorLabel.setText(""));
                delay.play();

                movieTable.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            System.err.println(e + " Failed to update movie");
        }
    }


    @FXML
    public void confirmMovieEditingChanges( ) {
        String falseInput = validateEnteredData();
        if (falseInput.isEmpty()) {
            Movie selectedMovie = movieTable.getSelectionModel().getSelectedItem();
            System.out.println( selectedMovie.getTitle());
            if (selectedMovie == null) {
                movieErrorLabel.setText("No movie selected for editing!");
                return;
            }
            try (Connection conn = dbConnection.getConnection()) {
                String updateQuery = """
                    UPDATE movies
                    SET title = ?, description = ?, genre = ?, duration_minutes = ?, release_date = ?, image_url = ?
                    WHERE movie_id = ?;
                """;
                try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
                    ps.setString(1, movieTitleField.getText());
                    ps.setString(2, movieDescriptionField.getText());
                    ps.setString(3, movieGenreField.getText());
                    ps.setInt(4, Integer.parseInt(movieDurationField.getText()));
                    ps.setDate(5, Date.valueOf(movieReleaseDateField.getValue()));
                    ps.setString(6, posterUrlPath);
                    ps.setInt(7, selectedMovie.getMovieId());
                    System.out.println(selectedMovie.getMovieId());
                    ps.executeUpdate();

                    movieErrorLabel.setStyle("-fx-text-fill: green;");
                    movieErrorLabel.setText("Movie Updated Successfully!");
                    initializeMoviesTableView();
                    movieTable.getSelectionModel().select(selectedMovie);

                }
            } catch (SQLException e) {
                System.err.println(e + " Failed to update movie");
            }
        } else {
            movieErrorLabel.setStyle("-fx-text-fill: #AB1A42;");
            movieErrorLabel.setText(falseInput);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(event -> movieErrorLabel.setText(""));
            delay.play();
        }
    }

    @FXML
    private void addNewMovieEntry(){
        clearMovieFields();
        changeEditingAddingButtons(movieEditingButtonsHBox,addNewMovieButtonsHBox);
    }

    @FXML
    public void confirmNewMovieEntry() {
        String falseInput = validateEnteredData();
        if(falseInput.isEmpty()){
            try(Connection conn = dbConnection.getConnection()){
                String insertQuery = """
                    insert into movies ( title, description, genre, duration_minutes, release_date, image_url)
                    values (?, ?, ?, ?, ?, ?);
                """;
                try {
                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setString(1, movieTitleField.getText());
                    ps.setString(2, movieDescriptionField.getText());
                    ps.setString(3, movieGenreField.getText());
                    ps.setInt(4, Integer.parseInt(movieDurationField.getText()));
                    ps.setDate(5, Date.valueOf(movieReleaseDateField.getValue()));
                    ps.setString(6, posterUrlPath);
                    ps.executeUpdate();

                    movieErrorLabel.setText(movieTitleField.getText() + " has been added!");
                    movieErrorLabel.setStyle("-fx-text-fill:#159176 ;");
                    clearMovieFields();
                    initializeMoviesTableView();
                    PauseTransition delay = new PauseTransition(Duration.seconds(2));
                    delay.setOnFinished(event -> movieErrorLabel.setText(""));
                    delay.play();
                }catch(SQLException e){
                    System.err.println(e + "Failed to insert movie");
                }
            }catch(SQLException e){
                System.err.println(e + "Failed to edit movie");
            }
        }else {

            movieErrorLabel.setStyle("-fx-text-fill: #AB1A42;");
            movieErrorLabel.setText(falseInput);
            PauseTransition delay = new PauseTransition(Duration.seconds(2.5));
            delay.setOnFinished(event -> movieErrorLabel.setText(""));
            delay.play();
        }

    }

    private String validateEnteredData() {
        if (movieTitleField.getText().trim().isEmpty()) {
            return "Movie Title cannot be empty!";
        }
        if (movieGenreField.getText().trim().isEmpty()) {
            return "Movie Genre cannot be empty!";
        }
        if (movieDurationField.getText().trim().isEmpty()) {
            return "Movie Duration cannot be empty!";
        }
        try {
            int duration = Integer.parseInt(movieDurationField.getText().trim());
            if (duration <= 0) {
                return "Duration must be greater than 0!";
            }
        } catch (NumberFormatException e) {
            return "Movie Duration must be a valid number!";
        }
        if (movieReleaseDateField.getValue() == null) {
            return "Release Date cannot be empty!";
        }
        if (movieDescriptionField.getText().trim().isEmpty()) {
            return "Movie Description cannot be empty!";
        }
        if (moviePosterImage.getImage() == null || moviePosterImage.getImage() == defaultMoviePoster){
            return "Poster image cannot be empty!";
        }

        return "";
    }


}
