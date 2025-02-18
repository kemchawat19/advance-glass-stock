package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class RequestController {

    @FXML
    private TextField requestNoField;
    @FXML
    private TextField requesterField;
    @FXML
    private DatePicker requestDatePicker;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Request Entry view loaded.");
    }

    @FXML
    public void handleAddRequestDetail() {
        // Open a dialog or add a detail row
        System.out.println("Add Request Detail clicked");
    }

    @FXML
    public void handleCreateRequest() {
        // Example: collect data from UI and simulate request creation
        String requestNo = requestNoField.getText();
        String requester = requesterField.getText();
        LocalDate requestDate = requestDatePicker.getValue();
        LocalDateTime requestDateTime = (requestDate != null) ? LocalDateTime.of(requestDate, LocalTime.now()) : null;

        // TODO: Build a DTO and call a service or REST endpoint
        System.out.println("Creating Request: " + requestNo + ", Requester: " + requester + ", Date: " + requestDateTime);
        statusLabel.setText("Request created (simulation).");
    }
}
