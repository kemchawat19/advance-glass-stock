package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class JobViewController {

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Initialize job management view
        statusLabel.setText("Job Management view loaded.");
    }

    // Add job-related event handlers here.
}
