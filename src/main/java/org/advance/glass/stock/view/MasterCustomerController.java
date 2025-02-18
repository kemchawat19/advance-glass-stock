package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MasterCustomerController {

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerContactField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Customer Master File view loaded.");
    }

    @FXML
    public void handleUpdateCustomer() {
        // Example: Collect data and update customer record
        String customerName = customerNameField.getText();
        String customerContact = customerContactField.getText();
        String customerAddress = customerAddressField.getText();

        // TODO: Call a service or REST endpoint to update the customer record
        System.out.println("Updating Customer: " + customerName);
        statusLabel.setText("Customer updated (simulation).");
    }
}
