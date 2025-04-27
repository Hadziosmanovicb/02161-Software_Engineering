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
    
        projectUIVisible = true;
    
        TextField projectNameField = new TextField();
        projectNameField.setPromptText("Projekt navn");
    
        TextField projectLeaderField = new TextField();
        projectLeaderField.setPromptText("Projektleder initialer");
    
        Button confirmButton = new Button("Bekræft");
        Button cancelButton = new Button("Annuller");
    
   
        confirmButton.setOnAction(e -> {
            String name = projectNameField.getText();
            String leader = projectLeaderField.getText();
            if (!name.isEmpty() && !leader.isEmpty()) {
                projectManager.createProject(name, leader);
                System.out.println("Projekt oprettet: " + name + " — Leder: " + leader);
                mainContainer.getChildren().removeAll(projectNameField, projectLeaderField, confirmButton, cancelButton);
                projectUIVisible = false;
            }
        });
    
    
        cancelButton.setOnAction(e -> {
            mainContainer.getChildren().removeAll(projectNameField, projectLeaderField, confirmButton, cancelButton);
            projectUIVisible = false;
        });
    
        int insertIndex = findButtonIndex("Opret Projekt");
        if (insertIndex != -1) {
            mainContainer.getChildren().add(insertIndex + 1, projectNameField);
            mainContainer.getChildren().add(insertIndex + 2, projectLeaderField);
            mainContainer.getChildren().add(insertIndex + 3, confirmButton);
            mainContainer.getChildren().add(insertIndex + 4, cancelButton); 
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
    
        activityUIVisible = true;
    
        ComboBox<String> projectDropdown = new ComboBox<>();
        projectDropdown.setPromptText("Vælg projekt");
        projectDropdown.getItems().addAll(projectManager.getAllProjects());
    
        TextField activityNameField = new TextField();
        activityNameField.setPromptText("Aktivitetsnavn");
    
        Button confirmButton = new Button("Bekræft");
        Button cancelButton = new Button("Annuller");
    
  
        confirmButton.setOnAction(e -> {
            String selectedProject = projectDropdown.getValue();
            String activityName = activityNameField.getText();
            if (selectedProject != null && !activityName.isEmpty()) {
    
                if (!projectManager.isLoggedInUserProjectLeader(selectedProject)) {
                    System.out.println("Kun projektlederen må tilføje aktiviteter til dette projekt.");
                    return;
                }
    
                projectManager.addActivityToProject(selectedProject, activityName);
                System.out.println("Aktivitet '" + activityName + "' oprettet til projekt: " + selectedProject);
                mainContainer.getChildren().removeAll(projectDropdown, activityNameField, confirmButton, cancelButton);
                activityUIVisible = false;
            }
        });
    
       
        cancelButton.setOnAction(e -> {
            mainContainer.getChildren().removeAll(projectDropdown, activityNameField, confirmButton, cancelButton);
            activityUIVisible = false;
        });
    
        int insertIndex = findButtonIndex("Tilføj Aktivitet");
        if (insertIndex != -1) {
            mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
            mainContainer.getChildren().add(insertIndex + 2, activityNameField);
            mainContainer.getChildren().add(insertIndex + 3, confirmButton);
            mainContainer.getChildren().add(insertIndex + 4, cancelButton); 
        }
    }
    
    @FXML
    private void handleAddEmployee() {
        if (projectUIVisible || activityUIVisible) return;
        projectUIVisible = true;
    
        // Vælg projekt
        ComboBox<String> projectDropdown = new ComboBox<>();
        projectDropdown.setPromptText("Vælg projekt");
        projectDropdown.getItems().addAll(projectManager.getAllProjects());
    
        // Indtast initialer
        TextField initialsField = new TextField();
        initialsField.setPromptText("Initialer på medarbejder");
        initialsField.setDisable(true); 
    
        Button confirmButton = new Button("Bekræft");
        Button cancelButton = new Button("Annuller");
    
        // Når projekt er valgt → aktiver initialer input
        projectDropdown.setOnAction(e -> {
            if (projectDropdown.getValue() != null) {
                initialsField.setDisable(false);
            }
        });
    
        // Bekræft-knap
        confirmButton.setOnAction(e -> {
            String project = projectDropdown.getValue();
            String initials = initialsField.getText().trim();
    
            if (project != null && !initials.isEmpty()) {
                projectManager.addEmployee(new Employee(initials));
                System.out.println("Medarbejder tilføjet: " + initials + " til projekt: " + project);
                mainContainer.getChildren().removeAll(projectDropdown, initialsField, confirmButton, cancelButton);
                projectUIVisible = false;
            }
        });
    
        // Annuller-knap
        cancelButton.setOnAction(e -> {
            mainContainer.getChildren().removeAll(projectDropdown, initialsField, confirmButton, cancelButton);
            projectUIVisible = false;
        });
    
        int insertIndex = findButtonIndex("Tilføj Medarbejder");
        if (insertIndex != -1) {
            mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
            mainContainer.getChildren().add(insertIndex + 2, initialsField);
            mainContainer.getChildren().add(insertIndex + 3, confirmButton);
            mainContainer.getChildren().add(insertIndex + 4, cancelButton);
        }
    }
    @FXML
private void handleAssignEmployee() {
    if (projectUIVisible || activityUIVisible) return;
    projectUIVisible = true;

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getAllProjects());

    ComboBox<String> activityDropdown = new ComboBox<>();
    activityDropdown.setPromptText("Vælg aktivitet");
    activityDropdown.setDisable(true);

    ComboBox<String> employeeDropdown = new ComboBox<>();
    employeeDropdown.setPromptText("Vælg medarbejder");
    projectManager.getAllEmployees().forEach(emp ->
        employeeDropdown.getItems().add(emp.getInitials())
    );
    employeeDropdown.setDisable(true);

    Button confirmButton = new Button("Bekræft");
    Button cancelButton = new Button("Annuller");

    projectDropdown.setOnAction(e -> {
        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            if (!projectManager.isLoggedInUserProjectLeader(selectedProject)) {
                System.out.println("Kun projektlederen må tildele medarbejdere til dette projekt.");
                activityDropdown.setDisable(true);
                employeeDropdown.setDisable(true);
                return;
            }

            activityDropdown.getItems().clear();
            projectManager.getActivities(selectedProject).forEach(activity ->
                activityDropdown.getItems().add(activity.getName())
            );
            activityDropdown.setDisable(false);
            employeeDropdown.setDisable(false);
        }
    });

    confirmButton.setOnAction(e -> {
        String project = projectDropdown.getValue();
        String activityName = activityDropdown.getValue();
        String employeeInitials = employeeDropdown.getValue();

        if (project != null && activityName != null && employeeInitials != null) {
            if (!projectManager.isLoggedInUserProjectLeader(project)) {
                System.out.println("Kun projektlederen må tildele medarbejdere.");
                return;
            }

            for (Activity activity : projectManager.getActivities(project)) {
                if (activity.getName().equals(activityName)) {
                    activity.assignEmployee(new Employee(employeeInitials));
                    System.out.println("Medarbejder " + employeeInitials + " tildelt til aktivitet " + activityName + " i projekt " + project);
                    break;
                }
            }

            mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, employeeDropdown, confirmButton, cancelButton);
            projectUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, employeeDropdown, confirmButton, cancelButton);
        projectUIVisible = false;
    });

    int insertIndex = findButtonIndex("Tildel Medarbejder til Aktivitet");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
        mainContainer.getChildren().add(insertIndex + 2, activityDropdown);
        mainContainer.getChildren().add(insertIndex + 3, employeeDropdown);
        mainContainer.getChildren().add(insertIndex + 4, confirmButton);
        mainContainer.getChildren().add(insertIndex + 5, cancelButton);
    }
}

@FXML
private void handleLogTime() {
    if (projectUIVisible || activityUIVisible) return;
    activityUIVisible = true;

    String loggedIn = projectManager.getLoggedInUser();

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

    DatePicker datePicker = new DatePicker();
    datePicker.setPromptText("Vælg dato");

    TextField hoursField = new TextField();
    hoursField.setPromptText("Antal timer");

    Button confirmButton = new Button("Bekræft");
    Button cancelButton = new Button("Annuller");

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
            mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, datePicker, hoursField, confirmButton, cancelButton);
            activityUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().removeAll(projectDropdown, activityDropdown, datePicker, hoursField, confirmButton, cancelButton);
        activityUIVisible = false;
    });

    int insertIndex = findButtonIndex("Registrer Tid");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, projectDropdown);
        mainContainer.getChildren().add(insertIndex + 2, activityDropdown);
        mainContainer.getChildren().add(insertIndex + 3, datePicker);
        mainContainer.getChildren().add(insertIndex + 4, hoursField);
        mainContainer.getChildren().add(insertIndex + 5, confirmButton);
        mainContainer.getChildren().add(insertIndex + 6, cancelButton);
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

    Button closeButton = new Button("Annuller");
    closeButton.setOnAction(e -> {
        mainContainer.getChildren().removeAll(activityList, closeButton);
        activityUIVisible = false;
    });

    int insertIndex = findButtonIndex("Se mine Aktiviteter");
    if (insertIndex != -1) {
        mainContainer.getChildren().add(insertIndex + 1, activityList);
        mainContainer.getChildren().add(insertIndex + 2, closeButton);
    }
}


}
