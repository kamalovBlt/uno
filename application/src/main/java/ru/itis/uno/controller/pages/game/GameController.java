package ru.itis.uno.controller.pages.game;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.itis.cards.Card;
import ru.itis.lobby.GameState;
import ru.itis.lobby.Player;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.CardLoader;

import java.util.List;
import java.util.Optional;

public class GameController {

    private Client client;
    private ClientProtocolService clientProtocolService;
    private GameState gameState;

    @FXML
    private HBox deck;

    @FXML
    private Line arrow;

    @FXML
    private StackPane centerStackPane;

    @FXML
    private Text bottomPlayerText;
    private int bottomPlayerId;

    @FXML
    private Text topPlayerText;
    private int topPlayerId;

    @FXML
    private Text leftPlayerText;
    private int leftPlayerId;

    @FXML
    private Text rightPlayerText;
    private int rightPlayerId;

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

    private RotateTransition rotateTransition;
    private boolean clockwise = true;

    @FXML
    public void initialize() {
        this.client = Client.getInstance();
        this.clientProtocolService = client.getClientProtocolService();

        if (arrow == null) {
            arrow = new Line(200, 100, 250, 50);
            arrow.setStroke(Color.RED);
            arrow.setStrokeWidth(3);
            gamePane.getChildren().add(arrow);
        }

        rotateTransition = new RotateTransition(Duration.seconds(2), arrow);
        rotateTransition.setByAngle(clockwise ? 360 : -360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);

    }

    public void handleUnoButton(ActionEvent actionEvent) {

    }

    public void initialRendering(GameStateResponseContent gameStateResponseContent) {

        this.gameState = gameStateResponseContent.getGameState();

        Player receiverPlayer = gameState.getReceiverPlayer();
        bottomPlayerText.setText(receiverPlayer.username());
        bottomPlayerId = receiverPlayer.id();

        List<Player> otherPlayers = gameState.getOtherPlayers();

        Player leftPlayer = otherPlayers.getFirst();
        Player topPlayer = otherPlayers.get(1);
        Player rightPlayer = otherPlayers.get(2);

        leftPlayerText.setText(leftPlayer.username());
        leftPlayerId = leftPlayer.id();

        topPlayerText.setText(topPlayer.username());
        topPlayerId = topPlayer.id();

        rightPlayerText.setText(rightPlayer.username());
        rightPlayerId = rightPlayer.id();

        updateScene(gameStateResponseContent.getGameState());

        getServerAnswer();

        startArrowAnimation();

    }

    public void getServerAnswer() {
        Thread networkThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Optional<Response> optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
                    if (optionalRunnableResponse.isPresent()) {
                        Response runnableResponse = optionalRunnableResponse.get();
                        if (runnableResponse.responseType().equals(ResponseType.GAME_STATE)) {
                            GameStateResponseContent gameStateResponseContent = (GameStateResponseContent) runnableResponse.content();
                            gameState = gameStateResponseContent.getGameState();
                            updateScene(gameState);
                            updateArrowDirection();
                        }
                        if (runnableResponse.responseType().equals(ResponseType.ERROR)) {
                            ErrorResponseContent content = (ErrorResponseContent) runnableResponse.content();
                            System.out.println(content.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("WaitAnswerAndRefreshScene failed");
            }
        });

        networkThread.setDaemon(true);
        networkThread.start();
    }

    public void handleTakeCard(MouseEvent mouseEvent) {

    }

    public void updateScene(GameState newGameState) {
        Platform.runLater(() -> {
            this.gameState = newGameState;

            centerStackPane.getChildren().clear();
            centerStackPane.getChildren().add(CardLoader.getCardImageView(gameState.getCurrentCard()));

            bottomHBox.getChildren().clear();
            leftVBox.getChildren().clear();
            topHBox.getChildren().clear();
            rightVBox.getChildren().clear();

            Player receiverPlayer = gameState.getReceiverPlayer();
            bottomPlayerText.setText(receiverPlayer.username());
            bottomPlayerId = receiverPlayer.id();
            for (Card card : receiverPlayer.cards()) {
                CardLoader.addCardToClientDeck(card, clientProtocolService, gameState, bottomHBox);
            }

            List<Player> otherPlayers = gameState.getOtherPlayers();
            for (int i = 0; i < otherPlayers.size(); i++) {
                Player player = otherPlayers.get(i);
                List<Card> otherPlayerCards = player.cards();
                if (i == 0) {
                    leftPlayerText.setText(player.username());
                    leftPlayerId = player.id();
                    for (Card card : otherPlayerCards) {
                        CardLoader.addCardToOpponentDeck(card, leftVBox, "reverse-left.png");
                    }
                }
                if (i == 1) {
                    topPlayerText.setText(player.username());
                    topPlayerId = player.id();
                    for (Card card : otherPlayerCards) {
                        CardLoader.addCardToOpponentDeck(card, topHBox, "reverse.png");
                    }
                }
                if (i == 2) {
                    rightPlayerText.setText(player.username());
                    rightPlayerId = player.id();
                    for (Card card : otherPlayerCards) {
                        CardLoader.addCardToOpponentDeck(card, rightVBox, "reverse-right.png");
                    }
                }
            }

            resetPlayerTextColors();
            int currentMovePlayerId = gameState.getCurrentMovePlayerId();
            if (currentMovePlayerId == bottomPlayerId) {
                bottomPlayerText.setFill(Color.RED);
            } else if (currentMovePlayerId == topPlayerId) {
                topPlayerText.setFill(Color.RED);
            } else if (currentMovePlayerId == rightPlayerId) {
                rightPlayerText.setFill(Color.RED);
            } else if (currentMovePlayerId == leftPlayerId) {
                leftPlayerText.setFill(Color.RED);
            }
        });
    }

    private void resetPlayerTextColors() {
        bottomPlayerText.setFill(Color.BLACK);
        topPlayerText.setFill(Color.BLACK);
        leftPlayerText.setFill(Color.BLACK);
        rightPlayerText.setFill(Color.BLACK);
    }

    private void startArrowAnimation() {
        rotateTransition.play();
    }

    private void updateArrowDirection() {
        Platform.runLater(() -> {
            rotateTransition.stop();

            int currentPlayerIndex = getCurrentPlayerIndex();
            int nextPlayerIndex = getNextPlayerIndex(currentPlayerIndex);

            if ((nextPlayerIndex - currentPlayerIndex + 4) % 4 == 1) {
                clockwise = true;
            } else {
                clockwise = false;
            }

            rotateTransition.setByAngle(clockwise ? 360 : -360);
            rotateTransition.playFromStart();
        });
    }

    private int getCurrentPlayerIndex() {
        int currentPlayerId = gameState.getCurrentMovePlayerId();
        if (currentPlayerId == bottomPlayerId) return 0;
        if (currentPlayerId == leftPlayerId) return 1;
        if (currentPlayerId == topPlayerId) return 2;
        if (currentPlayerId == rightPlayerId) return 3;
        return 0;
    }

    private int getNextPlayerIndex(int currentIndex) {
        List<Player> players = gameState.getOtherPlayers();
        players.add(gameState.getReceiverPlayer());
        int nextPlayerId = players.get((currentIndex + 1) % players.size()).id();
        if (nextPlayerId == bottomPlayerId) return 0;
        if (nextPlayerId == leftPlayerId) return 1;
        if (nextPlayerId == topPlayerId) return 2;
        if (nextPlayerId == rightPlayerId) return 3;
        return 0;
    }

}

