package main;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javafx.scene.control.TextField;
import java.io.IOException;

public class ClientInfoController {


    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private PasswordField passwordField;

    @FXML
    private void connectButtonAction() throws IOException {
        Client.clientInfo(firstNameTextField.getText().trim(),
                lastNameTextField.getText().trim(), passwordField.getText().trim());
    }
}
