module com.example.testrepo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.testrepo to javafx.fxml;
    exports com.example.testrepo;
    exports com.example.testrepo.controllers;
    opens com.example.testrepo.controllers to javafx.fxml;
}