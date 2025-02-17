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

    @FXML
    private TextField receiptNoField;
    @FXML
    private TextField supplierField;
    @FXML
    private DatePicker importDatePicker;
    @FXML
    private TableView<?> receiptDetailTable;  // Optionally manage multiple details

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Optionally set default values or initialize table columns.
    }

    @FXML
    public void handleAddReceiptDetail() {
        // Code to add detail rows to receiptDetailTable or open a detail dialog.
        System.out.println("Add Receipt Detail clicked");
    }

    @FXML
    public void handleCreateReceipt() {
        // Collect data from the form.
        String receiptNo = receiptNoField.getText();
        String supplier = supplierField.getText();
        LocalDate importDate = importDatePicker.getValue();
        LocalDateTime importDateTime = (importDate != null) ? LocalDateTime.of(importDate, LocalTime.now()) : null;

        // Build the DTO.
        // For this example, we use a single detail entry. You can expand it to a list if needed.
        CreateReceiptEntryReqDto dto = CreateReceiptEntryReqDto.builder()
                .receiptNo(receiptNo)
                .supplier(supplier)
                .importDate(importDateTime)
                .status("COMPLETED")
                .details(java.util.Collections.singletonList(
                        CreateReceiptEntryReqDto.Detail.builder()
                                .stockId(1L)  // Replace with actual stock ID value
                                .quantity(100) // Replace with actual quantity
                                .unitCost(new java.math.BigDecimal("5.00"))
                                .totalCost(new java.math.BigDecimal("500.00"))
                                .build()
                ))
                .build();

        // Prepare the REST call.
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateReceiptEntryReqDto> requestEntity = new HttpEntity<>(dto, headers);

        try {
            // POST the DTO to your backend endpoint.
            ResponseEntity<ReceiptEntry> response = restTemplate.postForEntity("http://localhost:8080/receipt-entry", requestEntity, ReceiptEntry.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                ReceiptEntry createdEntry = response.getBody();
                statusLabel.setText("Receipt created with ID: " + createdEntry.getId());
                System.out.println("Created Receipt: " + createdEntry);
            } else {
                statusLabel.setText("Failed to create receipt, status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error creating receipt: " + ex.getMessage());
        }
    }
}
