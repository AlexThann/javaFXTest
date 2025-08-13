module com.example.testrepo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;


    opens com.example.testrepo to javafx.fxml;
    exports com.example.testrepo;
    exports com.example.testrepo.controllers;
    opens com.example.testrepo.controllers to javafx.fxml;
    exports com.example.testrepo.controllers.user;
    opens com.example.testrepo.controllers.user to javafx.fxml;
    exports com.example.testrepo.controllers.admin;
    opens com.example.testrepo.controllers.admin to javafx.fxml;
}