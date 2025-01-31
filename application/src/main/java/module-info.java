module ru.itis {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires protocol;
    requires java.prefs;
    exports ru.itis.uno;
    exports ru.itis.uno.controller.pages;
    opens ru.itis.uno.controller.pages to javafx.fxml;
    exports ru.itis.uno.client;
    exports ru.itis.uno.controller.pages.game;
    opens ru.itis.uno.controller.pages.game to javafx.fxml;
}