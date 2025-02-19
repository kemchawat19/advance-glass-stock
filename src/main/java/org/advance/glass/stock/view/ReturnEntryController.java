package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class ReturnEntryController {

    // FXML-bound controls from return_scene.fxml
    @FXML
    private TextField returnNoField;      // Similar to receiptNoField, used for the return entry number.
    @FXML
    private TextField reasonField;        // Field for entering the reason for return.
    @FXML
    private DatePicker returnDatePicker;  // Date when the return occurs.
    @FXML
    private TableView<?> returnDetailTable;  // Optional: to display return detail lines.
    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Status: Waiting");
    }

    // Called when the "Add Detail" button is pressed.
    @FXML
    public void handleAddReturnDetail() {
        // Add logic to open a dialog or add a row to the returnDetailTable.
        System.out.println("Add Return Detail clicked");
    }

    // Called when the "Create Return" button is pressed.
    @FXML
    public void handleCreateReturn() {
        // 1. Collect data from UI fields.
        String returnNo = returnNoField.getText();
        String reason = reasonField.getText();
        LocalDate returnDate = returnDatePicker.getValue();
        LocalDateTime returnDateTime = (returnDate != null) ? LocalDateTime.of(returnDate, LocalTime.now()) : null;

        // 2. Build a unified DTO for the return entry.
        // For a return entry, we set type to "RETURN".
        EntryReqDto dto = EntryReqDto.builder()
                .entryNumber(returnNo)
                .type("RETURN")  // Ensures the service knows this is a return entry.
                .entryDate(returnDateTime)
                .status("COMPLETED")  // Or use "PENDING" based on your workflow.
                .referenceNumber(reason)  // You could use the reason as a reference or add a separate field if desired.
                // Add detail lines. For demonstration, we use a singleton list.
                .entryDetailDtoList(java.util.Collections.singletonList(
                        EntryDetailDto.builder()
                                .stockId(1L)         // Replace with actual stock ID
                                .quantity(1)         // Quantity returned; calculated as (requested - used)
                                .unit("pcs")
                                .unitCost(new java.math.BigDecimal("2.50"))
                                .totalCost(new java.math.BigDecimal("2.50"))
                                .description("Returned item detail")
                                .build()
                ))
                // For return entries, supplier fields might not apply, unless needed.
                .build();

        // 3. Prepare and make the REST call using RestTemplate.
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EntryReqDto> requestEntity = new HttpEntity<>(dto, headers);

        try {
            // Adjust the endpoint URL as necessary.
            ResponseEntity<Entry> response = restTemplate.postForEntity("http://localhost:8080/return-entry", requestEntity, Entry.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Entry createdEntry = response.getBody();
                statusLabel.setText("Return created with ID: " + createdEntry.getId());
                System.out.println("Created Return Entry: " + createdEntry);
            } else {
                statusLabel.setText("Failed to create return entry. Status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error creating return entry: " + ex.getMessage());
        }
    }
}
