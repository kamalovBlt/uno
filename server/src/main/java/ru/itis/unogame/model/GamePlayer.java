package ru.itis.unogame.model;

import ru.itis.cards.Card;

import java.net.Socket;
import java.util.List;
import java.util.Objects;

public final class GamePlayer {
    private final int id;
    private final String username;
    private final Socket socket;
    private final List<Card> cards;
    private boolean saysUno;

    public GamePlayer(
            int id,
            String username,
            Socket socket,
            List<Card> cards,
            boolean saysUno
    ) {
        this.id = id;
        this.username = username;
        this.socket = socket;
        this.cards = cards;
        this.saysUno = saysUno;
    }

    public int id() {
        return id;
    }

    public String username() {
        return username;
    }

    public Socket socket() {
        return socket;
    }

    public List<Card> cards() {
        return cards;
    }

    public boolean saysUno() {
        return saysUno;
    }

    public void setSaysUno(boolean value) {
        saysUno = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (GamePlayer) obj;
        return this.id == that.id &&
                Objects.equals(this.username, that.username) &&
                Objects.equals(this.socket, that.socket) &&
                Objects.equals(this.cards, that.cards) &&
                this.saysUno == that.saysUno;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, socket, cards, saysUno);
    }

    @Override
    public String toString() {
        return "GamePlayer[" +
                "id=" + id + ", " +
                "username=" + username + ", " +
                "socket=" + socket + ", " +
                "cards=" + cards + ", " +
                "saysUno=" + saysUno + ']';
    }
}
