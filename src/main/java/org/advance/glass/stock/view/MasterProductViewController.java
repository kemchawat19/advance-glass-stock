package org.advance.glass.stock.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.advance.glass.stock.model.db.Product;
import org.advance.glass.stock.service.ProductService;

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

    private ProductService productService; // Remove final (will be set manually)

    // ✅ Fix: No-Arg Constructor for JavaFX
    public MasterProductViewController() {
    }

    // ✅ Manually Inject Service After Initialization
    public void setProductService(ProductService productService) {
        this.productService = productService;
        loadProductData(); // Load data after setting service
    }

    @FXML
    public void initialize() {
        setupTable();
        loadProductData();
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

    private void loadProductData() {
        System.out.println("productService = " + productService);
        if (productService != null) {  // Prevent NullPointerException
            List<Product> productList = productService.getAllProducts();
            System.out.println("productList = " + productList);
            ObservableList<Product> productObservableList = FXCollections.observableArrayList(productList);
            productTable.setItems(productObservableList);
        }
    }

    @FXML
    public void handleUpdateProduct() {
        String productName = productNameField.getText();
        String productGroup = productGroupField.getText();
        String productUnit = productUnitField.getText();

        System.out.println("Updating Product: " + productName + ", " + productGroup + ", " + productUnit);

        // TODO: Call ProductService to update database
    }
}
