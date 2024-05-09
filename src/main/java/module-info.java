module com.example.exchage {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.exchage to javafx.fxml;
    exports com.example.exchage;
}