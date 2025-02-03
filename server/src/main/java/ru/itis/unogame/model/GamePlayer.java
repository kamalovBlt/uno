package ru.itis.unogame.model;

import ru.itis.cards.Card;

import java.net.Socket;
import java.util.List;

public record GamePlayer(
        int id,
        String username,
        Socket socket,
        List<Card> cards
) { }
