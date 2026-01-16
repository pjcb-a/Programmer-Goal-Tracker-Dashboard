package application.controllers;

import application.Main;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class LoadingController {

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        // Start the transition logic immediately after the controller is set up
        startDashboardTransition();
    }

    private void startDashboardTransition() {
        // Simulate a 1.5 second loading time
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));

        delay.setOnFinished(_ -> {
            if (mainApp != null) {
                // Final scene switch to the Dashboard
                mainApp.setNewScene(
                        "/application/views/dashboard.fxml",
                        "Programmer Goal Tracker Dashboard",
                        (_controller) -> {
                            //lambda expression that represents a Consumer<T> crucial to initialize the controller
                        }
                );
            }
        });

        delay.play();
    }
}