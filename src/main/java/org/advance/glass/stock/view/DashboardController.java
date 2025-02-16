package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label totalProductsLabel;

    @FXML
    private Label totalStockLabel;

    @FXML
    private Label statusLabel;

    // Event handler methods for navigation buttons
    @FXML
    public void handleDashboard() {
        statusLabel.setText("Dashboard selected.");
        // Load dashboard data here...
    }

    @FXML
    public void handleProducts() {
        statusLabel.setText("Products view selected.");
        // Load product data...
    }

    @FXML
    public void handleReceipts() {
        statusLabel.setText("Receipts view selected.");
        // Load receipt entries...
    }

    @FXML
    public void handleRequests() {
        statusLabel.setText("Requests view selected.");
        // Load request entries...
    }

    @FXML
    public void handleReports() {
        statusLabel.setText("Reports view selected.");
        // Load reports...
    }

    // Initialization method can load dashboard data if needed.
    public void initialize() {
        // For demonstration, set some default values.
        totalProductsLabel.setText("150");
        totalStockLabel.setText("2000");
        statusLabel.setText("Ready");
    }
}
