module com.example.testrepo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.testrepo to javafx.fxml;
    exports com.example.testrepo;
}