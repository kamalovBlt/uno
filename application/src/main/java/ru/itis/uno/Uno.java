package ru.itis.uno;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import ru.itis.uno.client.SessionManager;

import java.io.IOException;

public class Uno extends Application {

    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
        try {
            Parent root = fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SessionManager.clearSession();
        launch();
    }

}