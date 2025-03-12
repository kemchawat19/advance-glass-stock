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
    @FXML
    private TextField searchNameField;
    @FXML
    private TableColumn<Product, Void> actionColumn; // Declare in Controller

    private final Map<Long, Product> editedProducts = new HashMap<>(); // Store edited rows

    @FXML
    public void initialize() {
        setupTable();
        fetchProductsFromApi();
        setupSearchField();
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

        // Make columns editable and add ENTER key event
        setupEditableColumn(productNameColumn, "productName");
        setupEditableColumn(productGroupColumn, "productGroup");
        setupEditableColumn(productUnitColumn, "productUnit");
        setupEditableColumn(productStatusColumn, "productStatus");

        productTable.focusedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("TableView focused: " + newVal);
        });

        productTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                // Clear all previous styling
                getStyleClass().removeAll("edited-row");

                if (empty || product == null) {
                    setStyle(""); // Default row style
                } else if (editedProducts.containsKey(product.getId())) {
                    getStyleClass().add("edited-row"); // Add custom style if edited
                }

                // ✅ Fix Hover: Apply ONLY to non-edited rows
                this.setOnMouseEntered(event -> {
                    if (!editedProducts.containsKey(product.getId())) {
                        setStyle("-fx-background-color: #f3f4f6;"); // Light gray hover effect
                        getStyleClass().add("edited-row");
                    }
                });

                this.setOnMouseExited(event -> {
                    if (!editedProducts.containsKey(product.getId())) {
                        setStyle(""); // Reset hover
                    } else {
                        getStyleClass().add("edited-row"); // Reapply edited row color
                        setStyle(""); // Let CSS control the color
                    }
                });
            }
        });

        // ✅ Assign custom button cell to `actionColumn`
        setupActionColumn();

        // ✅ Automatically Resize Columns When Table Resizes
        productTable.widthProperty().addListener((obs, oldWidth, newWidth) -> adjustColumnResizePolicy());
    }

    // ✅ Dynamically Adjust Column Widths When Table Size Changes
    private void adjustColumnResizePolicy() {
        double tableWidth = productTable.getWidth();
        double totalFixedWidth = 100 + 150 + 120 + 120 + 100 + 150 + 140; // Sum of default column widths

        System.out.println("tableWidth = " + tableWidth);
        System.out.println("totalFixedWidth = " + totalFixedWidth);

        if (tableWidth >= totalFixedWidth) {
            // ✅ If columns fit inside table, use CONSTRAINED_RESIZE_POLICY
            productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            // ✅ Set default column widths
            productIdColumn.setPrefWidth(100);
            productIdColumn.setResizable(false);
            productNameColumn.setPrefWidth(330);
            productNameColumn.setResizable(false);
            createTimeStampColumn.setPrefWidth(170);
            createTimeStampColumn.setResizable(false);
            actionColumn.setPrefWidth(150);
            actionColumn.setResizable(false);

            // Allow the other columns to auto-resize
            productGroupColumn.setPrefWidth(150);
            productGroupColumn.setResizable(false);
            productUnitColumn.setPrefWidth(150);
            productUnitColumn.setResizable(false);
            productStatusColumn.setPrefWidth(150);
            productStatusColumn.setResizable(false);
        } else {
            // ✅ If columns overflow, reset to default widths (enables scrollbar)
            productTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            // ✅ Keep the predefined column widths
            productIdColumn.setPrefWidth(100);
            productNameColumn.setPrefWidth(150);
            productGroupColumn.setPrefWidth(120);
            productUnitColumn.setPrefWidth(120);
            productStatusColumn.setPrefWidth(100);
            createTimeStampColumn.setPrefWidth(150);
            actionColumn.setPrefWidth(140);
        }
    }

    private long newProductIdCounter = -1; // Negative IDs for duplicated rows

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("💾 บันทึก");
            private final Button deleteButton = new Button("🗑️ ลบ");

            {
                updateButton.getStyleClass().add("action-update-button");
                deleteButton.getStyleClass().add("action-delete-button");

                updateButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());

                    // ✅ If the row is NEW (negative ID), call CREATE API
                    if (product.getId() < 0) {
                        sendProductToApi(product);
                    } else {
                        updateProductApi(product);
                    }
                });

                // ✅ Delete button logic (only works when editedProducts is empty)
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());

                    if (!editedProducts.isEmpty()) {
                        System.out.println("❌ Cannot delete while editing.");
                        return;
                    }

                    deleteProductApi(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Hide button for empty rows
                } else {
                    Product product = getTableView().getItems().get(getIndex());

                    updateButton.setDisable(!editedProducts.containsKey(product.getId()));

                    deleteButton.setDisable(!editedProducts.isEmpty());

//                    // ✅ Apply the correct background color for updated rows
//                    TableRow<Product> row = getTableRow();
//                    if (row != null) {
//                        row.getStyleClass().removeAll("edited-row", "normal-row"); // Clear previous styles
//
//                        if (editedProducts.containsKey(product.getId())) {
//                            row.getStyleClass().add("edited-row"); // ✅ Apply the edited-row CSS class
//                        } else {
//                            row.setStyle(""); // Default style
//                        }
//                    }

                    // ✅ Align button properly
                    HBox container = new HBox(5, updateButton, deleteButton);
                    container.setStyle("-fx-alignment: center;");
                    setGraphic(container);
                }
            }
        });
    }

    void fetchProductsFromApi() {
        System.out.println("fetchProductsFromApi");
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

                editedProducts.remove(product.getId()); // ✅ Remove using correct Long key

                if (editedProducts.isEmpty()) {
                    fetchProductsFromApi(); // Refresh table
                }
                productTable.refresh();
            } else {
                System.out.println("Failed to create product.");
            }
        } catch (Exception ex) {
            System.out.println("Error creating product: " + ex.getMessage());
        }
    }

    void scrollToLastRow() {
        System.out.println("scrollToLastRow1");
        if (!productTable.getItems().isEmpty()) {
            System.out.println("scrollToLastRow2");
            System.out.println("productTable.getItems().size() = " + productTable.getItems().size());
            int lastRowIndex = productTable.getItems().size();
            System.out.println("lastRowIndex = " + lastRowIndex);
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

            System.out.println("editedProducts setupEditableColumn = " + editedProducts);

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

        // ✅ Create a separate list of product IDs to avoid ConcurrentModificationException
        List<Long> productIds = new ArrayList<>(editedProducts.keySet());

        for (Long productId : productIds) {
            Product product = editedProducts.get(productId);
            if (product == null) continue;

            System.out.println("Updating product: " + product);

            if (product.getId() < 0) {
                sendProductToApi(product);
            } else {
                updateProductApi(product);
            }

            // ✅ Remove only after API call
            editedProducts.remove(productId);
        }

        // ✅ Refresh the table AFTER all updates are done
        productTable.refresh();
    }

    private void updateProductApi(Product product) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            String url = "http://localhost:8080/api/products/" + product.getId();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);

            ResponseEntity<Product> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Product.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Product updated successfully!");

                editedProducts.remove(product.getId()); // ✅ Remove using correct Long key

                if (editedProducts.isEmpty()) {
                    fetchProductsFromApi(); // Refresh table
                }
                productTable.refresh();
            } else {
                System.out.println("❌ Failed to update product.");
            }
        } catch (Exception ex) {
            System.out.println("❌ Error updating product: " + ex.getMessage());
        }
    }

    @FXML
    private void handleCopyRow() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            System.out.println("❌ No row selected to copy.");
            return;
        }

        // ✅ Create a new Product with a unique temporary ID
        Product duplicatedProduct = new Product();
        duplicatedProduct.setId(newProductIdCounter--); // Decrement to ensure unique ID
        duplicatedProduct.setProductName(selectedProduct.getProductName());
        duplicatedProduct.setProductGroup(selectedProduct.getProductGroup());
        duplicatedProduct.setProductUnit(selectedProduct.getProductUnit());
        duplicatedProduct.setProductStatus(selectedProduct.getProductStatus());
        duplicatedProduct.setCreateTimeStamp(selectedProduct.getCreateTimeStamp());

        // ✅ Add duplicated row to table
        productTable.getItems().add(duplicatedProduct);

        // ✅ Store duplicated row using Long key (ID-based)
        editedProducts.put(duplicatedProduct.getId(), duplicatedProduct);

        scrollToLastRow();

        // ✅ Refresh table to reflect new row
        productTable.refresh();

        System.out.println("✅ Copied Row: " + duplicatedProduct);
    }

    private void deleteProductApi(Product product) {
        if (product.getId() < 0) {
            System.out.println("❌ Cannot delete unsaved product.");
            return;
        }

        try {
            String API_URL = "http://localhost:8080/api/products/" + product.getId();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(API_URL, HttpMethod.DELETE, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Product deleted successfully!");

                // ✅ Remove the product from TableView
                productTable.getItems().remove(product);
                productTable.refresh();
            } else {
                System.out.println("❌ Failed to delete product.");
            }
        } catch (Exception ex) {
            System.out.println("❌ Error deleting product: " + ex.getMessage());
        }
    }

    private void searchProductsFromApi(String productName) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            String API_URL = "http://localhost:8080/api/products/search?productName=" + productName;

            ResponseEntity<Product[]> response = restTemplate.getForEntity(API_URL, Product[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<Product> searchResults = Arrays.asList(response.getBody());
                ObservableList<Product> searchObservableList = FXCollections.observableArrayList(searchResults);
                productTable.setItems(searchObservableList);
            } else {
                System.out.println("⚠️ No products found for: " + productName);
            }
        } catch (Exception ex) {
            System.out.println("❌ Error fetching search results: " + ex.getMessage());
        }
    }


    private void setupSearchField() {
        searchNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.trim().isEmpty()) {
                fetchProductsFromApi(); // If empty, fetch all products
            } else {
                searchProductsFromApi(newValue);
            }
        });
    }
}
