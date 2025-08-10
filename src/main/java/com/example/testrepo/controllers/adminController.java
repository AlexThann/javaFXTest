package com.example.testrepo.controllers;

import javafx.fxml.FXML;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class adminController {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    public void initialize() {
        // lookupAll searches the node for children with CSS selector menu-icons.
        // Result is Set<node>
        // lambda expression to call the function
        rootAnchorPane.lookupAll(".menu-icons").forEach(node -> addHoverScaleEffect(node));
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
}
