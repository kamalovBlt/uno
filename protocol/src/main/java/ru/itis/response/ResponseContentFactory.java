package ru.itis.response;

import ru.itis.Content;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.response.content.SignInResponseContent;
import ru.itis.response.content.SignUpResponseContent;
import ru.itis.response.content.SuccessContent;

public class ResponseContentFactory {
    public static Content createResponseContent(ResponseType responseType, byte[] data) {
        switch (responseType) {
            case SUCCESS -> {
                return new SuccessContent();
            }
            case SIGN_IN -> {
                return new SignInResponseContent(data);
            }
            case SIGN_UP -> {
                return new SignUpResponseContent(data);
            }
            case LOBBY_ID -> {
                return new LobbyIdResponseContent(data);
            }
            default -> throw new IllegalArgumentException("Invalid response type");
        }
    }
}
