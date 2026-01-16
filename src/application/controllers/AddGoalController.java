package application.controllers;

import application.models.Goal;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class AddGoalController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField unitsField;
    @FXML private Label errorLabel;

    // ---- SOME PARTS AIDED BY AI ---- // Callback function to send the new Goal object back to the dashboard controller
    private Consumer<Goal> goalAddedCallback;
    private Stage dialogStage;

    @FXML
    public void initialize() {
        // Initialize error label state
        errorLabel.setText("");
        errorLabel.setStyle("-fx-text-fill: #D32F2F; -fx-font-weight: bold;");

        // for categories
        categoryComboBox.getItems().addAll("Learning", "Project", "Practice", "Maintenance", "Other");
        categoryComboBox.getSelectionModel().select("Learning"); // Set a default

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // callback function which the main dashboard controller provides.
    public void setGoalAddedCallback(Consumer<Goal> callback) {
        this.goalAddedCallback = callback;
    }

    @FXML
    private void handleAddGoal() {
        // Clear previous error messages
        errorLabel.setText("");

        if (!validateInput()) {
            return; // Stop if validation fails (error message is already set)
        }

        try {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String category = categoryComboBox.getValue();
            int units = Integer.parseInt(unitsField.getText());

            // 1. Create new Goal object
            Goal newGoal = new Goal(title, description, category, units, 0);

            // 2. Execute the callback to send the goal back to the dashboard
            if (goalAddedCallback != null) {
                goalAddedCallback.accept(newGoal);
            }

            // 3. Close the modal
            dialogStage.close();

        } catch (NumberFormatException e) {
            // Fallback for unexpected number conversion errors a part of constraint-ish
            errorLabel.setText("System Error: Target units format is incorrect.");
        }
    }

//    Input for constraint cases on each desired field on the addGoalView
    private boolean validateInput() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "! Goal Title cannot be empty.\n";
        }
        if (unitsField.getText() == null || unitsField.getText().trim().isEmpty()) {
            errorMessage += "! Target Units cannot be empty.\n";
        } else {
            try {
                if (Integer.parseInt(unitsField.getText().trim()) <= 0) {
                    errorMessage += "! Target Units must be greater than zero.\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "! Target Units must be a valid number.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            errorLabel.setText(errorMessage.trim());
            return false;
        }
    }

}
