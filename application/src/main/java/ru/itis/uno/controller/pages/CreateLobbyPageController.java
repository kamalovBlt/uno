package ru.itis.uno.controller.pages;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.util.Optional;

public class CreateLobbyPageController implements RootPaneAware {

    @FXML
    private Text lobbyId;


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
                        new CreateLobbyRequestContent(client.getId())
                ),
                client.getSocket()
        );
        Optional<Response> optionalResponse = clientProtocolService.read(client.getSocket());
        if (optionalResponse.isPresent()) {
            Response response = optionalResponse.get();
            if (response.responseType().equals(ResponseType.LOBBY_ID)) {
                LobbyIdResponseContent lobbyIdResponseContent = (LobbyIdResponseContent) response.content();
                this.lobbyId.setText("ID: %s".formatted(String.valueOf(lobbyIdResponseContent.getId())));
            }
        }
    }

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

}
