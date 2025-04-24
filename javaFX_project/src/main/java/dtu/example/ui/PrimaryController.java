package dtu.example.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML private TextField initialsField;
    @FXML private Label loginStatus;

    @FXML
    private void handleLogin() throws IOException {
        String initials = initialsField.getText().trim();

        if (initials.isEmpty()) {
            loginStatus.setText("Du skal indtaste initialer.");
            return;
        }

        // Her kan vi senere registrere brugeren hvis ny
        System.out.println("Logger ind som: " + initials);

        // Skift til secondary view
        App.setRoot("secondary");
    }
}
