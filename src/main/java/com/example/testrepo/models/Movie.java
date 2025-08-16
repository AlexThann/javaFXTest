package com.example.testrepo.models;

import com.example.testrepo.util.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Movie {
    private int movieId;
    private String title;
    private String description;
    private String genre;
    private int durationMinutes;
    private Date releaseDate;
    private String posterUrl;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

//    public static ObservableList<Movie> getMovieDataFromDB() {
//        ObservableList<Movie> movieList = FXCollections.observableArrayList();
//
//        try (Connection conn = DbConnection.getConnection()) {
//            String sql = "SELECT title, description,genre, duration_minutes, release_date, image_url FROM movies";
//            PreparedStatement stmt = conn.prepareStatement(sql);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                Movie movie = new Movie();
//                movie.setTitle(rs.getString("title"));
//                movie.setDescription(rs.getString("description"));
//                movie.setGenre(rs.getString("genre"));
//                movie.setDurationMinutes(rs.getInt("duration_minutes"));
//                movie.setReleaseDate(rs.getDate("release_date"));
//                movie.setPosterUrl(rs.getString("image_url"));
//                movieList.add(movie);
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return movieList;
//    }
}
