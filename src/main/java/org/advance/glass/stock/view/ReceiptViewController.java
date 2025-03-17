package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.advance.glass.stock.model.request.EntryDetailDto;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReceiptViewController {

    @FXML private TextField entryNumberField;
    @FXML private DatePicker entryDatePicker;
    @FXML private TextField supplierNameField;
    @FXML private TextField supplierInvoiceField;
    @FXML private TextField employeeNameField;
    @FXML private TableView<EntryDetailDto> receiptTable;
    @FXML private Button submitButton;

    private final List<EntryDetailDto> entryDetailList = new ArrayList<>();

    @FXML
    public void initialize() {
        entryDatePicker.setValue(LocalDateTime.now().toLocalDate()); // Set default date to today

        submitButton.setOnAction(event -> submitReceiptEntry());
    }

    private void submitReceiptEntry() {
        // ✅ Create Entry Request Object
        EntryReqDto entryReqDto = EntryReqDto.builder()
                .entryNumber(entryNumberField.getText())
                .entryDate(entryDatePicker.getValue().atStartOfDay())
                .supplierName(supplierNameField.getText())
                .supplierInvoice(supplierInvoiceField.getText())
                .employeeName(employeeNameField.getText())
                .entryDetailDtoList(entryDetailList) // Include table data
                .build();

        // ✅ Call API
        String apiUrl = "http://localhost:8080/api/receipt-entry";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<EntryReqDto> requestEntity = new HttpEntity<>(entryReqDto, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                showAlert("Success", "Receipt entry created successfully!");
            } else {
                showAlert("Error", "Failed to create receipt entry.");
            }
        } catch (Exception e) {
            showAlert("Error", "Error submitting receipt: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
