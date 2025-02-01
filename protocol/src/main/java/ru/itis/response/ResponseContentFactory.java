package ru.itis.response;

import ru.itis.Content;
import ru.itis.response.content.*;

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
            case LOBBY_TO_CLIENT -> {
                return new LobbyToClientResponseContent(data);
            }
            case ERROR -> {
                return new ErrorResponseContent(data);
            }
            default -> throw new IllegalArgumentException("Invalid response type");
        }
    }
}
