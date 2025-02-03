package ru.itis.response.content;

import lombok.Getter;
import ru.itis.Content;
import ru.itis.cards.Card;
import ru.itis.cards.CardColor;
import ru.itis.cards.CardType;
import ru.itis.lobby.GameState;
import ru.itis.lobby.Player;

import java.util.ArrayList;
import java.util.List;

@Getter

public class GameStateResponseContent implements Content {

    private static final String COMMON_INFO_PATTERN = "currentMovePlayerId=%s" +
            "&numberOfRemainingCards=%s";
    private static final String CARD_PATTERN = "cardValue=%s" +
            "&cardType=%s" +
            "&cardColor=%s";
    private static final String PLAYER_PATTERN = "playerId=%s" +
            "&playerUsername=%s" +
            "&playerCardsSize=%s";

    private final GameState gameState;
    private int index = 0;

    public GameStateResponseContent(GameState gameState) {
        this.gameState = gameState;
    }

    public GameStateResponseContent(byte[] data) {
        String content = new String(data);
        String[] gameStateData = content.split("&");
        int currentMovePlayerId = Integer.parseInt(gameStateData[index++].split("=")[1]);
        int numberOfRemainingCards = Integer.parseInt(gameStateData[index++].split("=")[1]);
        Card currentCard = deserializeCard(gameStateData);
        Player recieverPlayer = deserializePlayer(gameStateData);
        int countOfOtherPlayers = Integer.parseInt(gameStateData[index++].split("=")[1]);
        List<Player> otherPlayers = new ArrayList<>(countOfOtherPlayers);
        for (int i = 0; i < countOfOtherPlayers; ++i) {
            otherPlayers.add(deserializePlayer(gameStateData));
        }
        gameState = new GameState(currentCard, recieverPlayer, otherPlayers, numberOfRemainingCards, currentMovePlayerId);
    }

    @Override
    public byte[] toByteArray() {
        StringBuilder output = new StringBuilder();
        output.append(COMMON_INFO_PATTERN.formatted(gameState.getCurrentMovePlayerId(), gameState.getNumberOfRemainingCards()));
        output.append("&");
        output.append(serializeCard(gameState.getCurrentCard()));
        output.append("&");
        output.append(serializePlayer(gameState.getReceiverPlayer()));
        output.append("&");
        int playersSize = gameState.getOtherPlayers() == null ? 0 : gameState.getOtherPlayers().size();
        output.append("countOfPlayers=%s".formatted(playersSize));
        for (int i = 0; i < playersSize; ++i) {
            output.append("&");
            output.append(serializePlayer(gameState.getOtherPlayers().get(i)));
        }
        return output.toString().getBytes();
    }

    private Card deserializeCard(String[] data) {
        int cardValue = Integer.parseInt(data[index++].split("=")[1]);
        CardType cardType = CardType.valueOf(data[index++].split("=")[1]);
        CardColor cardColor = CardColor.valueOf(data[index++].split("=")[1]);
        return new Card(cardValue, cardType, cardColor);
    }

    private Player deserializePlayer(String[] data) {
        int playerId = Integer.parseInt(data[index++].split("=")[1]);
        String username = data[index++].split("=")[1];
        int playerCardSize = Integer.parseInt(data[index++].split("=")[1]);
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < playerCardSize; ++i) {
            cards.add(deserializeCard(data));
        }
        return new Player(playerId, username, cards);
    }

    private String serializeCard(Card card) {
        return CARD_PATTERN.formatted(card.value(), card.type(), card.color());
    }

    private String serializePlayer(Player player) {
        StringBuilder builder = new StringBuilder();
        int cardSize = player.cards() == null ? 0 : player.cards().size();
        builder.append(PLAYER_PATTERN.formatted(player.id(), player.username(), cardSize));
        for (int i = 0; i < cardSize; ++i) {
            builder.append("&").append(serializeCard(player.cards().get(i)));
        }
        return builder.toString();
    }
}
