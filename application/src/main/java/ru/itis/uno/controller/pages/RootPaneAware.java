package ru.itis.uno.controller.pages;

import javafx.scene.layout.Pane;
/**
 * Интерфейс для объектов требующих корневого узла шаблона приложения*/
public interface RootPaneAware {
    void setRootPane(Pane pane);
}
