module ru.itis {
    requires javafx.controls;
    requires javafx.fxml;
    exports ru.itis;
    exports ru.itis.controller;

    opens ru.itis.controller to javafx.fxml;

}
