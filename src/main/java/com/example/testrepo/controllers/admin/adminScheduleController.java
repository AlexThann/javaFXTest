package com.example.testrepo.controllers.admin;

import com.example.testrepo.models.Schedule;
import com.example.testrepo.util.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class adminScheduleController {

    private DbConnection dbConnection = new DbConnection();

    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();
    HashMap<Integer,Integer> roomAndSeatNumber = new HashMap<>();

    @FXML
    private Button editScheduleButton,deleteScheduleButton;

    @FXML
    private DatePicker screenDatePicker;

    @FXML
    private ComboBox<Integer> screenRoomComboBox;

    @FXML
    private TextField seatsTextField;

    @FXML
    private ComboBox<String> selectMovieComboBox;

    @FXML
    private TextField startTimeTextField,endTimeTextField;

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
    private TableColumn<Schedule,Double> ticketPriceColumn;

    @FXML
    private TableColumn<Schedule, Integer> seatsColumn;

    @FXML
    private TableColumn<Schedule, Integer> screenRoomColumn;

    @FXML
    private Label scheduleErrorLabel;

    @FXML
    private VBox parentVBox;

    @FXML
    public void initialize(){
        initializeScheduleTableView();
        initializeStaticMovieControls();
    }


    private void initializeStaticMovieControls(){
        initializeSpinner();
        TextFormatter<Integer> onlyIntsFormatter = new TextFormatter<>(change -> {
            // If the text contains anything other than digits, reject it
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            } else {
                return null; // Reject the change
            }
        });
        seatsTextField.setTextFormatter(onlyIntsFormatter);

        screenRoomComboBox.setOnAction((event) -> {
            Integer selectedRoom = screenRoomComboBox.getValue();
            seatsTextField.setText(String.valueOf(roomAndSeatNumber.get(selectedRoom)));

        });
        initializeDatePicker();
    }

    private void initializeDatePicker(){
        // This string converter is to set the date with a different format. dd/MM/yyyy
        StringConverter<LocalDate> converterDate = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        screenDatePicker.setConverter(converterDate);
        screenDatePicker.setValue(LocalDate.now());
        screenDatePicker.setEditable(false);

    }

    public void initializeScheduleUI(){
        // If scheduleList = fetchScheduleFromDB() the tableView still looks at the old list instead of the new one. You need setAll to reset the values
        scheduleList.setAll(fetchScheduleFromDB());
        initializeScreenRoomComboBox();
        initializeMovieNameComboBox();
        clearInputFields();
        scheduleErrorLabel.setText("");
    }

    private void initializeMovieNameComboBox(){
        ObservableList<String> movieNames = FXCollections.observableArrayList();
        try{
            Connection conn = dbConnection.getConnection();
            String getMovieNames="select title from movies;";
            PreparedStatement ps = conn.prepareStatement(getMovieNames);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                movieNames.add(rs.getString("title"));
            }
            selectMovieComboBox.setItems(movieNames);
            conn.close();
        }catch(SQLException e){
            System.err.println(e +" Failed to initialize combobox movie names");
        }


    }

    private void initializeScreenRoomComboBox(){
        HashMap<Integer,Integer> temp = new HashMap<>();
        try {
            Connection con = dbConnection.getConnection();
            String getScreenRoom = "select * from auditorium";
            PreparedStatement ps = con.prepareStatement(getScreenRoom);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Integer room = rs.getInt("screen_room");
                Integer seat = rs.getInt("max_seats");
                temp.put(room,seat);
            }
            roomAndSeatNumber = temp;
            screenRoomComboBox.setItems(FXCollections.observableArrayList(temp.keySet()));
            con.close();
        }catch(SQLException e){
            System.err.println(e + "Failed to initialize screen room combobox");
        }

    }

    private void initializeSpinner(){
        SpinnerValueFactory<Double> valueFac= new SpinnerValueFactory.DoubleSpinnerValueFactory(0,1000);
        ticketPriceSpinner.setValueFactory(valueFac);
        Pattern validEditingState = Pattern.compile("(([1-9][0-9]?)|0)?(\\.[0-9]{0,2})?");
        // This is used for the textFormatter for the amount spinner to have only values between 0.0-99.99.
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };


        TextFormatter<Double> textFormatter = new TextFormatter<>(filter);
        ticketPriceSpinner.getEditor().setTextFormatter(textFormatter);

    }

    private void initializeScheduleTableView(){
        movieNameColumn.setCellValueFactory(new PropertyValueFactory<Schedule,String>("movieName"));
        screenDateColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Date>("screeningDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Time>("endTime"));
        screenRoomColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("screenRoom"));
        seatsColumn.setCellValueFactory(new PropertyValueFactory<Schedule, Integer>("availableSeats"));
        ticketPriceColumn.setCellValueFactory(new PropertyValueFactory<Schedule,Double>("ticketPrice"));

        scheduleTableView.setItems(scheduleList);

        // Listener so when something is selected it appears on the text fields.
        scheduleTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                selectMovieComboBox.setValue(newValue.getMovieName());
                screenDatePicker.setValue(newValue.getScreeningDate().toLocalDate());
                startTimeTextField.setText(newValue.getStartTime().toString());
                endTimeTextField.setText(newValue.getEndTime().toString());
                screenRoomComboBox.setValue(newValue.getScreenRoom());
                ticketPriceSpinner.getValueFactory().setValue(newValue.getTicketPrice());
            }
        });

        parentVBox.setOnMousePressed(event -> {
            if (!scheduleTableView.equals(event.getSource())) {
                scheduleTableView.getSelectionModel().clearSelection();
            }});

        editScheduleButton.disableProperty().bind(scheduleTableView.getSelectionModel().selectedItemProperty().isNull());
        deleteScheduleButton.disableProperty().bind(scheduleTableView.getSelectionModel().selectedItemProperty().isNull());

    }
    @FXML
    private void editSchedule(){
        if (checkValidValues()) {
            return;
        }
        Schedule scheduleToEdit= scheduleTableView.getSelectionModel().getSelectedItem();
        try{
            Connection conn = dbConnection.getConnection();
            Integer movieId = getMovieIdByName(conn,selectMovieComboBox.getValue());
            if(movieId==null){
                System.err.println("movie not found not found");
            }
            String editQuery = "update schedule set movie_id=?, screening_date=?,start_time=?,end_time=?,screen_room=?,available_seats=?,ticket_price=? where schedule_id=?;";
            PreparedStatement ps = conn.prepareStatement(editQuery);
            ps.setInt(1,movieId);
            ps.setDate(2,Date.valueOf(screenDatePicker.getValue()));
            ps.setTime(3,Time.valueOf(startTimeTextField.getText()));
            ps.setTime(4,Time.valueOf(endTimeTextField.getText()));
            ps.setInt(5,screenRoomComboBox.getValue());
            ps.setInt(6,Integer.parseInt(seatsTextField.getText()));
            ps.setDouble(7,ticketPriceSpinner.getValue());
            ps.setInt(8,scheduleToEdit.getScheduleId());
            ps.executeUpdate();
            conn.close();
            scheduleList.setAll(fetchScheduleFromDB());
        }catch(SQLException e){
            System.err.println(e + "Failed to edit schedule");
        }
    }

    @FXML
    private void deleteSchedule(){
        Schedule scheduleToDelete= scheduleTableView.getSelectionModel().getSelectedItem();
        try{
            Connection con = dbConnection.getConnection();
            String deleteScheduleQuery = "delete from schedule where schedule_id=?;";
            PreparedStatement ps = con.prepareStatement(deleteScheduleQuery);
            ps.setInt(1,scheduleToDelete.getScheduleId());
            ps.executeUpdate();
            con.close();
            scheduleList.setAll(fetchScheduleFromDB());
        }catch(SQLException e){
            System.err.println(e + "Failed to delete schedule");
        }
    }



    @FXML
    private void addSchedule() {
        if (checkValidValues()) {
            return;
        }
        scheduleErrorLabel.setText("");
        try {
            Connection con = dbConnection.getConnection();
            Integer movieId = getMovieIdByName(con, selectMovieComboBox.getValue());
            if (movieId == null) {
                System.err.println("Movie not found: " + selectMovieComboBox.getValue());
                return;
            }

            insertSchedule(con, movieId);
            con.close();
            scheduleList.setAll(fetchScheduleFromDB());


        } catch (SQLException e) {
            System.err.println("Failed to add schedule: " + e.getMessage());
        }
    }

    private Integer getMovieIdByName(Connection con, String movieName) {
        String query = "SELECT movie_id FROM movies WHERE title = ?";
        try  {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, movieName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("movie_id");
            }
        }catch(SQLException e){
            System.err.println(e + "Failed to get movieId from name");
        }
        return null;
    }
    @FXML
    private void clearInputFields(){
        selectMovieComboBox.getSelectionModel().clearSelection();
        screenDatePicker.setValue(LocalDate.now());
        startTimeTextField.setText("");
        endTimeTextField.setText("");
        screenRoomComboBox.getSelectionModel().clearSelection();
        seatsTextField.setText("");
        ticketPriceSpinner.getValueFactory().setValue(0.0);
    }

    private void insertSchedule(Connection con, int movieId) {
        String insertQuery = """
        insert into schedule (movie_id, screening_date, start_time, end_time, screen_room, available_seats, ticket_price)
        values (?, ?, ?, ?, ?, ?, ?);
        """;
        try {
            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setInt(1, movieId);
            ps.setDate(2, Date.valueOf(screenDatePicker.getValue()));
            ps.setTime(3, Time.valueOf(startTimeTextField.getText()));
            ps.setTime(4, Time.valueOf(endTimeTextField.getText()));
            ps.setInt(5, screenRoomComboBox.getValue());
            ps.setInt(6, Integer.parseInt(seatsTextField.getText()));
            ps.setDouble(7, ticketPriceSpinner.getValue());
            ps.executeUpdate();
        }catch(SQLException e){
            System.err.println(e + "Failed to insert schedule");
        }
    }

    private boolean checkValidValues(){
        Pattern validTimePattern = Pattern.compile("([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)");
        if(selectMovieComboBox.getValue() == null){
            scheduleErrorLabel.setText("Please select a movie");
            return true;
        }else if(screenDatePicker.getValue() == null){
            scheduleErrorLabel.setText("Please select a screening date");
            return true;
        }else if(startTimeTextField.getText().isEmpty() || !validTimePattern.matcher(startTimeTextField.getText()).matches()){
            scheduleErrorLabel.setText("Please select a valid starting time (HH:MM:SS)");
            return true;
        }else if(endTimeTextField.getText().isEmpty() ||  !validTimePattern.matcher(endTimeTextField.getText()).matches()){
            scheduleErrorLabel.setText("Please select a valid ending time (HH:MM:SS)");
            return true;
        }else if(ticketPriceSpinner.getValue() == null){
            scheduleErrorLabel.setText("Please select a ticket price");
            return true;
        }else if(screenRoomComboBox.getValue() == null){
            scheduleErrorLabel.setText("Please select a screening room");
            return true;
        }else if(seatsTextField.getText().isEmpty()){
            scheduleErrorLabel.setText("Please select available seats");
            return true;
        }
        return false;
    }



    // COULD BE PUT IN A ScheduleService class which handles all DB actions !SOS!
    // Fetches schedule table from DB and initializes combo box based on the name
    private ObservableList<Schedule> fetchScheduleFromDB(){
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


}
