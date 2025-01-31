package ru.itis.unogame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.itis.lobby.Player;


/**
 * <p>Игрок1 находится слева</p>
 * <p>Игрок2 находится сверху</p>
 * <p>Игрок3 находится справа</p>
 * <p>Игрок4 находится снизу</p>
 * <p>Т.е. 1 -> 2 -> 3 -> 4 -> 1</p>*/
@Getter
@Setter
@RequiredArgsConstructor
public class GameLobby {

    private final int id;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    public int addPlayer(Player player) {
        if (player1 == null) {
            player1 = player;
            return 1;
        }
        if (player2 == null) {
            player2 = player;
            return 2;
        }
        if (player3 == null) {
            player3 = player;
            return 3;
        }
        if (player4 == null) {
            player4 = player;
            return 4;
        }
        return 0;
    }

    public boolean isFull() {
        return player1 != null && player2 != null && player3 != null && player4 != null;
    }

    public int getPosition(int id) {
        if (id == player1.id()) {
            return 1;
        }
        if (id == player2.id()) {
            return 2;
        }
        if (id == player3.id()) {
            return 3;
        }
        if (id == player4.id()) {
            return 4;
        }
        return 0;
    }

}
