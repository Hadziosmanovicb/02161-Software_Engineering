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
    
        try {
            // Forsøg at oprette ny medarbejder (og valider længden)
            if (!projectManager.employeeExists(initials)) {
                projectManager.addEmployee(new Employee(initials));
            }
    
            projectManager.setLoggedInUser(initials);
            App.setRoot("secondary");
    
        } catch (IllegalArgumentException ex) {
            loginStatus.setText("❌ " + ex.getMessage());
        }
    }
    

    public static ProjectManager getProjectManager() {
        return projectManager;
    }
    @FXML
private void initialize() {
    initialsField.setOnAction(e -> {
        try {
            handleLogin();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    });
}

}
