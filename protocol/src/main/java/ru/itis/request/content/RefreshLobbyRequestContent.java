package ru.itis.request.content;

import ru.itis.Content;

import java.nio.charset.StandardCharsets;

public class RefreshLobbyRequestContent implements Content {

    private final int clientId;
    private final int lobbyId;

    public RefreshLobbyRequestContent(int clientId, int lobbyId) {
        this.clientId = clientId;
        this.lobbyId = lobbyId;
    }

    public RefreshLobbyRequestContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] parts = content.split("&");
        String clientId = parts[0].split("=")[1];
        String lobbyId = parts[1].split("=")[1];
        this.clientId = Integer.parseInt(clientId);
        this.lobbyId = Integer.parseInt(lobbyId);
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "clientId=" + clientId + "&lobbyId=" + lobbyId;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

    public int getClientId() {
        return clientId;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}
