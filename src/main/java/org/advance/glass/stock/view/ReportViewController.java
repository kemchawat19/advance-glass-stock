package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReportViewController {

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Initialize report view
        statusLabel.setText("Report view loaded.");
    }

    // Add report generation or filtering methods here.
}
