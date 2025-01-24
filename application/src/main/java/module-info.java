module ru.itis {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    exports ru.itis;
    exports ru.itis.controller;

    opens ru.itis.controller to javafx.fxml;

}
