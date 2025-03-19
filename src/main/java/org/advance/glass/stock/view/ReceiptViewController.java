package org.advance.glass.stock.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.advance.glass.stock.model.request.EntryDetailDto;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReceiptViewController {

    @FXML
    private TextField entryNumberField;
    @FXML
    private DatePicker entryDatePicker;
    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField supplierInvoiceField;
    @FXML
    private TextField employeeNameField;
    @FXML
    private TableView<EntryDetailDto> receiptTable;
    @FXML
    private TableColumn<EntryDetailDto, Long> productIdColumn;
    @FXML
    private TableColumn<EntryDetailDto, Integer> quantityColumn;
    @FXML
    private TableColumn<EntryDetailDto, String> unitColumn;
    @FXML
    private TableColumn<EntryDetailDto, BigDecimal> unitPriceColumn;
    @FXML
    private TableColumn<EntryDetailDto, BigDecimal> amountColumn;
    @FXML
    private TableColumn<EntryDetailDto, String> descriptionColumn;
    @FXML
    private TableColumn<EntryDetailDto, Void> actionColumn; // Delete button column
    @FXML
    private Button submitButton;

    private final List<EntryDetailDto> entryDetailList = new ArrayList<>();
    private final ObservableList<EntryDetailDto> entryDetails = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        setupTable();
        entryDatePicker.setValue(LocalDateTime.now().toLocalDate()); // Set default date to today

        submitButton.setOnAction(event -> submitReceiptEntry());

        receiptTable.setItems(entryDetails); // 🔹 Bind the TableView to ObservableList
    }

    private void setupTable() {
        receiptTable.setEditable(true); // Enable editing

        // ✅ Set up columns (Must match EntryDetailDto fields)
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // ✅ Make columns editable
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        unitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        unitPriceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.BigDecimalStringConverter()));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // ✅ Handle editing updates
        setupEditableColumn(quantityColumn, "quantity");
        setupEditableColumn(unitColumn, "unit");
        setupEditableColumn(unitPriceColumn, "unitPrice");
        setupEditableColumn(descriptionColumn, "description");
    }

    private <T> void setupEditableColumn(TableColumn<EntryDetailDto, T> column, String property) {
        column.setOnEditCommit(event -> {
            EntryDetailDto entryDetail = event.getRowValue();
            T newValue = event.getNewValue();

            switch (property) {
                case "quantity":
                    entryDetail.setQuantity((Integer) newValue);
                    break;
                case "unit":
                    entryDetail.setUnit((String) newValue);
                    break;
                case "unitPrice":
                    entryDetail.setUnitPrice((BigDecimal) newValue);
                    entryDetail.setAmount(entryDetail.getUnitPrice().multiply(BigDecimal.valueOf(entryDetail.getQuantity()))); // Recalculate total
                    break;
                case "description":
                    entryDetail.setDescription((String) newValue);
                    break;
            }

            receiptTable.refresh(); // ✅ Update TableView UI
        });
    }

    private void submitReceiptEntry() {
        System.out.println("🔹 entryDetailList Before API Call: " + entryDetailList);

        // ✅ Ensure entryDetailList contains data from the TableView
        entryDetailList.addAll(receiptTable.getItems()); // 🔹 Copy current table data

        if (entryDetailList.isEmpty()) {
            showAlert("Error", "Cannot submit a receipt without products!");
            return;
        }
        // ✅ Create Entry Request Object
        EntryReqDto entryReqDto = EntryReqDto.builder()
                .entryNumber(entryNumberField.getText())
                .entryDate(entryDatePicker.getValue().atStartOfDay())
                .supplierName(supplierNameField.getText())
                .supplierInvoice(supplierInvoiceField.getText())
//                .employeeName(employeeNameField.getText())
                .entryDetailDtoList(new ArrayList<>(entryDetailList)) // 🔹 Include copied table data
                .build();

        System.out.println("entryReqDto = " + entryReqDto);

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
                clearForm(); // ✅ Clear form after submission
            } else {
                showAlert("Error", "Failed to create receipt entry.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error submitting receipt: " + e.getMessage());
        }
    }

    // ✅ Clear Form After Submission
    private void clearForm() {
        entryNumberField.clear();
        entryDatePicker.setValue(LocalDateTime.now().toLocalDate()); // Reset to today
        supplierNameField.clear();
        supplierInvoiceField.clear();
//        employeeNameField.clear();
        entryDetailList.clear();
        receiptTable.getItems().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleOpenAddEntryDetailStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddEntryDetailView.fxml"));
            Parent root = loader.load();

            AddEntryDetailController controller = loader.getController();
            controller.setOnEntryDetailAdded(entryDetail -> {
                System.out.println("✅ Adding entry to table: " + entryDetail);
                entryDetails.add(entryDetail); // ✅ Use ObservableList
                receiptTable.refresh(); // ✅ Ensure UI updates
            });

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(receiptTable.getScene().getWindow());

            stage.showAndWait(); // Wait for user to close
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error opening AddEntryDetailView: " + e.getMessage());
        }
    }
}
