package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.advance.glass.stock.model.db.ReceiptEntry;
import org.advance.glass.stock.model.request.CreateReceiptEntryReqDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReceiptController {

    // FXML-bound controls from receipt_scene.fxml
    @FXML
    private TextField receiptNoField;
    @FXML
    private TextField supplierField;
    @FXML
    private DatePicker importDatePicker;
    @FXML
    private TableView<?> receiptDetailTable; // Optional: if you're showing details in a table
    @FXML
    private Label statusLabel;

    // This initialize method is automatically called after FXML loading.
    @FXML
    public void initialize() {
        // Set default values if needed
        statusLabel.setText("Status: Waiting");
    }

    // Called when the "Add Detail" button is pressed.
    @FXML
    public void handleAddReceiptDetail() {
        // Here you would add logic to open a dialog or add a row to the receiptDetailTable.
        System.out.println("Add Receipt Detail clicked");
    }

    // Called when the "Create Receipt" button is pressed.
    @FXML
    public void handleCreateReceipt() {
        // 1. Collect data from the UI fields.
        String receiptNo = receiptNoField.getText();
        String supplier = supplierField.getText();
        LocalDate importDate = importDatePicker.getValue();
        LocalDateTime importDateTime = (importDate != null) ? LocalDateTime.of(importDate, LocalTime.now()) : null;

        // 2. Build a DTO for the receipt entry.
        // In this example, we use a single detail entry for demonstration.
        CreateReceiptEntryReqDto dto = CreateReceiptEntryReqDto.builder()
                .receiptNo(receiptNo)
                .supplier(supplier)
                .importDate(importDateTime)
                .status("COMPLETED")
                .details(java.util.Collections.singletonList(
                        CreateReceiptEntryReqDto.Detail.builder()
                                .stockId(1L)         // Replace with actual stock ID from detail dialog or table
                                .quantity(100)       // Replace with the user-entered quantity
                                .unitCost(new java.math.BigDecimal("5.00"))   // Replace with actual unit cost
                                .totalCost(new java.math.BigDecimal("500.00"))  // Replace with calculated total cost
                                .build()
                ))
                .build();

        // 3. Prepare and make the REST call using RestTemplate.
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReceiptEntryReqDto> requestEntity = new HttpEntity<>(dto, headers);

        try {
            // Adjust the endpoint URL as necessary.
            ResponseEntity<ReceiptEntry> response = restTemplate.postForEntity("http://localhost:8080/receipt-entry", requestEntity, ReceiptEntry.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ReceiptEntry createdEntry = response.getBody();
                statusLabel.setText("Receipt created with ID: " + createdEntry.getId());
                System.out.println("Created Receipt: " + createdEntry);
            } else {
                statusLabel.setText("Failed to create receipt. Status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error creating receipt: " + ex.getMessage());
        }
    }
}
