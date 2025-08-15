package com.example.testrepo.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.sql.Time;

public class Schedule {
    private Integer scheduleId;
    private String movieName;
    private Date screeningDate;
    private Time startTime;
    private Time endTime;
    private Integer screenRoom;
    private Integer availableSeats;
    private Double ticketPrice;


    public Schedule(Integer scheduleId, String movieName, Date screeningDate, Time startTime, Time endTime, Integer screenRoom, Integer availableSeats, Double ticketPrice) {
        this.scheduleId = scheduleId;
        this.movieName = movieName;
        this.screeningDate = screeningDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.screenRoom = screenRoom;
        this.availableSeats = availableSeats;
        this.ticketPrice = ticketPrice;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Integer getScreenRoom() {
        return screenRoom;
    }

    public void setScreenRoom(Integer screenRoom) {
        this.screenRoom = screenRoom;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

}
