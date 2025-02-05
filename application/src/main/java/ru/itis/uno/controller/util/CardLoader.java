package ru.itis.uno.controller.util;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.itis.cards.Card;
import ru.itis.cards.CardColor;
import ru.itis.cards.CardType;
import ru.itis.lobby.GameState;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CoverCardRequestContent;
import ru.itis.service.ClientProtocolService;
import ru.itis.uno.client.Client;

import java.util.Objects;
import java.util.Random;

public class CardLoader {

    public static void addCardToOpponentDeck(Card card, Pane pane, String path) {

        ImageView cardImageView = getCardImageViewFromPath(path);
        cardImageView.setId(card.type().toString() + card.color().toString() + card.value());
        pane.getChildren().add(cardImageView);

    }

    public static void addCardToClientDeck(Card card, ClientProtocolService clientProtocolService, GameState gameState, Pane pane) {
        ImageView cardImageView = getCardImageView(card);
        cardImageView.setId(card.type().toString() + card.color().toString() + card.value());

        cardImageView.setOnMouseClicked(a -> {

            CardColor cardColor = null;

            if (card.type().equals(CardType.COLOR_EDIT) || card.type().equals(CardType.PLUS4)) {
                Random random = new Random();
                cardColor = CardColor.values()[random.nextInt(4)];
            }
            if (cardColor != null) {
                clientProtocolService.send(new Request(
                                RequestType.COVER_CARD,
                                new CoverCardRequestContent(
                                        new Card(card.value(), card.type(), cardColor),
                                        gameState.getReceiverPlayer().id(),
                                        Client.getInstance().getCurrentGameId())),
                        Client.getInstance().getSocket()
                );
            }
            else {
                clientProtocolService.send(new Request(
                                RequestType.COVER_CARD,
                                new CoverCardRequestContent(
                                        card,
                                        gameState.getReceiverPlayer().id(),
                                        Client.getInstance().getCurrentGameId())),
                        Client.getInstance().getSocket()
                );
            }

        });

        cardImageView.setOnMouseEntered(a -> {
            cardImageView.setCursor(Cursor.HAND);
            cardImageView.setScaleX(1.2);
            cardImageView.setScaleY(1.2);
        });

        cardImageView.setOnMouseExited(a -> {
            cardImageView.setCursor(Cursor.DEFAULT);
            cardImageView.setScaleX(1.0);
            cardImageView.setScaleY(1.0);
        });

        pane.getChildren().add(cardImageView);
    }

    public static ImageView getCardImageView(Card card) {
        CardColor color = card.color();
        ImageView cardImageView = null;
        if (card.type().equals(CardType.NUMBER)) {
            cardImageView = getCardImageViewFromPath(color.toString().toLowerCase() + "/" + color.toString().toLowerCase() + card.value() + ".png");

        } else if (card.type().equals(CardType.BLOCK)) {
            cardImageView = getCardImageViewFromPath(color.toString().toLowerCase() + "/" + color.toString().toLowerCase() + "-block.png");
        } else if (card.type().equals(CardType.FLIP)) {
            cardImageView = getCardImageViewFromPath(color.toString().toLowerCase() + "/" + color.toString().toLowerCase() + "-flip.png");
        } else if (card.type().equals(CardType.PLUS2)) {
            cardImageView = getCardImageViewFromPath(color.toString().toLowerCase() + "/" + color.toString().toLowerCase() + "-plus-2.png");
        } else if (card.type().equals(CardType.COLOR_EDIT)) {
            cardImageView = getCardImageViewFromPath("any.png");
        } else if (card.type().equals(CardType.PLUS4)) {
            cardImageView = getCardImageViewFromPath("plus4.png");
        }
        return cardImageView;
    }

    public static ImageView getCardImageViewFromPath(String path) {

        Image image = new Image(Objects.requireNonNull(CardLoader.class.getResourceAsStream("/images/cards/" + path)));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        return imageView;

    }


}
