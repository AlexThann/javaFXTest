package com.example.testrepo.controllers;

import javafx.fxml.FXML;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class adminController {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private VBox dashboardVBox,scheduleVBox,salesVBox;

    @FXML
    private HBox moviesHBox;

    private Node currentVisibleOption;


    @FXML
    public void initialize() {
        // lookupAll searches the node for children with CSS selector menu-icons.
        // Result is Set<node>
        // lambda expression to call the function
        rootAnchorPane.lookupAll(".menu-icons").forEach(node -> addHoverScaleEffect(node));
        currentVisibleOption=moviesHBox;
    }

    private void changeMenu(Node showMenu){
        currentVisibleOption.setVisible(false);
        currentVisibleOption.setManaged(false);
        showMenu.setVisible(true);
        showMenu.setManaged(true);
        currentVisibleOption=showMenu;
    }
    @FXML
    private void showDashboard(){
        changeMenu(dashboardVBox);
    }
    @FXML
    private void showMovies(){
        changeMenu(moviesHBox);
    }
    @FXML
    private void showSchedule(){
        changeMenu(scheduleVBox);
    }

    @FXML
    private void showSales(){
        changeMenu(salesVBox);
    }

    // Func for button animation
    private void addHoverScaleEffect(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });

        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
    @FXML
    private void logout(){
        try{
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/example/testrepo/fxml/login-screen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(IOException e){
            System.out.println(e);
        }

    }
}
