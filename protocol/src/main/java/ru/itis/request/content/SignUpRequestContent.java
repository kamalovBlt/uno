package ru.itis.request.content;

import lombok.Getter;
import ru.itis.Content;

import java.nio.charset.StandardCharsets;

@Getter
public class SignUpRequestContent implements Content {

    private String username;
    private String password;

    public SignUpRequestContent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SignUpRequestContent(byte[] contentData) {
        String contentString = new String(contentData, StandardCharsets.UTF_8);
        String[] parts = contentString.split("&");
        for (String part : parts) {
            String[] keyValue = part.split("=");
            if (keyValue[0].equals("username")) {
                this.username = keyValue[1];
            } else if (keyValue[0].equals("password")) {
                this.password = keyValue[1];
            }
        }
    }

    @Override
    public byte[] toByteArray() {
        String contentString = "username=" + username + "&password=" + password;
        return contentString.getBytes(StandardCharsets.UTF_8);
    }

}
