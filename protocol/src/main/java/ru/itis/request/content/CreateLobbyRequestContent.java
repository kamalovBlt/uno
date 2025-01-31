package ru.itis.request.content;

import ru.itis.Content;

import java.nio.charset.StandardCharsets;

public class CreateLobbyRequestContent implements Content {

    private final int clientId;

    public CreateLobbyRequestContent(int id) {
        this.clientId = id;
    }

    public CreateLobbyRequestContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] parts = content.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid content format");
        }
        this.clientId = Integer.parseInt(parts[1]);
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "id=" + clientId;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

    public int getClientId() {
        return clientId;
    }
}
