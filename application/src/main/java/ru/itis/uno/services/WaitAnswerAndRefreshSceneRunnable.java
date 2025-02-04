package ru.itis.uno.services;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ru.itis.cards.Card;
import ru.itis.lobby.GameState;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;
import ru.itis.uno.controller.util.CardLoader;

import java.util.Optional;

public record WaitAnswerAndRefreshSceneRunnable(ClientProtocolService clientProtocolService, Pane gamePane) implements Runnable {
    @Override
    public void run() {
        Optional<Response> optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
        while (optionalRunnableResponse.isPresent()) {
            Response runnableResponse = optionalRunnableResponse.get();
            if (runnableResponse.responseType().equals(ResponseType.GAME_STATE)) {

                System.out.println("Обрабатываю");

                GameStateResponseContent gameStateResponseContent = (GameStateResponseContent) runnableResponse.content();
                GameState gameState = gameStateResponseContent.getGameState();

                Card currentCard = gameState.getCurrentCard();
                VBox centerVBox = (VBox) gamePane.lookup("#centerVBox");
                centerVBox.getChildren().clear();
                centerVBox.getChildren().add(CardLoader.getCardImageView(currentCard));
                System.out.println("Отправил");
            }
            optionalRunnableResponse = clientProtocolService.read(Client.getInstance().getSocket());
        }
    }
}
