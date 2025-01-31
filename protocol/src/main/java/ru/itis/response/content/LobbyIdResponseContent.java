package ru.itis.response.content;

import ru.itis.Content;

import java.nio.charset.StandardCharsets;

public class LobbyIdResponseContent implements Content {

    private final int id;

    public LobbyIdResponseContent(int id) {
        this.id = id;
    }

    public LobbyIdResponseContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] parts = content.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid content format");
        }
        this.id = Integer.parseInt(parts[1]);
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "id=" + id;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

    public int getId() {
        return id;
    }
}
