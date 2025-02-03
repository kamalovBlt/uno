package ru.itis.lobby;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.itis.cards.Card;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GameState {

    private Card currentCard;
    private Player receiverPlayer;
    private List<Player> otherPlayers;
    private int numberOfRemainingCards;
    private int currentMovePlayerId;
}
