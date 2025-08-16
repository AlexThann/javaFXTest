package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Schedule;
import com.example.testrepo.util.DbConnection;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class adminDashboardController {
    private DbConnection dbConnection = new DbConnection();

    ObservableList<Schedule> todaysScheduleList = FXCollections.observableArrayList();

    @FXML
    private TableView<Schedule> todaysScheduleTableView;

    @FXML
    private TableColumn<Schedule, String> movieNameColumn;

    @FXML
    private TableColumn<Schedule, Date> screenDateColumn;

    @FXML
    private TableColumn<Schedule, Time> startTimeColumn,endTimeColumn;

    @FXML
    private TableColumn<Schedule,Double> ticketPriceColumn;

    @FXML
    private TableColumn<Schedule, Integer> seatsColumn;

    @FXML
    private TableColumn<Schedule, Integer> screenRoomColumn;

    @FXML
    private VBox parentVBox;

    @FXML
    private StackPane carouselStackPane;
    private int carouselIndex;

    @FXML
    private List<Image> imageList = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("hi");
        initializeTableView();
    }

    public void initializeDashboardUI(){
        todaysScheduleList.setAll(fetchTodaysScheduleFromDB());
        initializeImageList();
    }

    private void initializeImageList(){
        List<Image> localImageList = new ArrayList<>();
        try{
            Connection conn=dbConnection.getConnection();
            String fetchImages = "select image_url from movies";
            PreparedStatement ps = conn.prepareStatement(fetchImages);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                localImageList.add(new Image(rs.getString("image_url")));
            }
            imageList=localImageList;
            conn.close();
        }catch(SQLException e){
            System.err.println(e + "Unable to load images for the carousel");
        }
        if (!imageList.isEmpty()) {
            carouselIndex = 0;

            // First image
            ImageView imageView = new ImageView(imageList.get(carouselIndex));
            imageView.setFitWidth(100);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);

            carouselStackPane.getChildren().clear();
            carouselStackPane.getChildren().add(imageView);

            // Setup timeline for automatic switching
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(3.5), event -> {
                        carouselIndex = (carouselIndex + 1) % imageList.size();

                        // Replace image
                        ImageView newImageView = new ImageView(imageList.get(carouselIndex));
                        newImageView.setFitWidth(100);
                        newImageView.setFitHeight(200);
                        newImageView.setPreserveRatio(true);

                        carouselStackPane.getChildren().setAll(newImageView);
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    public ObservableList<Schedule> fetchTodaysScheduleFromDB() {
        ObservableList<Schedule> localScheduleList = FXCollections.observableArrayList();

        try{
            Connection con = dbConnection.getConnection();
            String fetchSchedule = "select s.schedule_id, m.title,s.screening_date,s.start_time,s.end_time, s.screen_room,s.available_seats,s.ticket_price from schedule s,movies m where s.movie_id = m.movie_id and s.screening_date=CURDATE();";
            PreparedStatement ps = con.prepareStatement(fetchSchedule);
            ResultSet rs =  ps.executeQuery();
            while(rs.next()){
                Integer scheduleId = rs.getInt("schedule_id");
                String movieName = rs.getString("title");
                Date screeningDate = rs.getDate("screening_date");
                Time startScreeningTime = rs.getTime("start_time");
                Time endScreeningTime = rs.getTime("end_time");
                Integer screenRoom = rs.getInt("screen_room");
                Integer availableSeats = rs.getInt("available_seats");
                Double ticketPrice = rs.getDouble("ticket_price");
                localScheduleList.add(new Schedule(scheduleId,movieName,screeningDate,startScreeningTime,endScreeningTime,screenRoom,availableSeats,ticketPrice));

            }
            con.close();
            return localScheduleList;
        }catch(SQLException e){
            System.err.println(e + "Error when fetching schedule from DB");
        }
        return localScheduleList;
    }



    private void initializeTableView() {
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<Schedule,String>("movieName"));
        screenDateColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Date>("screeningDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("endTime"));
        screenRoomColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("screenRoom"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("availableSeats"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Double>("ticketPrice"));

        todaysScheduleTableView.setItems(todaysScheduleList);


        parentVBox.setOnMousePressed(event -> {
            if (!todaysScheduleTableView.equals(event.getSource())) {
                todaysScheduleTableView.getSelectionModel().clearSelection();
            }});
    }
}
