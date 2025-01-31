package ru.itis.uno.controller.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.io.IOException;
import java.util.Objects;

public class PlayGoToLobbyController implements RootPaneAware {

    @FXML
    public TextField inputField;

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

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/templates/game/game.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
