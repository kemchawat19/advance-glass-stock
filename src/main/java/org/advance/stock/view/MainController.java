package org.advance.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML
    private Label messageLabel;

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        messageLabel.setText("Welcome to JavaFX with Spring Boot 3.2.2!");
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
