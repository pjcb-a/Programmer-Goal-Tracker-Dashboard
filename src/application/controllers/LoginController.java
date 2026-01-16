package application.controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    // Hardcoded Admin Credentials
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "password";

    // Reference to Main application for scene switching
    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        errorLabel.setText(""); // Clear previous errors

        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            // Successful login, transition to Loading screen
            if (mainApp != null) {
                // Use the setNewScene helper to switch to the loading view
                mainApp.setNewScene(
                        "/application/views/loadingView.fxml",
                        "Welcome",
                        (LoadingController controller) -> {
                            // Pass the Main App reference to the LoadingController for the final transition
                            controller.setMainApp(mainApp);
                        }
                );
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }
}