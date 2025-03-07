package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.advance.glass.stock.model.db.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class AddProductController {

    @FXML
    private Label labelHeader;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productGroupField;
    @FXML
    private TextField productUnitField;
    @FXML
    private Button saveButton;
//    @FXML private Button cancelButton;

    private static final String API_URL = "http://localhost:8080/api/products";
    @Setter
    private MasterProductViewController parentController;  // Reference to MainController

//    @FXML
//    public void initialize() {
//        URL fontUrl = getClass().getResource("/fonts/Prompt-Regular.ttf");
//        System.out.println("Font URL: " + fontUrl);
//
//        if (fontUrl != null) {
//            Font customFont = Font.loadFont(fontUrl.toExternalForm(), 26);
//            System.out.println("Font Loaded: " + customFont);
//            labelHeader.setFont(customFont);
//
//            // 🔄 Force JavaFX to redraw
//            Platform.runLater(() -> {
//                labelHeader.applyCss();
//                labelHeader.layout();
//            });
//        } else {
//            System.out.println("❌ Font not found! Check your path.");
//        }
//    }

    @FXML
    public void handleSaveProduct() {
        System.out.println("🔹 Checking saveButton: " + saveButton);

        if (saveButton == null) {
            System.out.println("❌ Error: saveButton is NULL!");
            return;
        }

        String name = productNameField.getText();
        String group = productGroupField.getText();
        String unit = productUnitField.getText();

        if (name.isEmpty() || group.isEmpty() || unit.isEmpty()) {
            System.out.println("⚠️ Please fill all fields!");
            return;
        }

        Product newProduct = new Product(null, name, group, unit, "ACTIVE", LocalDateTime.now(), LocalDateTime.now());
        sendProductToApi(newProduct);

        // ✅ Close window safely
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
        // Refresh TableView in Main Controller
        if (parentController != null) {
            parentController.fetchProductsFromApi();
            parentController.scrollToLastRow();
        }
    }

    @FXML
    public void handleCancel() {
        Stage stage = (Stage) productNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handleClearFields() {
        // 🧹 Clear all input fields
        productNameField.clear();
        productGroupField.clear();
        productUnitField.clear();
        System.out.println("✅ Fields cleared successfully!");
    }


    /**
     * ✅ Send Product to API
     */
    private void sendProductToApi(Product product) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Product> requestEntity = new HttpEntity<>(product, headers);

            ResponseEntity<Product> response = restTemplate.postForEntity(API_URL, requestEntity, Product.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Product added successfully!");
            } else {
                System.out.println("❌ Failed to create product.");
            }
        } catch (Exception ex) {
            System.out.println("❌ Error creating product: " + ex.getMessage());
        }
    }
}
