package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Movie;
import com.example.testrepo.models.Schedule;
import com.example.testrepo.util.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class adminScheduleController {

    DbConnection dbConnection = new DbConnection();

    ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    @FXML
    private TextField endTimeTextField;

    @FXML
    private DatePicker screenDatePicker;

    @FXML
    private ComboBox<Integer> screenRoomComboBox;

    @FXML
    private TextField seatsTextField;

    @FXML
    private ComboBox<Movie> selectMovieComboBox;

    @FXML
    private TextField startTimeTextField;

    @FXML
    private Spinner<Double> ticketPriceSpinner;

    @FXML
    private TableView<Schedule> scheduleTableView;

    @FXML
    private TableColumn<Schedule, String> movieNameColumn;

    @FXML
    private TableColumn<Schedule, Date> screenDateColumn;

    @FXML
    private TableColumn<Schedule, Time> startTimeColumn,endTimeColumn;

    @FXML
    private TableColumn<Schedule,Float> ticketPriceColumn;

    @FXML
    private TableColumn<Schedule, Integer> seatsColumn;

    @FXML
    private TableColumn<Schedule, Integer> screenRoomColumn;


    @FXML
    public void initialize(){
        initializeScheduleTableView();
    }

    public void initializeScheduleTableView(){
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<Schedule,String>("movieName"));
        screenDateColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Date>("screeningDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("endTime"));
        screenRoomColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("screenRoom"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("availableSeats"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Float>("ticketPrice"));

        scheduleTableView.setItems(scheduleList);
     /*   scheduleList.add(
                new Schedule(1,"Marvel",        Date.valueOf("2025-08-15"),        // java.sql.Date
                        Time.valueOf("18:30:00"),          // java.sql.Time
                        Time.valueOf("21:00:00"), 4,4,4.1f)
        );*/

    }

    public void initializeScheduleUI(){
        // If scheduleList = fetchScheduleFromDB() the tableView still looks at the old list instead of the new one. You need setAll to reset the values
        scheduleList.setAll(fetchScheduleFromDB());

    }

    // COULD BE PUT IN A ScheduleService class which handles all DB actions !SOS!
    public ObservableList<Schedule> fetchScheduleFromDB(){
        ObservableList<Schedule> localScheduleList = FXCollections.observableArrayList();
        try{
            Connection con = dbConnection.getConnection();
            String fetchSchedule = "select s.schedule_id, m.title,s.screening_date,s.start_time,s.end_time, s.screen_room,s.available_seats,s.ticket_price from schedule s,movies m where s.movie_id = m.movie_id;";
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
                Float ticketPrice = rs.getFloat("ticket_price");
                localScheduleList.add(new Schedule(scheduleId,movieName,screeningDate,startScreeningTime,endScreeningTime,screenRoom,availableSeats,ticketPrice));
            }
            con.close();
            return localScheduleList;
        }catch(SQLException e){
            System.err.println(e + "Error when fetching schedule from DB");
        }
        return localScheduleList;
    }


}
