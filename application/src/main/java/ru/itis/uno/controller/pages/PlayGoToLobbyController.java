package ru.itis.uno.controller.pages;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.JoinLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.pages.game.GameController;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static javafx.scene.control.TextFormatter.*;


public class PlayGoToLobbyController {


    @FXML
    private Button submitButton;

    @FXML
    private TextField inputField;

    @FXML
    private Text info;
    @FXML
    private Text lobbyId;

    @FXML
    private Text error;

    @FXML
    public void initialize() {

        UnaryOperator<Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        inputField.setTextFormatter(textFormatter);
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        if (!inputField.getText().isEmpty()) {
            int lobbyId = Integer.parseInt(inputField.getText());
            this.lobbyId.setText("LobbyId: " + lobbyId);
            ClientProtocolService clientProtocolService = Client.getInstance().getClientProtocolService();
            clientProtocolService.send(
                    new Request(
                            RequestType.JOIN_LOBBY,
                            new JoinLobbyRequestContent(
                                    Client.getInstance().getId(),
                                    lobbyId,
                                    Client.getInstance().getUsername()
                            )
                    ),
                    Client.getInstance().getSocket()
            );
            Optional<Response> optionalResponse = clientProtocolService.read(Client.getInstance().getSocket());
            if (optionalResponse.isPresent()) {
                Response response = optionalResponse.get();
                if (response.responseType().equals(ResponseType.LOBBY_TO_CLIENT)) {
                    LobbyToClientResponseContent lobbyToClientResponseContent = (LobbyToClientResponseContent) response.content();
                    int lobbySize = lobbyToClientResponseContent.getPlayers().size();
                    submitButton.setVisible(false);
                    inputField.setVisible(false);
                    info.setText("Waiting all players, current lobby size: " + lobbySize);
                    Client.getInstance().setCurrentGameId(lobbyId);
                    WaitServerAnswerRunnable runnable = new WaitServerAnswerRunnable(clientProtocolService, info, actionEvent);
                    new Thread(runnable).start();
                }
                if (response.responseType().equals(ResponseType.GAME_STATE)) {
                    Client.getInstance().setCurrentGameId(lobbyId);
                    setGameScene(actionEvent, response, getClass().getResource("/view/templates/game/game.fxml"));
                }
                if (response.responseType().equals(ResponseType.ERROR)) {
                    error.setText(new ErrorResponseContent(response.content().toByteArray()).getMessage());
                }
            }
        }
    }

    private static void setGameScene(ActionEvent actionEvent, Response response, URL resource) {
        Platform.runLater(() -> {
            Parent root;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(resource);
                root = fxmlLoader.load();
                GameController gameController = fxmlLoader.getController();
                gameController.initialRendering((GameStateResponseContent) response.content());
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private record WaitServerAnswerRunnable(
            ClientProtocolService clientProtocolService,
            Text info,
            ActionEvent actionEvent
    ) implements Runnable {
        @Override
        public void run() {
            Optional<Response> optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
            while (optionalRunnableResponse.isPresent()) {
                Response runnableResponse = optionalRunnableResponse.get();
                if (runnableResponse.responseType().equals(ResponseType.LOBBY_TO_CLIENT)) {
                    LobbyToClientResponseContent runnableLobbyToClientResponseContent = (LobbyToClientResponseContent) runnableResponse.content();
                    int runnableLobbySize = runnableLobbyToClientResponseContent.getPlayers().size();
                    info.setText("Waiting all players, current lobby size: " + runnableLobbySize);
                    optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
                }
                if (runnableResponse.responseType().equals(ResponseType.GAME_STATE)) {
                    setGameScene(actionEvent, runnableResponse, getClass().getResource("/view/templates/game/game.fxml"));
                    break;
                }
            }
        }
    }
}
