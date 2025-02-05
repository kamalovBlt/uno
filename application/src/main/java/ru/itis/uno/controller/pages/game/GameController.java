package ru.itis.uno.controller.pages.game;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ru.itis.cards.Card;
import ru.itis.lobby.GameState;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CallUnoRequestContent;
import ru.itis.request.content.TakeCardFromDeckRequestContent;
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

    @FXML
    private AnchorPane gamePane;

    private ClientProtocolService clientProtocolService;
    private GameState gameState;

    @FXML
    private Text remainCards;

    @FXML
    private ImageView arrow;

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
    public void initialize() {
        Client client = Client.getInstance();
        this.clientProtocolService = client.getClientProtocolService();
    }

    public void handleUnoButton() {
        clientProtocolService.send(
                new Request(
                        RequestType.CALL_UNO,
                        new CallUnoRequestContent(Client.getInstance().getCurrentGameId(), Client.getInstance().getId())
                ),
                Client.getInstance().getSocket()
        );
    }

    public void initialRendering(GameStateResponseContent gameStateResponseContent) {

        this.gameState = gameStateResponseContent.getGameState();

        Player receiverPlayer = gameState.getReceiverPlayer();
        bottomPlayerText.setText(receiverPlayer.username());
        bottomPlayerId = receiverPlayer.id();

        List<Player> otherPlayers = gameState.getOtherPlayers();

        Player leftPlayer = otherPlayers.get(0);
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
                        }
                        if (runnableResponse.responseType().equals(ResponseType.ERROR)) {
                            ErrorResponseContent content = (ErrorResponseContent) runnableResponse.content();
                            System.out.println(content.getMessage());
                        }
                        if (runnableResponse.responseType().equals(ResponseType.GAME_IS_OVER)) {
                            Platform.runLater(() -> {
                                try {
                                    Scene scene = gamePane.getScene();
                                    gamePane.getChildren().clear();
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
                                    Parent root = fxmlLoader.load();
                                    scene.setRoot(root);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
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

    public void handleTakeCard() {
        clientProtocolService.send(
                new Request(
                        RequestType.TAKE_CARD_FROM_DECK,
                        new TakeCardFromDeckRequestContent(Client.getInstance().getCurrentGameId(), Client.getInstance().getId())
                ),
                Client.getInstance().getSocket()
        );
    }

    public void updateScene(GameState newGameState) {
        Platform.runLater(() -> {
            this.gameState = newGameState;

            remainCards.setText(String.valueOf(gameState.getNumberOfRemainingCards()));

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

            String currentMovePlayer;

            int currentMovePlayerId = gameState.getCurrentMovePlayerId();
            if (currentMovePlayerId == bottomPlayerId) {
                bottomPlayerText.setFill(Color.RED);
                currentMovePlayer = "bottom";
            } else if (currentMovePlayerId == topPlayerId) {
                topPlayerText.setFill(Color.RED);
                currentMovePlayer = "top";
            } else if (currentMovePlayerId == rightPlayerId) {
                rightPlayerText.setFill(Color.RED);
                currentMovePlayer = "right";
            } else {
                leftPlayerText.setFill(Color.RED);
                currentMovePlayer = "left";
            }

            if (lastMovePlayer == null) {
                angle = 360;
            } else {
                switch (lastMovePlayer) {
                    case "bottom":
                        angle = switch (currentMovePlayer) {
                            case "left" -> 360;
                            case "right" -> -360;
                            default -> angle;
                        };
                        break;
                    case "left":
                        angle = switch (currentMovePlayer) {
                            case "top" -> 360;
                            case "bottom" -> -360;
                            default -> angle;
                        };
                        break;
                    case "top":
                        angle = switch (currentMovePlayer) {
                            case "right" -> 360;
                            case "left" -> -360;
                            default -> angle;
                        };
                        break;
                    case "right":
                        angle = switch (currentMovePlayer) {
                            case "bottom" -> 360;
                            case "top" -> -360;
                            default -> angle;
                        };
                        break;
                }
            }

            lastMovePlayer = currentMovePlayer;

            if (currentRotateTransition == null) {
                currentRotateTransition = new RotateTransition(Duration.seconds(2), arrow);
                currentRotateTransition.setByAngle(angle);
                currentRotateTransition.setInterpolator(Interpolator.LINEAR);
                currentRotateTransition.setCycleCount(Animation.INDEFINITE);
                currentRotateTransition.play();
            } else {
                currentRotateTransition.stop();
                currentRotateTransition.setByAngle(angle);
                currentRotateTransition.play();
            }

        });
    }

    private RotateTransition currentRotateTransition;
    private String lastMovePlayer;
    private int angle = 360;

    private void resetPlayerTextColors() {
        bottomPlayerText.setFill(Color.BLACK);
        topPlayerText.setFill(Color.BLACK);
        leftPlayerText.setFill(Color.BLACK);
        rightPlayerText.setFill(Color.BLACK);
    }

}

