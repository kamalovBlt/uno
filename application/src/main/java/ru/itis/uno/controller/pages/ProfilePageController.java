package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.uno.client.SessionManager;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class ProfilePageController implements RootPaneAware {

    @FXML
    public Text id;

    @FXML
    public Text username;

    private Pane rootPane;

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

    @FXML
    private void initialize() {
        id.setText("ID:" + SessionManager.getId());
        username.setText("Username:" + SessionManager.getUsername());
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

    public void handleLeaveOutButton() {
        SessionManager.clearSession();

        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);

    }
}
