package application.models;

public class Goal {
//    Final used for dynamically created constant objects on each cards
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "In Progress";
    public static final String COMPLETED = "Completed";

    private final String title;
    private final String description;
    private final String category;
    private final int targetUnits;
    private int currentUnits;
    private String status;

    public Goal(String title, String description, String category, int targetUnits, int initialCurrentUnits) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.targetUnits = targetUnits;
        this.currentUnits = initialCurrentUnits;
        updateStatus();
    }

    // GETTER: Used by the controller to READ the current progress
    public int getCurrentUnits() {

        return currentUnits;
    }

    // SETTER: Used by the controller to UPDATE the progress
    public void setCurrentUnits(int currentUnits) {
        this.currentUnits = currentUnits;
        updateStatus(); // IMPORTANT: This line ensures the goal status changes automatically
    }

    public int getTargetUnits() {
        return targetUnits;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {

        return description;
    }

    public String getCategory() {

        return category;
    }

    public String getStatus() {

        return status;
    }
    public void setStatus(String status) {

        this.status = status;
    }

    private void updateStatus() {
        if (getCurrentUnits() >= getTargetUnits()) {
            setStatus(COMPLETED);
        } else if (getCurrentUnits() > 0) {
            setStatus(IN_PROGRESS);
        } else {
            setStatus(PENDING);
        }
    }
}