package ru.itis.uno.controller.pages.game;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.itis.cards.Card;
import ru.itis.cards.CardColor;
import ru.itis.cards.CardType;
import ru.itis.lobby.GameState;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CoverCardRequestContent;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.CardLoader;
import ru.itis.uno.services.WaitAnswerAndRefreshSceneRunnable;

import java.util.List;

public class GameController {


    private Client client;
    private ClientProtocolService clientProtocolService;
    private GameState gameState;

    @FXML
    private HBox deck;

    @FXML
    private VBox centerVBox;

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

    public void initialRendering(GameStateResponseContent gameStateResponseContent) {
        this.gameState = gameStateResponseContent.getGameState();
        Player receiverPlayer = gameState.getReceiverPlayer();
        List<Card> cards = receiverPlayer.cards();
        for (Card card : cards) {
            CardLoader.addCardToClientDeck(card, clientProtocolService, gameState, bottomHBox);
        }
        List<Player> otherPlayers = gameState.getOtherPlayers();

        for (int i = 0; i < otherPlayers.size(); i++) {
            List<Card> otherPlayerCards = otherPlayers.get(i).cards();
            for (Card otherPlayerCard : otherPlayerCards) {
                if (i == 0) {
                    CardLoader.addCardToOpponentDeck(otherPlayerCard, leftVBox, "reverse-left.png");
                }
                if (i == 1) {
                    CardLoader.addCardToOpponentDeck(otherPlayerCard, topHBox, "reverse.png");
                }
                if (i == 2) {
                    CardLoader.addCardToOpponentDeck(otherPlayerCard, rightVBox, "reverse-right.png");
                }
            }
        }

        WaitAnswerAndRefreshSceneRunnable waitAnswerAndRefreshSceneRunnable = new WaitAnswerAndRefreshSceneRunnable(clientProtocolService, gamePane);
        new Thread(waitAnswerAndRefreshSceneRunnable).start();

    }

    public void handleTakeCard(MouseEvent mouseEvent) {

    }
}
