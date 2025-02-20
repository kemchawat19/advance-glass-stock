package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MasterSupplierViewController {

    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField supplierContactField;
    @FXML
    private TextField supplierAddressField;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Supplier Master File view loaded.");
    }

    @FXML
    public void handleUpdateSupplier() {
        // Example: Collect data and update supplier record
        String supplierName = supplierNameField.getText();
        String supplierContact = supplierContactField.getText();
        String supplierAddress = supplierAddressField.getText();

        // TODO: Call a service or REST endpoint to update the supplier record
        System.out.println("Updating Supplier: " + supplierName);
        statusLabel.setText("Supplier updated (simulation).");
    }
}
