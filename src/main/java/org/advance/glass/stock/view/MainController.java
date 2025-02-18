package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        // Optionally load a default view, e.g., Stock.
        loadContent("/fxml/stock_scene.fxml");
    }

    @FXML
    public void handleStock() {
        loadContent("/fxml/stock_scene.fxml");
    }

    @FXML
    public void handleReceiptEntry() {
        loadContent("/fxml/receipt_scene.fxml");
    }

    @FXML
    public void handleRequestEntry() {
        loadContent("/fxml/request_scene.fxml");
    }

    @FXML
    public void handleJob() {
        loadContent("/fxml/job_scene.fxml");
    }

    @FXML
    public void handleReport() {
        loadContent("/fxml/report_scene.fxml");
    }

    @FXML
    public void handleMasterProduct() {
        loadContent("/fxml/master_product.fxml");
    }

    @FXML
    public void handleMasterCustomer() {
        loadContent("/fxml/master_customer.fxml");
    }

    @FXML
    public void handleMasterSupplier() {
        loadContent("/fxml/master_supplier.fxml");
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            contentPane.getChildren().setAll(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
