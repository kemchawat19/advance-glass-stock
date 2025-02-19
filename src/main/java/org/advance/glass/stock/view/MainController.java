package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        // Load a default view, e.g., Stock.
        loadContent("/fxml/stock_scene.fxml");

        // When the contentPane is attached to a Scene, set up key event handling.
        contentPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    KeyCode code = event.getCode();
                    switch (code) {
                        case DIGIT1:
                            handleStock();
                            break;
                        case DIGIT2:
                            handleReceiptEntry();
                            break;
                        case DIGIT3:
                            handleRequestEntry();
                            break;
                        case DIGIT4:
                            handleReturnEntry();
                            break;
                        case DIGIT5:
                            handleJob();
                            break;
                        case DIGIT6:
                            handleReport();
                            break;
                        case DIGIT7:
                            handleMasterProduct();
                            break;
                        case DIGIT8:
                            handleMasterCustomer();
                            break;
                        case DIGIT9:
                            handleMasterSupplier();
                            break;
                        default:
                            // Do nothing for other keys.
                            break;
                    }
                });
            }
        });
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
    public void handleReturnEntry() {
        loadContent("/fxml/return_scene.fxml");
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
