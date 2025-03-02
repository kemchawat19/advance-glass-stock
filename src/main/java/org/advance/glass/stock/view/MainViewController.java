package org.advance.glass.stock.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainViewController {

    @FXML
    private StackPane contentPane;
    @FXML
    private Button stockButton;
    @FXML
    private Button receiptEntryButton;
    @FXML
    private Button requestEntryButton;
    @FXML
    private Button returnEntryButton;
    @FXML
    private Button jobButton;
    @FXML
    private Button reportButton;
    @FXML
    private Button masterProductButton;
    @FXML
    private Button masterCustomerButton;
    @FXML
    private Button masterSupplierButton;
    @FXML
    private TitledPane masterFilesPane;

    private Button activeButton = null;
    private final Map<Button, String> buttonViewMap = new HashMap<>();

    @FXML
    public void initialize() {
        // Map buttons to corresponding views
        buttonViewMap.put(stockButton, "/fxml/StockView.fxml");
        buttonViewMap.put(receiptEntryButton, "/fxml/ReceiptView.fxml");
        buttonViewMap.put(requestEntryButton, "/fxml/RequestView.fxml");
        buttonViewMap.put(returnEntryButton, "/fxml/ReturnView.fxml");
        buttonViewMap.put(jobButton, "/fxml/JobView.fxml");
        buttonViewMap.put(reportButton, "/fxml/ReportView.fxml");
        buttonViewMap.put(masterProductButton, "/fxml/MasterProductView.fxml");
        buttonViewMap.put(masterCustomerButton, "/fxml/MasterCustomerView.fxml");
        buttonViewMap.put(masterSupplierButton, "/fxml/MasterSupplierView.fxml");

        // Set up button click handling
        buttonViewMap.forEach((button, fxmlPath) -> {
            if (button != null) {
                button.setOnAction(event -> {
                    collapseTitledPaneIfNeeded(button);
                    setActiveButton(button, fxmlPath);
                });
            }
        });

        // Load default view (Stock)
        setActiveButton(stockButton, "/fxml/StockView.fxml");

        // Setup keyboard shortcuts
        contentPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    KeyCode code = event.getCode();
                    switch (code) {
                        case DIGIT1 -> setActiveButton(stockButton, "/fxml/StockView.fxml");
                        case DIGIT2 -> setActiveButton(receiptEntryButton, "/fxml/ReceiptView.fxml");
                        case DIGIT3 -> setActiveButton(requestEntryButton, "/fxml/RequestView.fxml");
                        case DIGIT4 -> setActiveButton(returnEntryButton, "/fxml/ReturnView.fxml");
                        case DIGIT5 -> setActiveButton(jobButton, "/fxml/JobView.fxml");
                        case DIGIT6 -> setActiveButton(reportButton, "/fxml/ReportView.fxml");
                        case DIGIT7 -> setActiveButton(masterProductButton, "/fxml/MasterProductView.fxml");
                        case DIGIT8 -> setActiveButton(masterCustomerButton, "/fxml/MasterCustomerView.fxml");
                        case DIGIT9 -> setActiveButton(masterSupplierButton, "/fxml/MasterSupplierView.fxml");
                        default -> {
                        }
                    }
                });
            }
        });
    }

    private void setActiveButton(Button clickedButton, String fxmlPath) {
        // Reset previous button
        if (activeButton != null) {
            activeButton.getStyleClass().remove("nav-button-active");
        }

        // Set new active button
        clickedButton.getStyleClass().add("nav-button-active");
        activeButton = clickedButton;

        // Load content
        loadContent(fxmlPath);
    }

    private void collapseTitledPaneIfNeeded(Button clickedButton) {
        // If clicking outside "Master Files", collapse it
        if (!isMasterFileButton(clickedButton) && masterFilesPane.isExpanded()) {
            masterFilesPane.setExpanded(false);
        }
    }

    private boolean isMasterFileButton(Button button) {
        return button == masterProductButton || button == masterCustomerButton || button == masterSupplierButton;
    }

    private void loadContent(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent content = loader.load();
            contentPane.getChildren().setAll(content);
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }
}
