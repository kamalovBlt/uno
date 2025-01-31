package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class SignPageController implements RootPaneAware{

    private Pane rootPane;

    @FXML
    public void handleSignInButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-sign-in.fxml", rootPane);
    }

    @FXML
    public void handleSignUpButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-sign-up.fxml", rootPane);
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }
}
