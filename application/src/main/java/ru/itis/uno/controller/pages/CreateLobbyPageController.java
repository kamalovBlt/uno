package ru.itis.uno.controller.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.lobby.Lobby;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.request.content.RefreshLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.util.Optional;

public class CreateLobbyPageController implements RootPaneAware {

    @FXML
    private Text leftPlayer;

    @FXML
    private Text topPlayer;

    @FXML
    private Text rightPlayer;


    @FXML
    private Text lobbyId;

    private int intLobbyId;

    private Pane rootPane;

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

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
                intLobbyId = lobbyIdResponseContent.getId();
                this.lobbyId.setText("ID: %s".formatted(String.valueOf(lobbyIdResponseContent.getId())));
            }
        }

    }

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ClientProtocolService clientProtocolService = Client.getInstance().getClientProtocolService();
        clientProtocolService.send(
                new Request(
                        RequestType.REFRESH_LOBBY,
                        new RefreshLobbyRequestContent(
                                Client.getInstance().getId(),
                                intLobbyId
                        )
                ),
                Client.getInstance().getSocket()
        );
        Optional<Response> optionalResponse = clientProtocolService.read(Client.getInstance().getSocket());
        if (optionalResponse.isPresent()) {
            Response response = optionalResponse.get();
            if (response.responseType().equals(ResponseType.LOBBY_TO_CLIENT)) {
                LobbyToClientResponseContent lobbyToClientResponseContent = (LobbyToClientResponseContent) response.content();
                Lobby lobby = lobbyToClientResponseContent.getLobby();
                Player leftPlayer = lobby.leftPlayer();
                if (!leftPlayer.username().equals("0")) {
                    this.leftPlayer.setText(leftPlayer.username());
                }
                Player topPlayer = lobby.topPlayer();
                if (!topPlayer.username().equals("0")) {
                    this.topPlayer.setText(topPlayer.username());
                }
                Player rightPlayer = lobby.rightPlayer();
                if (!rightPlayer.username().equals("0")) {
                    this.rightPlayer.setText(rightPlayer.username());
                }
            }
        }

    }
}
