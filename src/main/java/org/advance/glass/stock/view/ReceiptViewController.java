package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.advance.glass.stock.constant.Type;
import org.advance.glass.stock.model.db.Entry;
import org.advance.glass.stock.model.request.EntryDetailDto;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReceiptViewController {

    // FXML-bound controls from ReceiptView.fxml
    @FXML
    private TextField receiptNoField;       // For receipt number
    @FXML
    private TextField supplierField;        // For supplier name
    @FXML
    private DatePicker importDatePicker;    // For import date
    @FXML
    private TableView<?> receiptDetailTable; // Optional: for detail lines
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Set default status on initialization
        statusLabel.setText("Status: Waiting");
    }

    @FXML
    public void handleAddReceiptDetail() {
        // Logic to add detail lines (e.g., open a dialog or add to a TableView)
        System.out.println("Add Receipt Detail clicked");
    }

    @FXML
    public void handleCreateReceipt() {
        // 1. Collect data from UI fields.
        String receiptNo = receiptNoField.getText();
        String supplier = supplierField.getText();
        LocalDate importDate = importDatePicker.getValue();
        LocalDateTime importDateTime = (importDate != null) ? LocalDateTime.of(importDate, LocalTime.now()) : null;

        // 2. Build the DTO for the receipt entry.
        // Set the type to "RECEIPT". You can also use an enum.
        EntryReqDto dto = EntryReqDto.builder()
                .entryNumber(receiptNo)
                .type(Type.RECEIPT.name())
                .entryDate(importDateTime)
                .processStatus("COMPLETED")
                .supplierName(supplier)
                // Optionally set other fields (supplierId, supplierInvoice, employee info)
                // For details, we use a singleton list for demonstration.
                .entryDetailDtoList(java.util.Collections.singletonList(
                        EntryDetailDto.builder()
                                .quantity(100)       // Replace with the actual quantity
                                .unitCost(new java.math.BigDecimal("5.00"))   // Replace with actual unit cost
                                .totalCost(new java.math.BigDecimal("500.00"))  // Replace with calculated total cost
                                .build()
                ))
                .build();

        // 3. Prepare and send the REST call using RestTemplate.
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EntryReqDto> requestEntity = new HttpEntity<>(dto, headers);

        try {
            // Adjust the endpoint URL as necessary.
            ResponseEntity<Entry> response = restTemplate.postForEntity(
                    "http://localhost:8080/receipt-entry", requestEntity, Entry.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Entry createdEntry = response.getBody();
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
