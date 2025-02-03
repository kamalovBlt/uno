package ru.itis.request.content;

import lombok.Getter;
import ru.itis.Content;

import java.nio.charset.StandardCharsets;

@Getter
public class CallUnoRequestContent implements Content {

    private static final String CONTENT_PATTERN = "gameId=%s&playerId=%s";

    private final int gameId;
    private final int playerId;

    public CallUnoRequestContent(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public CallUnoRequestContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] attributes = content.split("&");
        gameId = Integer.parseInt(attributes[0].split("=")[1]);
        playerId = Integer.parseInt(attributes[1].split("=")[1]);
    }

    @Override
    public byte[] toByteArray() {
        return CONTENT_PATTERN.formatted(gameId, playerId).getBytes(StandardCharsets.UTF_8);
    }

}
