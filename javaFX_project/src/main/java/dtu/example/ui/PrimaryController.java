package dtu.example.ui;

import java.io.IOException;
import dtu.example.ui.domain.Employee;
import dtu.example.ui.domain.ProjectManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML private TextField initialsField;
    @FXML private Label loginStatus;

    private static ProjectManager projectManager = new ProjectManager();

    @FXML
    private void handleLogin() throws IOException {
        String initials = initialsField.getText().trim();

        if (initials.isEmpty()) {
            loginStatus.setText("Du skal indtaste initialer.");
            return;
        }

        // Opret medarbejder hvis ny
        if (!projectManager.employeeExists(initials)) {
            projectManager.addEmployee(new Employee(initials));
        }

        // Gem den loggede ind bruger
        projectManager.setLoggedInUser(initials);

        // Gå videre til secondary view
        App.setRoot("secondary");
    }

    public static ProjectManager getProjectManager() {
        return projectManager;
    }
}
