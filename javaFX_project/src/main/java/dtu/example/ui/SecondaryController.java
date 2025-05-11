package dtu.example.ui;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dtu.example.ui.domain.Activity;
import dtu.example.ui.domain.Employee;
import dtu.example.ui.domain.ProjectManager;
import dtu.example.ui.domain.ProjectReportGenerator;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SecondaryController {
    private void showError(String message) {
        Label errorLabel = new Label("❌ " + message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        mainContainer.getChildren().add(errorLabel);
        
    }
    
    @FXML private VBox mainContainer;

    private boolean projectUIVisible = false;

    private final ProjectManager projectManager = PrimaryController.getProjectManager();
    private final ProjectReportGenerator reportGenerator = new ProjectReportGenerator(projectManager);
private void fireFirstButtonWithText(String buttonText) {
    mainContainer.getChildren().stream()
        .flatMap(node -> {
            if (node instanceof HBox hbox) return hbox.getChildren().stream();
            else return java.util.stream.Stream.of(node);
        })
        .filter(node -> node instanceof Button b && b.getText().equals(buttonText))
        .findFirst()
        .ifPresent(node -> ((Button) node).fire());
}
    @FXML
    private Label loggedInLabel;
@FXML
private void initialize() {
    loggedInLabel.setText("Logget ind som: " + projectManager.getLoggedInUser());

    // Only trigger "Bekræft" when Enter is pressed
    mainContainer.setOnKeyPressed(keyEvent -> {
        if (keyEvent.getCode() == javafx.scene.input.KeyCode.ENTER) {
            fireFirstButtonWithText("Bekræft");
        }
    });

    mainContainer.requestFocus(); // Ensures mainContainer receives key events
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
        if (projectManager.projectExists(name)) {
            showError("Et projekt med dette navn findes allerede.");
            return;
        }

        try {
            Employee leaderEmp = new Employee(leader); 
            projectManager.addEmployee(leaderEmp);    
            projectManager.createProject(name, leader);
            System.out.println("Projekt oprettet: " + name + " — Leder: " + leader);

            mainContainer.getChildren().clear();
            Label success = new Label("Projektet blev oprettet!");
            success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
            mainContainer.getChildren().add(success);
            projectUIVisible = false;
        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        }
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

    
    private boolean activityUIVisible = false;
    @FXML
    private void handleAddActivity() {
        if (activityUIVisible || projectUIVisible) return;
    
        activityUIVisible = true;
        mainContainer.getChildren().clear(); 
    
        Label title = new Label("Tilføj ny aktivitet");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");
    
        ComboBox<String> projectDropdown = new ComboBox<>();
        projectDropdown.setPromptText("Vælg projekt");
        projectDropdown.getItems().addAll(projectManager.getProjectsForLoggedInUser());
        projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField activityNameField = new TextField();
        activityNameField.setPromptText("Indtast aktivitetsnavn");
        activityNameField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField startWeekField = new TextField();
        startWeekField.setPromptText("Start uge (fx 12)");
        startWeekField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField startYearField = new TextField();
        startYearField.setPromptText("Start år (fx 2025)");
        startYearField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField endWeekField = new TextField();
        endWeekField.setPromptText("Slut uge (fx 20)");
        endWeekField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField endYearField = new TextField();
        endYearField.setPromptText("Slut år (fx 2025)");
        endYearField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
        TextField budgetHoursField = new TextField();
        budgetHoursField.setPromptText("Budgetterede timer");
        budgetHoursField.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    
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
    
            try {
                int startWeek, startYear, endWeek, endYear, budgetedHours;
try {
    startWeek = Integer.parseInt(startWeekField.getText().trim());
    startYear = Integer.parseInt(startYearField.getText().trim());
    endWeek = Integer.parseInt(endWeekField.getText().trim());
    endYear = Integer.parseInt(endYearField.getText().trim());
    budgetedHours = Integer.parseInt(budgetHoursField.getText().trim());
} catch (NumberFormatException ex) {
    showError("Alle felter skal være udfyldt korrekt som tal.");
    return;
}

// Valider uger
if (startWeek < 1 || startWeek > 52 || endWeek < 1 || endWeek > 52) {
    showError("Uger skal være mellem 1 og 52.");
    return;
}

// Valider år
if (startYear < 2000 || endYear < 2000) {
    showError("År skal være 2000 eller højere.");
    return;
}


if (endYear < startYear || (endYear == startYear && endWeek < startWeek)) {
    showError("Slutdato skal være efter startdato.");
    return;
}
if (budgetedHours <= 1) {
    showError("En aktivitet skal være længere end 1 time.");
    return;
}

    
               if (selectedProject != null && !activityName.isEmpty()) {
    if (!projectManager.isLoggedInUserProjectLeader(selectedProject)) {
        showNotProjectLeaderMessage();
        return;
    }

    if (projectManager.activityExistsInProject(selectedProject, activityName)) {
        showError("En aktivitet med dette navn findes allerede i projektet.");
        return;
    }

    projectManager.addActivityToProject(selectedProject, activityName, startWeek, startYear, endWeek, endYear, budgetedHours);

                    System.out.println("Aktivitet '" + activityName + "' oprettet til projekt: " + selectedProject);
                    mainContainer.getChildren().clear();
                    Label success = new Label("Aktivitet blev oprettet!");
                    success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
                    mainContainer.getChildren().add(success);
                    activityUIVisible = false;
                }
            } catch (NumberFormatException ex) {
                Label errorLabel = new Label("❌ Ugyldigt input! Alle felter skal udfyldes korrekt.");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                mainContainer.getChildren().add(errorLabel);
            }
        });
    
        cancelButton.setOnAction(e -> {
            mainContainer.getChildren().clear();
            activityUIVisible = false;
        });
    
        mainContainer.setSpacing(20);
        mainContainer.setPadding(new Insets(40, 20, 20, 20));
        mainContainer.setAlignment(Pos.TOP_LEFT);
        mainContainer.getChildren().addAll(
            title, projectDropdown, activityNameField,
            startWeekField, startYearField, endWeekField, endYearField, budgetHoursField,
            buttonBox
        );
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
    projectDropdown.getItems().addAll(projectManager.getProjectsForLoggedInUser());
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
            try {
                Employee newEmp = new Employee(initials); 
               projectManager.addEmployee(newEmp);

if (projectManager.isEmployeePartOfProject(project, initials)) {
    showError("Medarbejderen er allerede tildelt projektet.");
    return;
}

projectManager.addEmployeeToProject(project, initials);


    
                System.out.println("Medarbejder tilføjet: " + initials + " til projekt: " + project);
                mainContainer.getChildren().clear();
                Label success = new Label("Medarbejder blev tilføjet!");
                success.setStyle("-fx-font-size: 18px; -fx-text-fill: green;");
                mainContainer.getChildren().add(success);
                projectUIVisible = false;
            } catch (IllegalArgumentException ex) {
                Label errorLabel = new Label("❌ " + ex.getMessage());
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                mainContainer.getChildren().add(errorLabel);
            }
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
    projectDropdown.getItems().addAll(projectManager.getProjectsForLoggedInUser());
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
                    Employee emp = projectManager.getEmployeeByInitials(employeeInitials);
                    if (emp == null) {
                        System.out.println("Medarbejderen findes ikke.");
                        return;
                    }
                    
           if (!projectManager.isEmployeePartOfProject(project, employeeInitials)) {
    Label errorLabel = new Label("❌ Medarbejderen er ikke tilknyttet projektet og kan ikke tildeles aktivitet.");
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    mainContainer.getChildren().add(errorLabel);
    return;
}

if (activity.getAssignedEmployees().contains(emp)) {
    Label errorLabel = new Label("❌ Medarbejderen er allerede tildelt denne aktivitet.");
    errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    mainContainer.getChildren().add(errorLabel);
    return;
}

activity.assignEmployee(emp);

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
    mainContainer.getChildren().clear();

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

    DatePicker datePicker = new DatePicker();
    datePicker.setPromptText("Vælg dato");
    datePicker.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

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

    activityDropdown.setOnAction(e -> {
        String project = projectDropdown.getValue();
        String activityName = activityDropdown.getValue();
        if (project != null && activityName != null) {
            for (Activity act : projectManager.getActivities(project)) {
                if (act.getName().equals(activityName)) {
                    LocalDate startDate = LocalDate.of(act.getStartYear(), 1, 1).plusWeeks(act.getStartWeek() - 1);
                    LocalDate endDate = LocalDate.of(act.getEndYear(), 1, 1).plusWeeks(act.getEndWeek() - 1);
                    datePicker.setDayCellFactory(picker -> new javafx.scene.control.DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                            setDisable(empty || item.isBefore(startDate) || item.isAfter(endDate));
                        }
                    });
                    break;
                }
            }
        }
    });

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
            Label errorLabel = new Label("❌ Ugyldigt antal timer");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            mainContainer.getChildren().add(errorLabel);
            return;
        }

        if (project != null && activityName != null && hours > 0 && date != null) {
            for (Activity act : projectManager.getActivities(project)) {
                if (act.getName().equals(activityName)) {
                    if ("Godkendt".equals(act.getStatus())) {
                        Label errorLabel = new Label("❌ Aktiviteten er godkendt. Kan ikke registrere flere timer.");
                        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                        mainContainer.getChildren().add(errorLabel);
                        return;
                    }
                    act.registerTime(new Employee(loggedIn), date, hours);
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
    mainContainer.getChildren().clear();

    Label title = new Label("Mine Aktiviteter / Projektaktiviteter");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    String loggedIn = projectManager.getLoggedInUser();

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");
    projectDropdown.getItems().addAll(projectManager.getProjectsForLoggedInUser());

    VBox contentBox = new VBox(20);
    contentBox.setPadding(new Insets(40, 20, 20, 20));
    contentBox.setAlignment(Pos.TOP_LEFT);

    VBox projectInfoBox = new VBox(10);
    projectInfoBox.setPadding(new Insets(10));
    projectInfoBox.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #00acc1; -fx-border-radius: 5px;");
    projectInfoBox.setPrefWidth(600);

    VBox employeeList = new VBox(10);
    employeeList.setPadding(new Insets(10));
    employeeList.setStyle("-fx-background-color: #e3f2fd; -fx-border-color: #90caf9; -fx-border-radius: 5px;");
    employeeList.setPrefWidth(600);

    VBox activityList = new VBox(15);
    activityList.setPadding(new Insets(10));
    activityList.setStyle("-fx-background-color: #f1f1f1; -fx-border-color: #bbb; -fx-border-radius: 5px;");
    activityList.setPrefWidth(600);

  
    ScrollPane employeeScrollPane = new ScrollPane(employeeList);
    employeeScrollPane.setFitToWidth(true);
    employeeScrollPane.setPrefHeight(200);

    ScrollPane activityScrollPane = new ScrollPane(activityList);
    activityScrollPane.setFitToWidth(true);
    activityScrollPane.setPrefHeight(400);

    projectDropdown.setOnAction(e -> {
        activityList.getChildren().clear();
        employeeList.getChildren().clear();
        projectInfoBox.getChildren().clear();

        String selectedProject = projectDropdown.getValue();
        if (selectedProject != null) {
            boolean isLeader = projectManager.isLoggedInUserProjectLeader(selectedProject);

            List<Activity> activities = projectManager.getActivities(selectedProject);

            // Projektstatus
            long totalActivities = activities.size();
            long completedActivities = activities.stream().filter(Activity::isCompleted).count();
            int projectProgress = totalActivities == 0 ? 0 : (int) (completedActivities * 100 / totalActivities);

            Label projectStatus = new Label("Projektstatus: " + projectProgress + "% færdig");
            projectStatus.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            projectInfoBox.getChildren().add(projectStatus);

            // Medarbejdere
           Label empTitle = new Label("Medarbejdere i projektet:");
empTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
employeeList.getChildren().add(empTitle);

Map<String, List<String>> assignment = projectManager.getEmployeeActivityAssignment(selectedProject);

if (assignment.isEmpty()) {
    employeeList.getChildren().add(new Label("- Ingen medarbejdere tilføjet"));
} else {
    for (Map.Entry<String, List<String>> entry : assignment.entrySet()) {
        List<String> assignedActivities = entry.getValue();
        String status = assignedActivities.size() == 1 && assignedActivities.get(0).equals("ledig")
                        ? "ledig"
                        : "aktiviteter: " + String.join(", ", assignedActivities);
        employeeList.getChildren().add(new Label("- " + entry.getKey() + " (" + status + ")"));
    }
}

           
            for (Activity activity : activities) {
                boolean isAssigned = activity.getAssignedEmployees().stream()
                        .anyMatch(emp -> emp.getInitials().equals(loggedIn));
                int personalHours = activity.getRegisteredTime(loggedIn);

                if (isLeader || isAssigned || personalHours > 0) {
                    VBox activityBox = new VBox(5);
                    activityBox.setPadding(new Insets(10));
                    activityBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5px;");

                    StringBuilder sb = new StringBuilder();
                    sb.append("Aktivitet: ").append(activity.getName());
                    sb.append("\nStatus: ").append(activity.getStatus());
                    sb.append("\nStart: Uge ").append(activity.getStartWeek()).append(" / ").append(activity.getStartYear());
                    sb.append("\nSlut: Uge ").append(activity.getEndWeek()).append(" / ").append(activity.getEndYear());
                    sb.append("\nBudgetterede timer: ").append(activity.getBudgetedHours());
                    sb.append("\nFærdiggørelse: ").append(activity.getCompletionPercentage()).append("%");

                    Label infoLabel = new Label(sb.toString());
                    infoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

                    activityBox.getChildren().add(infoLabel);

                    if (!"Godkendt".equals(activity.getStatus())) {
                        if (isLeader && "Afventer".equals(activity.getStatus())) {
                            Button acceptBtn = new Button("Godkend");
                            Button rejectBtn = new Button("Afvis");

                            acceptBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                            rejectBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                            acceptBtn.setOnAction(ev -> {
                                activity.setStatus("Godkendt");
                                projectDropdown.getOnAction().handle(null);
                            });
                            rejectBtn.setOnAction(ev -> {
                                activity.setStatus("Afvist");
                                projectDropdown.getOnAction().handle(null);
                            });

                            HBox buttonBox = new HBox(10, acceptBtn, rejectBtn);
                            activityBox.getChildren().add(buttonBox);
                        } else if (isAssigned && ("Ikke klar".equals(activity.getStatus()) || "Afvist".equals(activity.getStatus()))) {
                            Button doneBtn = new Button("Marker som Færdig");
                            doneBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
                            doneBtn.setOnAction(ev -> {
                                activity.setStatus("Afventer");
                                projectDropdown.getOnAction().handle(null);
                            });
                            activityBox.getChildren().add(doneBtn);
                        }
                    }

                    activityList.getChildren().add(activityBox);
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

    contentBox.getChildren().addAll(title, projectDropdown, projectInfoBox, employeeScrollPane, activityScrollPane, closeButton);
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
@FXML
private void handleGenerateProjectReport() {
    if (projectUIVisible || activityUIVisible) return;

    projectUIVisible = true;
    mainContainer.getChildren().clear();

    Label title = new Label("Generér Rapport for Projekt");
    title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

    ComboBox<String> projectDropdown = new ComboBox<>();
    projectDropdown.setPromptText("Vælg projekt");
    projectDropdown.getItems().addAll(projectManager.getProjectsForLoggedInUser());
    projectDropdown.setStyle("-fx-font-size: 16px; -fx-pref-width: 300px;");

    TextArea reportTextArea = new TextArea();
    reportTextArea.setEditable(false);
    reportTextArea.setWrapText(true);
    reportTextArea.setStyle("-fx-font-size: 14px;");
    reportTextArea.setPrefSize(600, 400);

    Button generateButton = new Button("Generér Rapport");
    generateButton.setStyle("-fx-background-color: #1976D2; -fx-text-fill: white; -fx-font-size: 16px;");

    Button backButton = new Button("Tilbage");
    backButton.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 16px;");

    generateButton.setOnAction(e -> {
        String selectedProject = projectDropdown.getValue();
        if (selectedProject == null) {
            reportTextArea.setText("❌ Vælg et projekt først.");
        } else {
            String report = reportGenerator.generateReport(selectedProject);
            reportTextArea.setText(report);
        }
    });

    backButton.setOnAction(e -> {
        mainContainer.getChildren().clear();
        projectUIVisible = false;
    });

    VBox layout = new VBox(20, title, projectDropdown, reportTextArea, new HBox(20, generateButton, backButton));
    layout.setPadding(new Insets(40, 20, 20, 20));
    layout.setAlignment(Pos.TOP_LEFT);

    mainContainer.getChildren().add(layout);
}

}
