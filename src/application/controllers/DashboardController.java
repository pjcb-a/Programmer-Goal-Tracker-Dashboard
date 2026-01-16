package application.controllers;

import application.models.Goal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

// Handles calculating goal statistics, dynamically loading cards, and managing the Add Goal workflow.
public class DashboardController {
    @FXML
    private HBox cardRowContainer;
    @FXML
    private FlowPane goalListContainer;
    private ArrayList<Goal> allGoalsList;
    @FXML
    public void initialize() {
        allGoalsList = new ArrayList<>(fetchAllGoalsFromService());
        refreshDashboardViews();
    }

//      Triggers the recalculation of stats and the reloading of all card views. --- SOME PARTS AIDED WITH AI FOR ERROR HANDLING AND DEBUGGING WHILE IMPLEMENTING ---

    private void refreshDashboardViews() {
        // DEBUG LINE: Confirm that the refresh is being triggered by the GoalCard buttons
        System.out.println("DEBUG: Recalculating all stat cards nd reloading goal cards.");

        if(goalListContainer == null) return;
        // Refresh Stat Cards
        List<Map<String, Object>> cardsData = calculateCardData(allGoalsList);
        loadAndDisplayStatCards(cardsData);

        // Refresh Detailed Goal Cards
        loadAndDisplayGoalCards(allGoalsList);
    }

    // ----------------------------------------------------------------------
    // 1. ADD GOAL MODAL LOGIC ---- Some parts and logic are aided by AI ----
    // ----------------------------------------------------------------------

    @FXML
    private void openAddGoalModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/AddGoalView.fxml"));
            Parent root = loader.load();
            Image icon = new Image(Objects.requireNonNull(getClass().getResource("/assets/logo.png")).toExternalForm());

            AddGoalController addController = loader.getController();

            Stage modalStage = new Stage();
            modalStage.setTitle("Add New Goal");
            modalStage.getIcons().add(icon);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setResizable(false);
            addController.setDialogStage(modalStage);
            addController.setGoalAddedCallback(this::addNewGoalToListAndRefresh);

            Scene scene = new Scene(root);
            modalStage.setScene(scene);
            modalStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error loading AddGoalView.fxml: " + e.getMessage());
        }
    }

    private void addNewGoalToListAndRefresh(Goal newGoal) {
        allGoalsList.add(newGoal);
        refreshDashboardViews();
    }

    private void deleteGoalFromListAndRefresh(Goal goalToDelete) {
        // If the goal object is found in the list, it is removed.
        boolean wasRemoved = allGoalsList.remove(goalToDelete);

//        For Debugging purposes
        if (wasRemoved) {
            System.out.println("Goal removed: " + goalToDelete.getTitle());
        } else {
            System.out.println("Goal not found for removal.");
        }

        // Refresh the entire dashboard UI to reflect the deletion and recalculate stats
        refreshDashboardViews();
    }

    // ----------------------------------------------------------------------
    // 2. DATA / STATS LOGIC (Uses allGoalsList)
    // ----------------------------------------------------------------------

//    Mock-up database
    private List<Goal> fetchAllGoalsFromService() {
        return Arrays.asList(
                new Goal("Pass OOP Finals!", "90 grade or above please", "Education", 90, 80),
                new Goal("Build Portfolio Site", "Design and deploy a site", "Web Dev", 10, 5),
                new Goal("Create another Website", "Enhance skills ", "Education", 12, 12),
                new Goal("Fix App Bug", "Address critical bug in production", "Maintenance", 1, 0),
                new Goal("Answer Leet code #3578", "Understand the problem and answer it finally", "Others", 5, 2),
                new Goal("Create GUI Dashboard", "Create a simple GUI using the lessons learned in OOP", "Education", 90, 50)
        );
    }

    private List<Map<String, Object>> calculateCardData(List<Goal> allGoals) {
        int totalGoals = allGoals.size();
        int completedCount = 0;
        int inProgressCount = 0;
        int pendingCount = 0;

        for (Goal goal : allGoals) {
            String status = goal.getStatus();

            if (Goal.COMPLETED.equals(status)) {
                completedCount++;
            } else if (Goal.IN_PROGRESS.equals(status)) {
                inProgressCount++;
            } else if (Goal.PENDING.equals(status)) {
                pendingCount++;
            }
        }

//       ---- The logic for the main section cards ----
        List<Map<String, Object>> cardDataList = new ArrayList<>();
        cardDataList.add(Map.of("title", "Total Goals", "count", totalGoals));
        cardDataList.add(Map.of("title", "Completed", "count", completedCount));
        cardDataList.add(Map.of("title", "In Progress", "count", inProgressCount));
        cardDataList.add(Map.of("title", "Pending", "count", pendingCount));

        return cardDataList;
    }

    private void loadAndDisplayStatCards(List<Map<String, Object>> dataList) {

        cardRowContainer.getChildren().clear();

        for (Map<String, Object> data : dataList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/StatCard.fxml"));
                Node cardNode = loader.load();

                // Uses lookup() since StatCard has no controller
                Label titleLabel = (Label) cardNode.lookup("#cardTitleLabel");
                Label numberLabel = (Label) cardNode.lookup("#cardNumberLabel");

                String title = (String) data.get("title");
                Integer count = (Integer) data.get("count");

                if (titleLabel != null) {
                    titleLabel.setText(title);
                }
                if (numberLabel != null) {
                    numberLabel.setText(String.valueOf(count));
                }

                cardRowContainer.getChildren().add(cardNode);

            } catch (IOException e) {
                System.err.println("Error loading StatCard.fxml: " + e.getMessage());
            }
        }
    }

    // ----------------------------------------------------------------------
    // 3. DETAILED GOAL CARD LOGIC (Uses allGoalsList)
    // ----------------------------------------------------------------------

    private void loadAndDisplayGoalCards(List<Goal> allGoals) {
        if(goalListContainer == null) return;

        goalListContainer.getChildren().clear();

//-----------------------------------------------------------------------------------------------------------------
//        CREATED WITH AI (NUMBER 1 & 2)
        // 1. Set up the refresh handler (Runnable) - This is the core mechanism to update stat cards
        Runnable refreshCallback = this::refreshDashboardViews;

        // 2. Set up the delete handler (Consumer<Goal>)
        Consumer<Goal> deleteCallback = this::deleteGoalFromListAndRefresh;
//-----------------------------------------------------------------------------------------------------------------

        for (Goal goal : allGoals) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/views/GoalCard.fxml"));
                Node goalCardNode = loader.load();

                GoalCardController cardController = loader.getController();
                cardController.setDeleteGoalHandler(deleteCallback);

                cardController.setRefreshHandler(refreshCallback);

                cardController.setGoal(goal);

                // Add the card to the FlowPane container
                goalListContainer.getChildren().add(goalCardNode);

            } catch (IOException e) {
                System.err.println("Error loading GoalCard.fxml: " + e.getMessage());
            }
        }
    }
}