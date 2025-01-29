package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class CreateLobbyPageController implements RootPaneAware {

    @FXML
    public Text lobbyId;

    private Pane rootPane;

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

    @FXML
    public void initialize() {
        this.lobbyId.setText("ID: 123456");
    }

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

}
