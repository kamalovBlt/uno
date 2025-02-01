package ru.itis.response.content;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.itis.Content;

import java.nio.charset.StandardCharsets;

@Getter
@RequiredArgsConstructor
public class ErrorResponseContent implements Content {

    private final String message;

    public ErrorResponseContent(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        String[] parts = content.split("=");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid content format");
        }
        this.message = parts[1];
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "message=" + message;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }
}
