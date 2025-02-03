package ru.itis.unogame.model;

import lombok.Getter;
import ru.itis.cards.Card;
import ru.itis.cards.CardColor;
import ru.itis.cards.CardType;
import ru.itis.lobby.GameState;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.GameStateResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.util.LoopedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Game {

    private final ReentrantLock lock = new ReentrantLock(true);
    private final int id;
    private final LoopedList<GamePlayer> players;
    private final Deque<Card> deck;
    private final MessageService<Response, Request> serverProtocolService;
    private Card currentCard;
    private int idLastPlayerSayUno;

    public Game(int id, LoopedList<GamePlayer> players, Deque<Card> deck, MessageService<Response, Request> serverProtocolService) {
        this.id = id;
        this.players = players;
        this.deck = deck;
        this.serverProtocolService = serverProtocolService;
        fillDeck();
    }

    public int getCountOfPlayers() {
        return players.size();
    }

    private void dealCards() {
        for (int i = 0; i < 7; ++i) {
            for (int j = 0; j < players.size(); ++j) {
                Card card = deck.pop();
                GamePlayer gamePlayer = players.getCurrent();
                gamePlayer.cards().add(card);
                players.next();
            }
        }
        do {
            currentCard = deck.pop();
        } while (currentCard.color() == CardColor.ANY);
    }

    private void fillDeck() {
        List<Card> cards = new ArrayList<>(108);
        for (int j = 0; j < 2; ++j) {
            for (int i = 1; i < 10; ++i) {
                addCardAllColor(i, CardType.NUMBER, cards);
            }
            addCardAllColor(-1, CardType.BLOCK, cards);
            addCardAllColor(-1, CardType.PLUS2, cards);
            addCardAllColor(-1, CardType.FLIP, cards);
        }
        addCardAllColor(0, CardType.NUMBER, cards);
        for (int i = 0; i < 4; ++i) {
            cards.add(new Card(-1, CardType.COLOR_EDIT, CardColor.ANY));
            cards.add(new Card(-1, CardType.PLUS4, CardColor.ANY));
        }
        Collections.shuffle(cards);
        deck.addAll(cards);
    }

    private void addCardAllColor(int value, CardType cardType, List<Card> cards) {
        cards.add(new Card(value, cardType, CardColor.GREEN));
        cards.add(new Card(value, cardType, CardColor.BLUE));
        cards.add(new Card(value, cardType, CardColor.RED));
        cards.add(new Card(value, cardType, CardColor.YELLOW));
    }

    public void start() {
        dealCards();
        notifyAllPlayers();
    }

    private void notifyAllPlayers() {
        int currentMovePlayerId = players.getCurrent().id();
        for (int i = 0; i < players.size(); ++i) {
            GamePlayer receiverPlayer = players.getCurrent();
            Player player = new Player(receiverPlayer.id(), receiverPlayer.username(), receiverPlayer.cards());
            players.next();
            List<GamePlayer> otherPlayers = new ArrayList<>();
            while (receiverPlayer.id() != players.getCurrent().id()) {
                otherPlayers.add(players.getCurrent());
                players.next();
            }
            if (players.isReversed()) {
                List<GamePlayer> reversedPlayers = new ArrayList<>();
                for (int j = otherPlayers.size() - 1; j >= 0; --j) {
                    reversedPlayers.add(otherPlayers.get(j));
                }
                otherPlayers = reversedPlayers;
            }
            List<Player> outputPlayers = otherPlayers.stream()
                    .map(pl -> new Player(pl.id(), pl.username(), pl.cards()))
                    .toList();
            GameState gameState = new GameState(currentCard, player, outputPlayers, deck.size(), currentMovePlayerId);
            serverProtocolService.send(new Response(
                    ResponseType.GAME_STATE,
                    new GameStateResponseContent(gameState)),
                    receiverPlayer.socket());
            players.next();
        }
    }

    public void coverCard(int playerId, Card card) {
        lock.lock();
        try {
            if (players.getCurrent().id() != playerId) {
                return;
            }
            if (card.type() == CardType.PLUS4) {
                currentCard = card;
                players.next();
                for (int i = 0; i < Math.min(deck.size(), 4); ++i) {
                    players.getCurrent().cards().add(deck.pop());
                }
            }
            if (card.type() == CardType.COLOR_EDIT) {
                currentCard = card;
                players.next();
            }
            if (card.type() == CardType.NUMBER) {
                if (card.color() == currentCard.color() || card.value() == currentCard.value()) {
                    currentCard = card;
                    players.next();
                }
            }
            if (card.color() == currentCard.color() || card.type() == currentCard.type()) {
                if (card.type() == CardType.FLIP) {
                    currentCard = card;
                    players.reverse();
                    players.next();
                }
                if (card.type() == CardType.BLOCK) {
                    currentCard = card;
                    players.next();
                    players.next();
                }
                if (card.type() == CardType.PLUS2) {
                    currentCard = card;
                    players.next();
                    for (int i = 0; i < Math.min(deck.size(), 2); ++i) {
                        players.getCurrent().cards().add(deck.pop());
                    }
                }
            }
            notifyAllPlayers();
        } finally {
            lock.unlock();
        }
    }

    public void takeCardFromDeck(int playerId) {
        lock.lock();
        try {
            if (players.getCurrent().id() != playerId) {
                return;
            }
            if (!deck.isEmpty()) {
                players.getCurrent().cards().add(deck.pop());
            }
            notifyAllPlayers();
        } finally {
            lock.unlock();
        }
    }

    public void callUno(int playerId) {
        lock.lock();
        try {
            if (players.getCurrent().id() == playerId) {
                idLastPlayerSayUno = playerId;
                return;
            }
            if (players.getCurrent().id() != idLastPlayerSayUno && players.getCurrent().cards().size() == 1) {
                for (int i = 0; i < Math.min(2, deck.size()); ++i) {
                    players.getCurrent().cards().add(deck.pop());
                }
                notifyAllPlayers();
            }
        } finally {
            lock.unlock();
        }
    }
}
