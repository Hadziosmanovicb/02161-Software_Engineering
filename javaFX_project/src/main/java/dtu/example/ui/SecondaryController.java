package dtu.example.ui;

import dtu.example.ui.domain.ProjectManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SecondaryController {

    @FXML private VBox mainContainer;

    private boolean projectUIVisible = false;

    private final ProjectManager projectManager = PrimaryController.getProjectManager();

    @FXML
private void handleCreateProject() {
    if (projectUIVisible) return;

    TextField projectNameField = new TextField();
    projectNameField.setPromptText("Projekt navn");

    TextField projectLeaderField = new TextField();
    projectLeaderField.setPromptText("Projektleder initialer");

    Button confirmButton = new Button("Bekræft");
    confirmButton.setOnAction(e -> {
        String name = projectNameField.getText();
        String leader = projectLeaderField.getText();
        if (!name.isEmpty() && !leader.isEmpty()) {
            projectManager.createProject(name, leader);
            System.out.println("Projekt oprettet: " + name + " — Leder: " + leader);
            mainContainer.getChildren().removeAll(projectNameField, projectLeaderField, confirmButton);
            projectUIVisible = false;
        }
    });

    int insertIndex = findButtonIndex("Opret Projekt");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectNameField);
        mainContainer.getChildren().add(insertIndex + 2, projectLeaderField);
        mainContainer.getChildren().add(insertIndex + 3, confirmButton);
        projectUIVisible = true;
    }
}
    private int findButtonIndex(String buttonText) {
        for (int i = 0; i < mainContainer.getChildren().size(); i++) {
            if (mainContainer.getChildren().get(i) instanceof Button btn &&
                btn.getText().equals(buttonText)) {
                return i;
            }
        }
        return -1;
    }

    @FXML
    private void handleLogout() throws Exception {
        App.setRoot("primary");
    }

    // Deaktiver alle andre knapper
    private boolean activityUIVisible = false;

@FXML
private void handleAddActivity() {
    if (activityUIVisible) return;

    // Projektvalg
    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getAllProjectNames());

    // Aktivitet-navn
    TextField activityNameField = new TextField();
    activityNameField.setPromptText("Aktivitetsnavn");

    // Bekræft-knap
    Button confirmButton = new Button("Bekræft");
    confirmButton.setOnAction(e -> {
        String selectedProject = projectDropdown.getValue();
        String activityName = activityNameField.getText();
        if (selectedProject != null && !activityName.isEmpty()) {
            System.out.println("Aktivitet '" + activityName + "' oprettet til projekt: " + selectedProject);
            mainContainer.getChildren().removeAll(projectDropdown, activityNameField, confirmButton);
            activityUIVisible = false;
        }
    });

    // Indsæt i GUI
    int insertIndex = findButtonIndex("Tilføj Aktivitet");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
        mainContainer.getChildren().add(insertIndex + 2, activityNameField);
        mainContainer.getChildren().add(insertIndex + 3, confirmButton);
        activityUIVisible = true;
    }
}

    @FXML private void handleAddEmployee() {}
    @FXML private void handleAssignEmployee() {}
    @FXML private void handleLogTime() {}
    @FXML private void handleShowMyActivities() {}
}
