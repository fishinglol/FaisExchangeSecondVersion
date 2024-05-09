package com.example.exchage;// This package declaration indicates that this Java file belongs to the "com.example.exchange" package

// Importing necessary JavaFX classes
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Importing necessary IO and utility classes
import java.io.IOException;

// The main class extending the Application class provided by JavaFX
public class HelloApplication extends Application {

    // The start method is called when the JavaFX application starts
    @Override
    public void start(Stage stage) throws IOException {
        // FXMLLoader loads the FXML file that describes the UI
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Creating a new Scene with the loaded FXML and setting its size
        Scene scene = new Scene(fxmlLoader.load(), 250, 270);

        // Setting the title of the Stage
        stage.setTitle("Hello Exchange!");

        // Setting the scene to the Stage and displaying it
        stage.setScene(scene);
        stage.show();
    }

    // The main method, entry point of the Java program
    public static void main(String[] args) {
        // Launching the JavaFX application
        launch();
    }
}
