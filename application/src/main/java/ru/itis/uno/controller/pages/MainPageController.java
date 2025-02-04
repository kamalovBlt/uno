package ru.itis.uno.controller.pages;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.layout.Pane;
import ru.itis.uno.services.AuthenticationService;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class MainPageController implements RootPaneAware {

    private AuthenticationService authenticationService;
    private Pane rootPane;

    @FXML
    public void initialize() {
        this.authenticationService = new AuthenticationService();
    }

    @FXML
    private void handlePlayButtonClick() {

        rootPane.getChildren().clear();

        if (authenticationService.checkAuthentication()) {

            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-play-go-to-lobby.fxml", rootPane);
        }

        else {
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-sign.fxml", rootPane);
        }

    }

    @FXML
    private void handleExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void handleCreateButtonClick() {

        rootPane.getChildren().clear();

        if (authenticationService.checkAuthentication()) {
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-create-lobby.fxml", rootPane);
        }

        else {
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-sign.fxml", rootPane);
        }

    }

    @FXML
    private void handleProfileButtonClick() {

        rootPane.getChildren().clear();

        if (authenticationService.checkAuthentication()) {
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-profile.fxml", rootPane);
        }
        else {
            FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-sign.fxml", rootPane);
        }

    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

}
