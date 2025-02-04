package ru.itis.lobby;

import ru.itis.cards.Card;

import java.util.List;

public record Player(int id, String username, List<Card> cards) {
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", cards=" + cards +
                '}';
    }
}
