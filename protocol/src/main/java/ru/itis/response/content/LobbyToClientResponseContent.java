package ru.itis.response.content;

import lombok.Getter;
import ru.itis.Content;
import ru.itis.lobby.Player;

import java.util.ArrayList;
import java.util.List;

/* Хранит список игроков, в порядке по часовой стрелке, начиная от самого получателя включительно.
Т.е если отправляется клиенту с username user, то в списке будет сначала user, leftUser, topUser, rightUser
*/
@Getter
public class LobbyToClientResponseContent implements Content {

    private final List<Player> players;

    public LobbyToClientResponseContent(List<Player> players) {
        this.players = players;
    }

    public LobbyToClientResponseContent(byte[] data) {
        String content = new String(data);
        String[] playersData = content.split("\\|\\|");
        players = new ArrayList<>();
        for (String playerString : playersData) {
            players.add(parsePlayer(playerString));
        }
    }

    @Override
    public byte[] toByteArray() {
        String pattern = "id=%s&username=%s";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < players.size(); ++i) {
            Player player = players.get(i);
            output.append(pattern.formatted(player.id(), player.username()));
            if (i != players.size() - 1) {
                output.append("||");
            }
        }
        return output.toString().getBytes();
    }

    private Player parsePlayer(String playerData) {
        String[] parts = playerData.split("&");
        int playerId = Integer.parseInt(parts[0].split("=")[1]);
        String playerName = parts[1].split("=")[1];
        return new Player(playerId, playerName, null);
    }
}