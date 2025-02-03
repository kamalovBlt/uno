package ru.itis.request.content;

import lombok.Getter;
import ru.itis.Content;
import ru.itis.cards.Card;
import ru.itis.cards.CardColor;
import ru.itis.cards.CardType;

import java.nio.charset.StandardCharsets;

@Getter
public class CoverCardRequestContent implements Content {

    private static final String CARD_PATTERN = "cardValue=%s" +
            "&cardType=%s" +
            "&cardColor=%s";

    private final Card card;
    private final int playerId;
    private final int gameId;


    public CoverCardRequestContent(Card card, int playerId, int gameId) {
        this.card = card;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public CoverCardRequestContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] attributes = content.split("&");
        gameId = Integer.parseInt(attributes[0].split("=")[1]);
        playerId = Integer.parseInt(attributes[1].split("=")[1]);
        int cardValue = Integer.parseInt(attributes[2].split("=")[1]);
        CardType cardType = CardType.valueOf(attributes[3].split("=")[1]);
        CardColor cardColor = CardColor.valueOf(attributes[4].split("=")[1]);
        card = new Card(cardValue, cardType, cardColor);
    }

    @Override
    public byte[] toByteArray() {
        StringBuilder builder = new StringBuilder();
        builder.append("gameId=%s".formatted(gameId));
        builder.append("&");
        builder.append("playerId=%s".formatted(playerId));
        builder.append("&");
        builder.append(CARD_PATTERN.formatted(card.value(), card.type(), card.color()));
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
