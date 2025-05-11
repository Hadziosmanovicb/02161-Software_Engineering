/**
 * Filnavn: PrimaryController.java
 * Relaterede filer: ProjectManager.java, Employee.java, Activity.java, ProjectReportGenerator.java, primary.fxml, secondary.fxml
 *
 * Formål:
 * Håndterer login-funktionalitet og oprettelse af nye brugere via GUI.
 * Validerer input, initierer brugerens session i systemet og navigerer videre til
 * det sekundære interface ved succesfuld login.
 */

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
// Ansvarlig: Benjamin
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
    
// Ansvarlig: Younes
    public static ProjectManager getProjectManager() {
        return projectManager;
    }
    // Ansvarlig: Ali
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
