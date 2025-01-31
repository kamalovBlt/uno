package ru.itis.uno.controller.pages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import ru.itis.lobby.Lobby;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.JoinLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.FXMLLoaderUtil;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static javafx.scene.control.TextFormatter.*;


public class PlayGoToLobbyController implements RootPaneAware {

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    @FXML
    private TextField inputField;

    @FXML
    private Text info;

    @FXML
    private Text error;

    private Pane rootPane;

    @FXML
    private void handleBackButton() {
        rootPane.getChildren().clear();
        FXMLLoaderUtil.loadFXMLToPane("/view/templates/main-page-main-buttons.fxml", rootPane);
    }

    @Override
    public void setRootPane(Pane pane) {
        this.rootPane = pane;
    }

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
                    Lobby lobby = lobbyToClientResponseContent.getLobby();
                    waiting(lobby);
                }

                if (response.responseType().equals(ResponseType.ERROR)) {
                    error.setText("Invalid lobby or lobby full");
                }

            }
        }
        else {
            error.setText("Please enter a number");
        }

        /*try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/templates/game/game.fxml")));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    private void waiting(Lobby lobby) {
        submitButton.setDisable(true);
        backButton.setDisable(true);
        this.info.setText("Ожидаем начала игры. LOBBY ID=" + Integer.parseInt(inputField.getText()));
    }

}
