package ru.itis.response.content;

import ru.itis.Content;

import java.nio.charset.StandardCharsets;

public class GameIsOverResponseContent implements Content {

    private static final String PATTERN = "username=%s";

    private final String winnerUsername;

    public GameIsOverResponseContent(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public GameIsOverResponseContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        winnerUsername = content.split("=")[1];
    }

    @Override
    public byte[] toByteArray() {
        return PATTERN.formatted(winnerUsername).getBytes(StandardCharsets.UTF_8);
    }
}
