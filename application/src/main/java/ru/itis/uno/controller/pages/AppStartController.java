package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

public class AppStartController implements RootPaneAware {

    @FXML
    public Pane rootPane;

    @FXML
    public void initialize() {

        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }


    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }
}
