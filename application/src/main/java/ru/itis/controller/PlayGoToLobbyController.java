package ru.itis.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ru.itis.controller.util.FXMLLoaderUtil;

public class PlayGoToLobbyController implements RootPaneAware {

    /*TODO: тут будет присоединение к лобби*/

    private Pane rootPane;

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

}
