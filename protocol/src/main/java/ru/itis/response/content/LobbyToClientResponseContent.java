package ru.itis.response.content;

import ru.itis.Content;
import ru.itis.lobby.Lobby;
import ru.itis.lobby.Player;

public class LobbyToClientResponseContent implements Content {

    private final Lobby lobby;

    public LobbyToClientResponseContent(Lobby lobby) {
        this.lobby = lobby;
    }

    public LobbyToClientResponseContent(byte[] data) {

        String content = new String(data);
        String[] playersData = content.split("\\|\\|");

        if (playersData.length != 3) {
            throw new IllegalArgumentException("Invalid data format");
        }

        Player leftPlayer = parsePlayer(playersData[0]);
        Player topPlayer = parsePlayer(playersData[1]);
        Player rightPlayer = parsePlayer(playersData[2]);

        this.lobby = new Lobby(leftPlayer, topPlayer, rightPlayer);
    }

    @Override
    public byte[] toByteArray() {
        Player leftPlayer = lobby.leftPlayer();
        Player topPlayer = lobby.topPlayer();
        Player rightPlayer = lobby.rightPlayer();

        String content = "player1:id=%s&username=%s||player2:id=%s&username=%s||player3:id=%s&username=%s".formatted(
                leftPlayer.id(), leftPlayer.username(),
                topPlayer.id(), topPlayer.username(),
                rightPlayer.id(), rightPlayer.username()
        );

        return content.getBytes();
    }

    private Player parsePlayer(String playerData) {
        String[] parts = playerData.split(":id=|&username=");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid player data format");
        }
        int playerId = Integer.parseInt(parts[1]);
        String playerName = parts[2];

        return new Player(playerId, playerName);
    }

    public Lobby getLobby() {
        return lobby;
    }

}