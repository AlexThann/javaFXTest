package com.example.testrepo.controllers.admin;

import com.example.testrepo.controllers.admin.adminMoviesController;
import com.example.testrepo.controllers.admin.adminDashboardController;
import com.example.testrepo.controllers.admin.adminSalesController;
import com.example.testrepo.controllers.admin.adminScheduleController;
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
    private AnchorPane dashboardPane,schedulePane,moviesPane,salesPane;

    private Node currentVisibleOption;

    // Controllers for each sub-menu
    @FXML
    private adminMoviesController moviesUIController;

    @FXML
    private adminSalesController salesUIController;

    @FXML
    private adminScheduleController scheduleUIController;

    @FXML
    private adminDashboardController dashboardUIController;


    @FXML
    public void initialize() {
        // lookupAll searches the node for children with CSS selector menu-icons.
        // Result is Set<node>
        // lambda expression to call the function
        rootAnchorPane.lookupAll(".menu-icons").forEach(node -> addHoverScaleEffect(node));
        currentVisibleOption=moviesPane;
        //moviesUIController.setRootAnchorPane(rootAnchorPane);
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
        changeMenu(dashboardPane);
    }
    @FXML
    private void showMovies(){
        changeMenu(moviesPane);

    }
    @FXML
    private void showSchedule(){
        changeMenu(schedulePane);
    }

    @FXML
    private void showSales(){
        changeMenu(salesPane);
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
