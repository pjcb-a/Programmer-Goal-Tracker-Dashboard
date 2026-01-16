package application;

import application.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.util.Objects;
import java.util.function.Consumer;

public class Main extends Application {   //Inheritance

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        // FIX 1: Initialize the primaryStage field so setNewScene can use it
        this.primaryStage = stage;

        // Load the initial scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/loginView.fxml"));
        Scene scene = new Scene(loader.load());

        // Load the stylesheet
        String stylesheetPath = Objects.requireNonNull(getClass().getResource("/application/views/styles.css")).toExternalForm();
        scene.getStylesheets().add(stylesheetPath);

        // Load application icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResource("/assets/logo.png")).toExternalForm());

        // Get and set up controller
        LoginController controller = loader.getController();
        controller.setMainApp(this); // Pass Main reference for navigation

        // Configure the stage
        stage.setTitle("Programmer Goal Tracker Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(icon);
        stage.show();
    }

    //    Aided with AI for a more flexible way to handle scene switching.
    public <T> void setNewScene(String fxmlPath, String title, Consumer<T> controllerSetup) {
        // FIX 2: The entire method body, including the try-catch block, must be inside the curly braces {}
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());

            // FIX 3: Standardized stylesheet path to match the one used in start()
            newScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/application/views/styles.css")).toExternalForm());

            T controller = loader.getController();
            if(controllerSetup != null) {
                controllerSetup.accept(controller);
            }

            primaryStage.setTitle(title);
            primaryStage.setScene(newScene);
            primaryStage.show();
        } catch(Exception e){
            // Print stack trace for debugging purposes
            System.err.println("Failed to load scene: " + fxmlPath);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}