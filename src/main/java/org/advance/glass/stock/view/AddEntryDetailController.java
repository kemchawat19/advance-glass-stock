package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.advance.glass.stock.model.request.EntryDetailDto;

import java.math.BigDecimal;
import java.util.function.Consumer;

public class AddEntryDetailController {

    @FXML
    private TextField productIdField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField unitField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField amountField;
    @FXML
    private TextField descriptionField;
    @FXML
    private Button saveButton;

    @Setter
    private Consumer<EntryDetailDto> onEntryDetailAdded; // Callback to pass data

    @FXML
    public void initialize() {
        // Auto-calculate amount when quantity or unit price changes
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> calculateAmount());
        unitPriceField.textProperty().addListener((obs, oldVal, newVal) -> calculateAmount());
    }

    @FXML
    public void handleSaveEntryDetail() {
        if (productIdField.getText().isEmpty() ||
                quantityField.getText().isEmpty() ||
                unitField.getText().isEmpty() ||
                unitPriceField.getText().isEmpty()) {
            System.out.println("⚠️ Please fill all required fields!");
            return;
        }

        EntryDetailDto entryDetail = EntryDetailDto.builder()
                .productId(Long.parseLong(productIdField.getText()))
                .quantity(Integer.parseInt(quantityField.getText()))
                .unit(unitField.getText())
                .unitPrice(new BigDecimal(unitPriceField.getText()))
                .amount(new BigDecimal(amountField.getText())) // Already auto-calculated
                .description(descriptionField.getText())
                .build();

        if (onEntryDetailAdded != null) {
            onEntryDetailAdded.accept(entryDetail); // Send data back to ReceiptViewController
        }

        closeWindow();
    }

    private void calculateAmount() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            BigDecimal unitPrice = new BigDecimal(unitPriceField.getText());
            amountField.setText(unitPrice.multiply(BigDecimal.valueOf(quantity)).toString());
        } catch (NumberFormatException e) {
            amountField.setText("0"); // Default if invalid input
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
