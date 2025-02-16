package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label welcomeText;

    @FXML
    private Label messageLabel;

    @FXML
    public void onHelloButtonClick() {
        // This sample simply updates the labels when the button is clicked.
        welcomeText.setText("Hello, Inventory Management!");
        messageLabel.setText("Button clicked!");

        // In a real application you could, for example, trigger a REST call:
        // String response = callRestEndpoint();
        // messageLabel.setText(response);
    }
}
