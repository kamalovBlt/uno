package ru.itis.response.content;

import lombok.Getter;
import ru.itis.Content;

import java.nio.charset.StandardCharsets;

@Getter
public class SignUpResponseContent implements Content {

    private int id;

    public SignUpResponseContent(int id) {
        this.id = id;
    }

    public SignUpResponseContent(byte[] contentData) {
        String contentString = new String(contentData, StandardCharsets.UTF_8);
        String[] parts = contentString.split("&");
        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue[0].equals("id")) {
                this.id = Integer.parseInt(keyValue[1]);
            }
        }
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "id=" + id;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

}
