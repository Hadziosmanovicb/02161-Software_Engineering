package dtu.example.ui;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private void handleCreateProject() {
        System.out.println("→ Opret Projekt valgt");
    }

    @FXML
    private void handleAddActivity() {
        System.out.println("→ Tilføj Aktivitet valgt");
    }

    @FXML
    private void handleAddEmployee() {
        System.out.println("→ Tilføj Medarbejder valgt");
    }

    @FXML
    private void handleAssignEmployee() {
        System.out.println("→ Tildel Medarbejder valgt");
    }

    @FXML
    private void handleLogTime() {
        System.out.println("→ Registrer Tid valgt");
    }

    @FXML
    private void handleShowMyActivities() {
        System.out.println("→ Se mine Aktiviteter valgt");
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("primary");
    }
}
