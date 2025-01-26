package ru.itis.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ru.itis.controller.util.FXMLLoaderUtil;

public class ProfilePageController implements RootPaneAware {

    private Pane rootPane;

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

    @FXML
    private void initialize() {

    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

}
