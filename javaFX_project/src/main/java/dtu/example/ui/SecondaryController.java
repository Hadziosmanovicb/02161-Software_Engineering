package dtu.example.ui;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import dtu.example.ui.domain.Activity;
import dtu.example.ui.domain.Employee;
import dtu.example.ui.domain.ProjectManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
        projectDropdown.getItems().addAll(projectManager.getAllProjects());
    
        // Aktivitet-navn
        TextField activityNameField = new TextField();
        activityNameField.setPromptText("Aktivitetsnavn");
    
        // Bekræft-knap
        Button confirmButton = new Button("Bekræft");
        confirmButton.setOnAction(e -> {
            String selectedProject = projectDropdown.getValue();
            String activityName = activityNameField.getText();
            if (selectedProject != null && !activityName.isEmpty()) {
                // HER bliver det gemt
                projectManager.addActivityToProject(selectedProject, activityName);
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
    

    @FXML
private void handleAddEmployee() {
    
    if (projectUIVisible || activityUIVisible) return;
    projectUIVisible = true; 

    // Opret input-felt
    TextField initialsField = new TextField();
    initialsField.setPromptText("Initialer på medarbejder");

    Button confirmButton = new Button("Bekræft");
    confirmButton.setOnAction(e -> {
        String initials = initialsField.getText().trim();
        if (!initials.isEmpty()) {
            projectManager.addEmployee(new Employee(initials));
            System.out.println("Medarbejder tilføjet: " + initials);
            mainContainer.getChildren().removeAll(initialsField, confirmButton);
            projectUIVisible = false;
        }
    });

    int insertIndex = findButtonIndex("Tilføj Medarbejder");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, initialsField);
        mainContainer.getChildren().add(insertIndex + 2, confirmButton);
    }
}

    @FXML
private void handleAssignEmployee() {
    if (projectUIVisible || activityUIVisible) return;
    projectUIVisible = true;

    // Projekt dropdown
    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getAllProjects());


    ComboBox<String> activityDropdown = new ComboBox<>();
    activityDropdown.setPromptText("Vælg aktivitet");
    activityDropdown.setDisable(true); 

   
    projectDropdown.setOnAction(e -> {
        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            activityDropdown.getItems().clear();
            projectManager.getActivities(selectedProject).forEach(activity ->
                activityDropdown.getItems().add(activity.getName())
            );
            activityDropdown.setDisable(false);
        }
    });

    // Medarbejder-dropdown
    ComboBox<String> employeeDropdown = new ComboBox<>();
    employeeDropdown.setPromptText("Vælg medarbejder");
    projectManager.getAllEmployees().forEach(emp ->
        employeeDropdown.getItems().add(emp.getInitials())
    );

    // Bekræft-knap
    Button confirmButton = new Button("Bekræft");
    confirmButton.setOnAction(e -> {
        String project = projectDropdown.getValue();
        String activityName = activityDropdown.getValue();
        String employeeInitials = employeeDropdown.getValue();

        if (project != null && activityName != null && employeeInitials != null) {
            // Find aktiviteten
            for (Activity activity : projectManager.getActivities(project)) {
                if (activity.getName().equals(activityName)) {
                    activity.assignEmployee(new Employee(employeeInitials));
                    System.out.println("Medarbejder " + employeeInitials + " tildelt til aktivitet " + activityName + " i projekt " + project);
                    break;
                }
            }

            // Ryd GUI
            mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, employeeDropdown, confirmButton);
            projectUIVisible = false;
        }
    });

    int insertIndex = findButtonIndex("Tildel Medarbejder til Aktivitet");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
        mainContainer.getChildren().add(insertIndex + 2, activityDropdown);
        mainContainer.getChildren().add(insertIndex + 3, employeeDropdown);
        mainContainer.getChildren().add(insertIndex + 4, confirmButton);
    }
}

    @FXML
private void handleLogTime() {
    if (projectUIVisible || activityUIVisible) return;
    activityUIVisible = true;

    String loggedIn = projectManager.getLoggedInUser();

    // Projekt dropdown
    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");

    Set<String> relevantProjects = new HashSet<>();
    for (String project : projectManager.getAllProjects()) {
        for (Activity act : projectManager.getActivities(project)) {
            if (act.getAssignedEmployees().stream()
                   .anyMatch(emp -> emp.getInitials().equals(loggedIn))) {
                relevantProjects.add(project);
                break;
            }
        }
    }

    projectDropdown.getItems().addAll(relevantProjects);

    // Aktivitet dropdown
    ComboBox<String> activityDropdown = new ComboBox<>();
    activityDropdown.setPromptText("Vælg aktivitet");
    activityDropdown.setDisable(true);

    projectDropdown.setOnAction(e -> {
        activityDropdown.getItems().clear();
        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            for (Activity act : projectManager.getActivities(selectedProject)) {
                if (act.getAssignedEmployees().stream()
                       .anyMatch(emp -> emp.getInitials().equals(loggedIn))) {
                    activityDropdown.getItems().add(act.getName());
                }
            }
            activityDropdown.setDisable(false);
        }
    });

    // Dato
    DatePicker datePicker = new DatePicker();
    datePicker.setPromptText("Vælg dato");

    // Timer
    TextField hoursField = new TextField();
    hoursField.setPromptText("Antal timer");

    // Bekræft
    Button confirmButton = new Button("Bekræft");
    confirmButton.setOnAction(e -> {
        String project = projectDropdown.getValue();
        String activityName = activityDropdown.getValue();
        LocalDate date = datePicker.getValue();
        int hours;

        try {
            hours = Integer.parseInt(hoursField.getText());
        } catch (NumberFormatException ex) {
            System.out.println("Ugyldigt antal timer");
            return;
        }

        if (project != null && activityName != null && hours > 0 && date != null) {
            for (Activity act : projectManager.getActivities(project)) {
                if (act.getName().equals(activityName)) {
                    act.registerTime(new Employee(loggedIn), date, hours);
                    System.out.println("Tid registreret: " + hours + " timer på " + activityName + " (" + date + ")");
                    break;
                }
            }
            mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, datePicker, hoursField, confirmButton);
            activityUIVisible = false;
        }
    });

    int insertIndex = findButtonIndex("Registrer Tid");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
        mainContainer.getChildren().add(insertIndex + 2, activityDropdown);
        mainContainer.getChildren().add(insertIndex + 3, datePicker);
        mainContainer.getChildren().add(insertIndex + 4, hoursField);
        mainContainer.getChildren().add(insertIndex + 5, confirmButton);
    }
}

   @FXML
private void handleShowMyActivities() {
    if (projectUIVisible || activityUIVisible) return;
    activityUIVisible = true;

    String loggedIn = projectManager.getLoggedInUser();

    VBox activityList = new VBox(10);
    activityList.setPadding(new Insets(10));
    activityList.setStyle("-fx-background-color: #e9e9e9; -fx-border-color: #ccc; -fx-border-radius: 5px;");
    
    for (String project : projectManager.getAllProjects()) {
        for (Activity activity : projectManager.getActivities(project)) {
            boolean isAssigned = activity.getAssignedEmployees().stream()
                .anyMatch(emp -> emp.getInitials().equals(loggedIn));

            if (isAssigned) {
                int totalHours = activity.getRegisteredTime(loggedIn);

                Label infoLabel = new Label("Projekt: " + project +
                        " | Aktivitet: " + activity.getName() +
                        " | Timer brugt: " + totalHours);

                infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
                activityList.getChildren().add(infoLabel);
            }
        }
    }

    Button closeButton = new Button("Luk");
    closeButton.setOnAction(e -> {
        mainContainer.getChildren().remove(activityList);
        mainContainer.getChildren().remove(closeButton);
        activityUIVisible = false;
    });

    int insertIndex = findButtonIndex("Se mine Aktiviteter");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, activityList);
        mainContainer.getChildren().add(insertIndex + 2, closeButton);
    }
}

}
