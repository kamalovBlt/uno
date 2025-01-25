package ru.itis.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.layout.Pane;
import ru.itis.controller.util.FXMLLoaderUtil;

public class MainPageController implements RootPaneAware {

    private Pane rootPane;

    @FXML
    private void handlePlayButtonClick() {
        /*TODO: надо будет проверить зарегистрирован ли пользователь*/
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-play-go-to-lobby.fxml", rootPane);
    }

    @FXML
    private void handleExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void handleCreateButtonClick() {

        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-create-lobby.fxml", rootPane);

    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }
}
