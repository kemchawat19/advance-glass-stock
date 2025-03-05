package org.advance.glass.stock.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.advance.glass.stock.model.db.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MasterProductViewController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productGroupField;
    @FXML
    private TextField productUnitField;

    @FXML
    public void initialize() {
        setupTable();
        fetchProductsFromApi();
    }

    private void setupTable() {
        // Define table columns
        TableColumn<Product, Long> productIdColumn = new TableColumn<>("รหัสสินค้า");
        TableColumn<Product, String> productNameColumn = new TableColumn<>("ชื่อสินค้า");
        TableColumn<Product, String> productGroupColumn = new TableColumn<>("หมวดสินค้า");
        TableColumn<Product, String> productUnitColumn = new TableColumn<>("หน่วยสินค้า");
        TableColumn<Product, String> productStatusColumn = new TableColumn<>("สถานะสินค้า");
        TableColumn<Product, String> createTimeStampColumn = new TableColumn<>("เวลาที่สร้างสินค้า");

        // Set column mappings
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productGroupColumn.setCellValueFactory(new PropertyValueFactory<>("productGroup"));
        productUnitColumn.setCellValueFactory(new PropertyValueFactory<>("productUnit"));
        productStatusColumn.setCellValueFactory(new PropertyValueFactory<>("productStatus"));
        createTimeStampColumn.setCellValueFactory(new PropertyValueFactory<>("createTimeStamp"));

//        productIdColumn.setMinWidth(80);
//        productNameColumn.setMinWidth(150);
//        productGroupColumn.setMinWidth(120);
//        productUnitColumn.setMinWidth(120);
//        productStatusColumn.setMinWidth(100);

        // ✅ Ensure Table Auto-resizes the Columns
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Add columns to table
        Collections.addAll(productTable.getColumns(), productIdColumn, productNameColumn, productGroupColumn, productUnitColumn, productStatusColumn, createTimeStampColumn);
    }

    @FXML
    public void handleUpdateProduct() {
        String productName = productNameField.getText();
        String productGroup = productGroupField.getText();
        String productUnit = productUnitField.getText();

        System.out.println("Updating Product: " + productName + ", " + productGroup + ", " + productUnit);

        // TODO: Call ProductService to update database
    }

    private void fetchProductsFromApi() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // API Endpoint
            String API_URL = "http://localhost:8080/api/products";
            ResponseEntity<Product[]> response = restTemplate.getForEntity(API_URL, Product[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Product> productList = Arrays.asList(response.getBody());
                ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
                productTable.setItems(productObservableList);
            } else {
                System.out.println("Failed to fetch products. Status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            System.out.println("Error fetching products: " + ex.getMessage());
        }
    }

    @FXML
    public void handleSelectProduct() {
        // Get selected row data
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            System.out.println("Selected Product ID: " + selectedProduct.getId());
            System.out.println("Product Name: " + selectedProduct.getProductName());
            System.out.println("Product Group: " + selectedProduct.getProductGroup());
            System.out.println("Product Unit: " + selectedProduct.getProductUnit());
            System.out.println("Product Status: " + selectedProduct.getProductStatus());

            // Set values to input fields
            productIdField.setText(String.valueOf(selectedProduct.getId()));
            productNameField.setText(selectedProduct.getProductName());
            productGroupField.setText(selectedProduct.getProductGroup());
            productUnitField.setText(selectedProduct.getProductUnit());
        } else {
            System.out.println("No product selected!");
        }
    }

    @FXML
    public void handleOpenAddProductDialog() {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle("Add New Product");

        // Add UI Fields
        DialogPane dialogPane = dialog.getDialogPane();

        VBox content = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField groupField = new TextField();
        groupField.setPromptText("Product Group");

        TextField unitField = new TextField();
        unitField.setPromptText("Product Unit");

        content.getChildren().addAll(new Label("Enter Product Details:"), nameField, groupField, unitField);

        // 🏷️ Set Dialog Size
        dialogPane.setMinSize(400, 300);

        dialogPane.setContent(content);

        // 🔹 Manually Create Buttons (Centered)
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Cancel");

        HBox buttonBox = new HBox(15, cancelButton, okButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox layout = new VBox(content, buttonBox);
        layout.setAlignment(Pos.CENTER);

        dialogPane.setContent(layout);

        // 🔹 Handle Button Actions
        okButton.setOnAction(e -> {
            dialog.setResult(new Product(
                    null, nameField.getText(), groupField.getText(), unitField.getText(),
                    "ACTIVE", LocalDateTime.now(), LocalDateTime.now()
            ));
            dialog.close(); // ✅ Close dialog after OK
        });

        cancelButton.setOnAction(e -> dialog.close()); // ✅ Close dialog on Cancel

        dialog.showAndWait().ifPresent(this::sendProductToApi);
    }


    /**
     * ✅ Send Single Product to API
     */
    private void sendProductToApi(Product product) {
        try {
            String API_URL = "http://localhost:8080/api/products";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);

            ResponseEntity<Product> response = restTemplate.postForEntity(API_URL, requestEntity, Product.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Product added successfully!");
                fetchProductsFromApi(); // Refresh table
            } else {
                System.out.println("Failed to create product.");
            }
        } catch (Exception ex) {
            System.out.println("Error creating product: " + ex.getMessage());
        }
    }

    @FXML
    public void handleAddProduct() {
        String name = productNameField.getText();
        String group = productGroupField.getText();
        String unit = productUnitField.getText();

        if (name.isEmpty() || group.isEmpty() || unit.isEmpty()) {
            System.out.println("Please fill all fields!");
            return;
        }

        Product newProduct = new Product(null, name, group, unit, "ACTIVE", LocalDateTime.now(), LocalDateTime.now());

        sendProductToApi(newProduct);
        scrollToLastRow();

        // Clear input fields after saving
        productNameField.clear();
        productGroupField.clear();
        productUnitField.clear();
    }

    private void scrollToLastRow() {
        if (!productTable.getItems().isEmpty()) {
            int lastRowIndex = productTable.getItems().size() - 1;
            productTable.scrollTo(lastRowIndex); // ✅ Scroll to the last item
            productTable.getSelectionModel().select(lastRowIndex); // ✅ Select last row
        }
    }
}
