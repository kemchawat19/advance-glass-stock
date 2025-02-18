package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

public class StockController {

    @FXML
    private TableView<?> stockTable; // Replace '?' with your Stock model class
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Load stock data and initialize table columns
        statusLabel.setText("Stock view loaded.");
    }

    // You can add event handler methods to refresh or update stock data.
    @FXML
    public void handleRefreshStock() {
        // Refresh logic here
        statusLabel.setText("Stock data refreshed.");
    }
}
