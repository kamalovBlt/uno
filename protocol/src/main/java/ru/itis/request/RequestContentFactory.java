package ru.itis.request;

import ru.itis.Content;
import ru.itis.request.content.SignInRequestContent;

public class RequestContentFactory {

    public static Content createRequestContent(RequestType requestType, byte[] data) {

        switch (requestType) {
            case SIGN_IN -> {
                return new SignInRequestContent(data);
            }
            case SIGN_UP -> {
                return null;
            }

            default -> throw new IllegalArgumentException("Unsupported request type: " + requestType);

        }

    }
}
