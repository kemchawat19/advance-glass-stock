package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MasterProductController {

    @FXML
    private TextField productCodeField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productGroupField;
    @FXML
    private TextField productUnitField;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Product Master File view loaded.");
    }

    @FXML
    public void handleUpdateProduct() {
        // Example: Collect data and update product master record
        String productCode = productCodeField.getText();
        String productName = productNameField.getText();
        String productGroup = productGroupField.getText();
        String productUnit = productUnitField.getText();

        // TODO: Call a service or REST endpoint to update the product record
        System.out.println("Updating Product: " + productCode + ", " + productName);
        statusLabel.setText("Product updated (simulation).");
    }
}
