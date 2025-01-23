package ru.itis.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.layout.AnchorPane;


public class MainPageController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        System.out.println(rootPane == null);
    }

    @FXML
    private void handlePlayButtonClick() {
    }

    @FXML
    private void handleExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void handleCreateButtonClick() {
    }

}
