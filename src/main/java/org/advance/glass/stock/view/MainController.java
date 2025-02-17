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
        // Optionally, load the default view (e.g., dashboard) when the application starts.
        loadContent("/fxml/dashboard.fxml");
    }

    @FXML
    public void handleDashboard() {
        loadContent("/fxml/dashboard.fxml");
    }

    @FXML
    public void handleReceipt() {
        loadContent("/fxml/receipt_scene.fxml");
    }

    @FXML
    public void handleRequest() {
        loadContent("/fxml/request_scene.fxml");
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
