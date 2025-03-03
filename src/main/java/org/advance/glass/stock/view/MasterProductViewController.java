package org.advance.glass.stock.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.advance.glass.stock.model.db.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
}
