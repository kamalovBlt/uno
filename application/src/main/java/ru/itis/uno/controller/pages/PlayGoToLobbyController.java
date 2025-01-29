package ru.itis.uno.controller.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class PlayGoToLobbyController implements RootPaneAware {

    @FXML
    public TextField inputField;

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

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        System.out.println();
    }
}
