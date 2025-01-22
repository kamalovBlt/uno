package ru.itis.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainPageController {

    @FXML
    private Button playButton;

    @FXML
    private Button createButton;

    @FXML
    private Button exitButton;

    @FXML
    private void handlePlayButtonClick() {
        System.out.println("play button clicked");
    }

    @FXML
    private void handleExitButtonClick() {
        Platform.exit();
    }

    @FXML
    private void handleCreateButtonClick() {

    }
}
