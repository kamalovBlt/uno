package ru.itis.uno.controller.pages;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.pages.game.GameController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreateLobbyPageController {

    @FXML
    private Text leftPlayer;

    @FXML
    private Text topPlayer;

    @FXML
    private Text rightPlayer;


    @FXML
    private Text lobbyId;

    @FXML
    public void initialize() {
        Client client = Client.getInstance();
        ClientProtocolService clientProtocolService = client.getClientProtocolService();
        clientProtocolService.send(
                new Request(
                        RequestType.CREATE_LOBBY,
                        new CreateLobbyRequestContent(client.getId(), client.getUsername())
                ),
                client.getSocket()
        );
        Optional<Response> optionalResponse = clientProtocolService.read(client.getSocket());

        if (optionalResponse.isPresent()) {
            Response response = optionalResponse.get();
            if (response.responseType().equals(ResponseType.LOBBY_ID)) {
                LobbyIdResponseContent lobbyIdResponseContent = (LobbyIdResponseContent) response.content();
                int lobbyId = lobbyIdResponseContent.getId();
                this.lobbyId.setText("ID: %s".formatted(String.valueOf(lobbyId)));
                client.setCurrentGameId(lobbyId);
            }
        }
        WaitServerAnswerRunnable runnable = new WaitServerAnswerRunnable(clientProtocolService, leftPlayer, topPlayer, rightPlayer);
        new Thread(runnable).start();
    }

    private record WaitServerAnswerRunnable(
            ClientProtocolService clientProtocolService,
            Text leftPlayer,
            Text topPlayer,
            Text rightPlayer

    ) implements Runnable {
        @Override
        public void run() {
            Optional<Response> optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
            while (optionalRunnableResponse.isPresent()) {
                Response response = optionalRunnableResponse.get();
                if (response.responseType().equals(ResponseType.LOBBY_TO_CLIENT)) {
                    LobbyToClientResponseContent lobbyToClientResponseContent = (LobbyToClientResponseContent) response.content();
                    List<Player> lobby = lobbyToClientResponseContent.getPlayers();
                    List<Text> texts = List.of(leftPlayer, topPlayer, rightPlayer);
                    for (int i = 1; i < Math.min(lobby.size(), texts.size() + 1); ++i) {
                        texts.get(i - 1).setText(lobby.get(i).username());
                    }
                    optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
                }
                if (response.responseType().equals(ResponseType.GAME_STATE)) {
                    Platform.runLater(() -> {
                        Parent root;
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/templates/game/game.fxml"));
                            root = fxmlLoader.load();
                            GameController gameController = fxmlLoader.getController();
                            gameController.initialRendering((GameStateResponseContent) response.content());
                            Stage stage = (Stage) leftPlayer.getScene().getWindow();
                            stage.getScene().setRoot(root);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
            }
        }
    }
}
