package ru.itis.uno.controller.pages.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.lobby.Lobby;

public class GameController {

    private Client client;
    private ClientProtocolService clientProtocolService;
    private Lobby lobby;

    @FXML
    private HBox deck;

    @FXML
    private HBox topHBox;

    @FXML
    private VBox leftVBox;

    @FXML
    private HBox bottomHBox;

    @FXML
    private VBox rightVBox;

    @FXML
    private Pane gamePane;

    @FXML
    public void initialize() {
        this.client = Client.getInstance();
        this.clientProtocolService = client.getClientProtocolService();
    }

    public void handleUnoButton(ActionEvent actionEvent) {

    }

    public void handleMove(MouseEvent actionEvent) {
        System.out.println("URA");
    }
}
