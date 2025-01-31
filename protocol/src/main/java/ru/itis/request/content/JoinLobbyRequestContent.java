package ru.itis.request.content;

import ru.itis.Content;

import java.nio.charset.StandardCharsets;

public class JoinLobbyRequestContent implements Content {

    private final int clientId;
    private final int lobbyId;
    private final String username;

    public JoinLobbyRequestContent(int clientId, int lobbyId, String username) {
        this.clientId = clientId;
        this.lobbyId = lobbyId;
        this.username = username;
    }

    public JoinLobbyRequestContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] parts = content.split("&");
        String clientId = parts[0].split("=")[1];
        String lobbyId = parts[1].split("=")[1];
        String username = parts[2].split("=")[1];
        this.clientId = Integer.parseInt(clientId);
        this.lobbyId = Integer.parseInt(lobbyId);
        this.username = username;
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "clientId=" + clientId + "&lobbyId=" + lobbyId + "&username=" + username;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

    public int getClientId() {
        return clientId;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public String getUsername() {
        return username;
    }
}
