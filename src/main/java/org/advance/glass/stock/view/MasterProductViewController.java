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
import java.util.List;

public class MasterProductViewController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> productGroupColumn;
    @FXML
    private TableColumn<Product, String> productUnitColumn;

    @FXML
    private TextField productNameField;
    @FXML
    private TextField productGroupField;
    @FXML
    private TextField productUnitField;

    private final String API_URL = "http://localhost:8080/api/products";  // ✅ API Endpoint

    @FXML
    public void initialize() {
        setupTable();
        fetchProductsFromApi();
    }

    private void setupTable() {
        // Define table columns
        productNameColumn = new TableColumn<>("Product Name");
        productGroupColumn = new TableColumn<>("Product Group");
        productUnitColumn = new TableColumn<>("Product Unit");

        // Set column mappings
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productGroupColumn.setCellValueFactory(new PropertyValueFactory<>("productGroup"));
        productUnitColumn.setCellValueFactory(new PropertyValueFactory<>("productUnit"));

        // Add columns to table
        productTable.getColumns().addAll(productNameColumn, productGroupColumn, productUnitColumn);
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
            ResponseEntity<Product[]> response = restTemplate.getForEntity(API_URL, Product[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Product> productList = Arrays.asList(response.getBody());
                ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
                productTable.setItems(productObservableList);
            } else {
                System.out.println("Failed to fetch products. Status: " + response.getStatusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error fetching products: " + ex.getMessage());
        }
    }
}
