package com.example.testrepo.controllers;

import javafx.fxml.FXML;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

// new for scaling
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import java.util.List;
import java.util.ArrayList;

public class adminController {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private VBox dashboardVBox,scheduleVBox,salesVBox,MovieEditingVBox,MovieEditingTextFieldsVBox;

    @FXML
    private HBox moviesHBox,movieEditingButtonsHBox;

    private Node currentVisibleOption;

    private final double baseWidth = 800;  // your design width
    private final double baseHeight = 600; // your design height

    @FXML
    public void initialize() {
        // lookupAll searches the node for children with CSS selector menu-icons.
        // Result is Set<node>
        // lambda expression to call the function
        rootAnchorPane.lookupAll(".menu-icons").forEach(node -> addHoverScaleEffect(node));
        currentVisibleOption=moviesHBox;
        /// //////////////////////////////////////
        ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> {
            double scaleY = rootAnchorPane.getHeight() / baseHeight;
            double scaleX = rootAnchorPane.getWidth() / baseWidth;
            MovieEditingTextFieldsVBox.setScaleY(scaleY);// text fields change only height
            movieEditingButtonsHBox.setScaleY(scaleY); // buttons change height and width
            movieEditingButtonsHBox.setScaleX(scaleX);
        };
        rootAnchorPane.heightProperty().addListener(sizeListener);
        // Initial scale
        double initialScale = rootAnchorPane.getHeight() / baseHeight;
        MovieEditingTextFieldsVBox.setScaleY(initialScale); // textfields change only height

        movieEditingButtonsHBox.setScaleY(initialScale);// buttons change w and h
        movieEditingButtonsHBox.setScaleX(initialScale);
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
