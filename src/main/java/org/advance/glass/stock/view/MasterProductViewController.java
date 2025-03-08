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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.advance.glass.stock.model.db.Product;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

public class MasterProductViewController {

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Long> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> productGroupColumn;
    @FXML
    private TableColumn<Product, String> productUnitColumn;
    @FXML
    private TableColumn<Product, String> productStatusColumn;
    @FXML
    private TableColumn<Product, String> createTimeStampColumn;

    private final Map<Long, Product> editedProducts = new HashMap<>(); // Store edited rows

    @FXML
    public void initialize() {
        setupTable();
        fetchProductsFromApi();
    }

    private void setupTable() {
        productTable.setEditable(true); // Enable editing

        // Set up columns
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productGroupColumn.setCellValueFactory(new PropertyValueFactory<>("productGroup"));
        productUnitColumn.setCellValueFactory(new PropertyValueFactory<>("productUnit"));
        productStatusColumn.setCellValueFactory(new PropertyValueFactory<>("productStatus"));
        createTimeStampColumn.setCellValueFactory(new PropertyValueFactory<>("createTimeStamp"));

        // Make columns editable
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productGroupColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productUnitColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        productStatusColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Enable row tracking
        setupEditableColumn(productNameColumn, "productName");
        setupEditableColumn(productGroupColumn, "productGroup");
        setupEditableColumn(productUnitColumn, "productUnit");
        setupEditableColumn(productStatusColumn, "productStatus");

        // Apply row factory
        productTable.setRowFactory(tv -> new TableRow<>() {
            private final Button updateButton = new Button("💾 บันทึก");

            {
                // ✅ Setup Button Action
                updateButton.setOnAction(event -> {
                    Product product = getItem();
                    if (product != null) {
                        updateProductApi(product);
                        editedProducts.remove(product.getId()); // ✅ Remove from edited list after update
                        productTable.refresh(); // ✅ Refresh UI
                    }
                });
                updateButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                getStyleClass().removeAll("edited-row", "normal-row");

                if (empty || product == null) {
                    setGraphic(null); // Hide button for empty rows
                    getStyleClass().add("normal-row");
                } else {
                    if (editedProducts.containsKey(product.getId())) {
                        getStyleClass().add("edited-row"); // Apply edited row style

                        // ✅ Wrap content in HBox to keep button floating next to row
                        HBox rowContent = new HBox(10, updateButton);
                        rowContent.setStyle("-fx-alignment: center-right; -fx-padding: 5px;");
                        setGraphic(rowContent);
                    } else {
                        setGraphic(null); // Remove button if row is not edited
                    }
                }
            }
        });
    }

    private TableColumn<Product, Void> getActionColumn() {
        TableColumn<Product, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("💾 บันทึก");

            {
                updateButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    updateProductApi(product);
                    editedProducts.remove(product.getId()); // Remove from edited list after update
                    productTable.refresh(); // Refresh UI
                });
                updateButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || !editedProducts.containsKey(getTableView().getItems().get(getIndex()).getId())) {
                    setGraphic(null); // Hide button if row is NOT edited
                } else {
                    setGraphic(updateButton); // Show button if row is edited
                }
            }
        });
        return actionColumn;
    }

    void fetchProductsFromApi() {
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

    void scrollToLastRow() {
        if (!productTable.getItems().isEmpty()) {
            int lastRowIndex = productTable.getItems().size() - 1;
            productTable.scrollTo(lastRowIndex); // ✅ Scroll to the last item
            productTable.getSelectionModel().select(lastRowIndex); // ✅ Select last row
        }
    }

    @FXML
    public void handleOpenAddProductStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddProductView.fxml"));
            Parent root = loader.load();

            // Get the controller and pass reference
            AddProductController controller = loader.getController();
            controller.setParentController(this);  // Pass reference to refresh TableView

            // Apply CSS Manually (Debugging Step)
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());

            // Create a new Stage (Window)
            Stage stage = new Stage();
//            stage.setTitle("เพิ่มสินค้า");
            stage.setScene(scene);

            // 🏷️ Track Main Window (Stage) and Close AddProductView if Main Closes
//            Stage mainStage = (Stage) productTable.getScene().getWindow();
//            mainStage.setOnCloseRequest(event -> stage.close());

            // 🏷️ Make the AddProductView modal (blocks interaction with main window)
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(productTable.getScene().getWindow()); // Set owner as Main Stage

            stage.showAndWait(); // Wait for user to close
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupEditableColumn(TableColumn<Product, String> column, String property) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());

        column.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            String newValue = event.getNewValue();

            switch (property) {
                case "productName":
                    product.setProductName(newValue);
                    break;
                case "productGroup":
                    product.setProductGroup(newValue);
                    break;
                case "productUnit":
                    product.setProductUnit(newValue);
                    break;
                case "productStatus":
                    product.setProductStatus(newValue);
                    break;
            }

            editedProducts.put(product.getId(), product);

            productTable.requestFocus();
            productTable.refresh();

            System.out.println("Updated Product: " + product);
        });
    }

    @FXML
    public void handleUpdateAll() {
        if (editedProducts.isEmpty()) {
            System.out.println("No changes to update.");
            return;
        }

        System.out.println("Updating " + editedProducts.size() + " products...");

        for (Product product : editedProducts.values()) {
            updateProductApi(product); // Call API for each edited product
        }

        editedProducts.clear(); // ✅ Clear memory after updating
        productTable.refresh();
    }

    private static final String API_URL = "http://localhost:8080/api/products/";

    private void updateProductApi(Product product) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = API_URL + product.getId();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);

            ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Product updated successfully!");
            } else {
                System.out.println("❌ Failed to update product.");
            }
        } catch (Exception ex) {
            System.out.println("❌ Error updating product: " + ex.getMessage());
        }
    }

}
