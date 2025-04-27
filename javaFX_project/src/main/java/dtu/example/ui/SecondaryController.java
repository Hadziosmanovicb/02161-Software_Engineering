package dtu.example.ui;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import dtu.example.ui.domain.Activity;
import dtu.example.ui.domain.Employee;
import dtu.example.ui.domain.ProjectManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SecondaryController {

    @FXML private VBox mainContainer;

    private boolean projectUIVisible = false;

    private final ProjectManager projectManager = PrimaryController.getProjectManager();
    @FXML
    private Label loggedInLabel;
    @FXML
    private void initialize() {
        loggedInLabel.setText("Logget ind som: " + projectManager.getLoggedInUser());
    }
        
  @FXML
private void handleCreateProject() {
    if (projectUIVisible || activityUIVisible) return;

    projectUIVisible = true;
    mainContainer.getChildren().clear(); 

    Label title = new Label("Opret nyt projekt");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    TextField projectNameField = new TextField();
    projectNameField.setPromptText("Indtast projektnavn");
    projectNameField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    TextField projectLeaderField = new TextField();
    projectLeaderField.setPromptText("Indtast projektleder initialer");
    projectLeaderField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    Button confirmButton = new Button("Bekræft");
    confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    Button cancelButton = new Button("Annuller");
    cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    HBox buttonBox = new HBox(20, confirmButton, cancelButton);
    buttonBox.setPadding(new Insets(10));
    buttonBox.setAlignment(Pos.CENTER);

    confirmButton.setOnAction(e -> {
        String name = projectNameField.getText().trim();
        String leader = projectLeaderField.getText().trim();
        if (!name.isEmpty() && !leader.isEmpty()) {
            projectManager.createProject(name, leader);
            System.out.println("Projekt oprettet: " + name + " — Leder: " + leader);
            mainContainer.getChildren().clear();
            Label success = new Label("Projektet blev oprettet!");
            success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
            mainContainer.getChildren().add(success);
            projectUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        projectUIVisible = false;
    });

    mainContainer.setSpacing(20);
    mainContainer.setPadding(new Insets(40, 20, 20, 20));
    mainContainer.setAlignment(Pos.TOP_LEFT);
    mainContainer.getChildren().addAll(title, projectNameField, projectLeaderField, buttonBox);
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
        if (activityUIVisible || projectUIVisible) return;
    
        activityUIVisible = true;
        mainContainer.getChildren().clear(); // Ryd alt!
    
        Label title = new Label("Tilføj ny aktivitet");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");
    
        ComboBox<String> projectDropdown = new ComboBox<>();
        projectDropdown.setPromptText("Vælg projekt");
        projectDropdown.getItems().addAll(projectManager.getAllProjects());
        projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField activityNameField = new TextField();
        activityNameField.setPromptText("Indtast aktivitetsnavn");
        activityNameField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        Button confirmButton = new Button("Bekræft");
        confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");
    
        Button cancelButton = new Button("Annuller");
        cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");
    
        HBox buttonBox = new HBox(20, confirmButton, cancelButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);
    
        confirmButton.setOnAction(e -> {
            String selectedProject = projectDropdown.getValue();
            String activityName = activityNameField.getText().trim();
            if (selectedProject != null && !activityName.isEmpty()) {
                if (!projectManager.isLoggedInUserProjectLeader(selectedProject)) {
                    showNotProjectLeaderMessage();
                    return;
                }
                projectManager.addActivityToProject(selectedProject, activityName);
                System.out.println("Aktivitet '" + activityName + "' oprettet til projekt: " + selectedProject);
                mainContainer.getChildren().clear();
                Label success = new Label("Aktivitet blev oprettet!");
                success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
                mainContainer.getChildren().add(success);
                activityUIVisible = false;
            }
        });
    
        cancelButton.setOnAction(e -> {
            mainContainer.getChildren().clear();
            activityUIVisible = false;
        });
    
        mainContainer.setSpacing(20);
        mainContainer.setPadding(new Insets(40, 20, 20, 20));
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.getChildren().addAll(title, projectDropdown, activityNameField, buttonBox);
    }
    @FXML
private void handleAddEmployee() {
    if (projectUIVisible || activityUIVisible) return;

    projectUIVisible = true;
    mainContainer.getChildren().clear(); 

    Label title = new Label("Tilføj medarbejder");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getAllProjects());
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    TextField initialsField = new TextField();
    initialsField.setPromptText("Indtast medarbejders initialer");
    initialsField.setDisable(true);
    initialsField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    Button confirmButton = new Button("Bekræft");
    confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    Button cancelButton = new Button("Annuller");
    cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    HBox buttonBox = new HBox(20, confirmButton, cancelButton);
    buttonBox.setPadding(new Insets(10));
    buttonBox.setAlignment(Pos.CENTER);

    projectDropdown.setOnAction(e -> {
        if (projectDropdown.getValue() != null) {
            if (!projectManager.isLoggedInUserProjectLeader(projectDropdown.getValue())) {
                showNotProjectLeaderMessage();
                return;
            }
            initialsField.setDisable(false);
        }
    });
    

    confirmButton.setOnAction(e -> {
        String project = projectDropdown.getValue();
        String initials = initialsField.getText().trim();

        if (project != null && !initials.isEmpty()) {
            projectManager.addEmployee(new Employee(initials));
            System.out.println("Medarbejder tilføjet: " + initials + " til projekt: " + project);
            mainContainer.getChildren().clear();
            Label success = new Label("Medarbejder blev tilføjet!");
            success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
            mainContainer.getChildren().add(success);
            projectUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        projectUIVisible = false;
    });

    mainContainer.setSpacing(20);
    mainContainer.setPadding(new Insets(40, 20, 20, 20));
    mainContainer.setAlignment(Pos.TOP_LEFT);
    mainContainer.getChildren().addAll(title, projectDropdown, initialsField, buttonBox);
}

@FXML
private void handleAssignEmployee() {
    if (projectUIVisible || activityUIVisible) return;
    projectUIVisible = true;
    mainContainer.getChildren().clear(); 

    Label title = new Label("Tildel medarbejder til aktivitet");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getAllProjects());
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    ComboBox<String> activityDropdown = new ComboBox<>();
    activityDropdown.setPromptText("Vælg aktivitet");
    activityDropdown.setDisable(true);
    activityDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    ComboBox<String> employeeDropdown = new ComboBox<>();
    employeeDropdown.setPromptText("Vælg medarbejder");
    employeeDropdown.setDisable(true);
    employeeDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    projectManager.getAllEmployees().forEach(emp ->
        employeeDropdown.getItems().add(emp.getInitials())
    );

    Button confirmButton = new Button("Bekræft");
    confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    Button cancelButton = new Button("Annuller");
    cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    HBox buttonBox = new HBox(20, confirmButton, cancelButton);
    buttonBox.setPadding(new Insets(10));
    buttonBox.setAlignment(Pos.CENTER);

    projectDropdown.setOnAction(e -> {
        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            if (!projectManager.isLoggedInUserProjectLeader(selectedProject)) {
                showNotProjectLeaderMessage();
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

            mainContainer.getChildren().clear();
            Label success = new Label("Medarbejder blev tildelt!");
            success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
            mainContainer.getChildren().add(success);
            projectUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        projectUIVisible = false;
    });

    mainContainer.setSpacing(20);
    mainContainer.setPadding(new Insets(40, 20, 20, 20));
    mainContainer.setAlignment(Pos.TOP_LEFT);
    mainContainer.getChildren().addAll(title, projectDropdown, activityDropdown, employeeDropdown, buttonBox);
}
@FXML
private void handleLogTime() {
    if (projectUIVisible || activityUIVisible) return;
    activityUIVisible = true;
    mainContainer.getChildren().clear(); // Ryd GUI'en først

    Label title = new Label("Registrer Tid på Aktivitet");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    String loggedIn = projectManager.getLoggedInUser();

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

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
    activityDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

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
    datePicker.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    TextField hoursField = new TextField();
    hoursField.setPromptText("Antal timer");
    hoursField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    Button confirmButton = new Button("Bekræft");
    confirmButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    Button cancelButton = new Button("Annuller");
    cancelButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");

    HBox buttonBox = new HBox(20, confirmButton, cancelButton);
    buttonBox.setPadding(new Insets(10));
    buttonBox.setAlignment(Pos.CENTER);

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
            mainContainer.getChildren().clear();
            Label success = new Label("Tid registreret med succes!");
            success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
            mainContainer.getChildren().add(success);
            activityUIVisible = false;
        }
    });

    cancelButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        activityUIVisible = false;
    });

    mainContainer.setSpacing(20);
    mainContainer.setPadding(new Insets(40, 20, 20, 20));
    mainContainer.setAlignment(Pos.TOP_LEFT);
    mainContainer.getChildren().addAll(title, projectDropdown, activityDropdown, datePicker, hoursField, buttonBox);
}

@FXML
private void handleShowMyActivities() {
    if (projectUIVisible || activityUIVisible) return;
    activityUIVisible = true;
    mainContainer.getChildren().clear(); // Ryd alt

    Label title = new Label("Mine Aktiviteter");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    String loggedIn = projectManager.getLoggedInUser();

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    projectDropdown.getItems().addAll(projectManager.getAllProjects());

    VBox activityList = new VBox(15);
    activityList.setPadding(new Insets(20));
    activityList.setStyle("-fx-background-color: #f1f1f1; -fx-border-color: #bbb; -fx-border-radius: 10px;");
    activityList.setPrefWidth(600);

    projectDropdown.setOnAction(e -> {
        activityList.getChildren().clear(); // Ryd når man vælger nyt projekt

        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            for (Activity activity : projectManager.getActivities(selectedProject)) {
                boolean isAssigned = activity.getAssignedEmployees().stream()
                        .anyMatch(emp -> emp.getInitials().equals(loggedIn));
                int totalHours = activity.getRegisteredTime(loggedIn);

                if (isAssigned || totalHours > 0) {
                    Label infoLabel = new Label(
                            "Aktivitet: " + activity.getName() + "\n" +
                            "Timer brugt: " + totalHours
                    );
                    infoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333; -fx-padding: 10; -fx-background-color: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    activityList.getChildren().add(infoLabel);
                }
            }
        }
    });

    Button closeButton = new Button("Tilbage");
    closeButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px; -fx-pref-width: 150px;");
    closeButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        activityUIVisible = false;
    });

    VBox contentBox = new VBox(20, title, projectDropdown, activityList, closeButton);
    contentBox.setPadding(new Insets(40, 20, 20, 20));
    contentBox.setAlignment(Pos.TOP_LEFT);

    mainContainer.getChildren().add(contentBox);
}

private void showNotProjectLeaderMessage() {
    mainContainer.getChildren().clear();
    Label errorLabel = new Label("❌ Du er ikke projektleder på dette projekt!");
    errorLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-font-weight: bold;");
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.getChildren().add(errorLabel);
    projectUIVisible = false;
    activityUIVisible = false;
}

}
