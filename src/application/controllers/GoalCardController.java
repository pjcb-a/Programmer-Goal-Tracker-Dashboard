package application.controllers;

import application.models.Goal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.util.function.Consumer;

//  Controller for a single, detailed goal card.
//  Handles display logic, progress updates, and action buttons for one Goal object.

public class GoalCardController {

    @FXML private Label titleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label unitProgressLabel;
    @FXML private Label percentageLabel;
    @FXML private ProgressBar progressBar;

    private Goal goal;
    // Handlers for cross-controller communication FUNCTIONAL INTERFACES
    private Consumer<Goal> deleteGoalHandler;
    private Runnable refreshHandler;

    public void setGoal(Goal goal) {
        this.goal = goal;
        // The goal object manages its own progress and status via getter/setter
        updateUI();
    }
//    ---------------------------------- CREATED WITH AI ---------------------------------------------------
//   Function handler for handling goal deletion on the main screen.
 
    public void setDeleteGoalHandler(Consumer<Goal> handler) {
        this.deleteGoalHandler = handler;
    }

//    Refresher for almost realtime update on the main dashboard
    public void setRefreshHandler(Runnable handler) {
        this.refreshHandler = handler;
    }
//    ---------------------------------------------------------------------------------------------------

//      Updates all UI elements based on the current Goal object state.
//      This method fixes the usage of the old, non-existent variables.

    private void updateUI() {
        if (goal == null) return;

        titleLabel.setText(goal.getTitle());
        descriptionLabel.setText(goal.getDescription());
        categoryLabel.setText(goal.getCategory());

        // Progress Calculation
        double progressRatio = (double) goal.getCurrentUnits() / goal.getTargetUnits();

        // Ensure progress doesn't exceed 1.0 (100%)
        if (progressRatio > 1.0) progressRatio = 1.0;

        unitProgressLabel.setText(goal.getCurrentUnits() + " / " + goal.getTargetUnits());
        percentageLabel.setText(String.format("%.0f%%", progressRatio * 100));

        progressBar.setProgress(progressRatio);
    }

    @FXML
    private void handleIncreaseProgress() {
        int newUnits = goal.getCurrentUnits() + 1;
        if (newUnits > goal.getTargetUnits()) {
            newUnits = goal.getTargetUnits();
        }
        // Update the model (Goal object)
        goal.setCurrentUnits(newUnits);

        // Update the view
        updateUI();

        // Notify dashboard to refresh stats (calls the refreshHandler)
        if (refreshHandler != null) {
            refreshHandler.run();
        }
    }

//    Same as the function above just decrementing it
    @FXML
    private void handleDecreaseProgress() {
        int newUnits = goal.getCurrentUnits() - 1;
        if (newUnits < 0) {
            newUnits = 0;
        }
        goal.setCurrentUnits(newUnits);

        updateUI();

        if (refreshHandler != null) {
            refreshHandler.run();
        }
    }

    @FXML
    private void handleDeleteGoal() {
        if (deleteGoalHandler != null && goal != null) {
            deleteGoalHandler.accept(goal);
        }
    }

}
