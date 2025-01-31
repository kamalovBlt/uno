package ru.itis.request;

import ru.itis.Content;
import ru.itis.request.content.*;

public class RequestContentFactory {

    public static Content createRequestContent(RequestType requestType, byte[] data) {

        switch (requestType) {

            case SIGN_IN -> {
                return new SignInRequestContent(data);
            }
            case SIGN_UP -> {
                return new SignUpRequestContent(data);
            }
            case CREATE_LOBBY -> {
                return new CreateLobbyRequestContent(data);
            }
            case JOIN_LOBBY -> {
                return new JoinLobbyRequestContent(data);
            }
            case REFRESH_LOBBY -> {
                return new RefreshLobbyRequestContent(data);
            }

            default -> throw new IllegalArgumentException("Unsupported request type: " + requestType);

        }

    }
}
