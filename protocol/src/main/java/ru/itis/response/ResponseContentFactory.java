package ru.itis.response;

import ru.itis.Content;
import ru.itis.response.content.SuccessContent;

public class ResponseContentFactory {
    public static Content createResponseContent(ResponseType responseType, byte[] data) {
        switch (responseType) {
            case SUCCESS -> {
                return new SuccessContent();
            }
            default -> throw new IllegalArgumentException("Invalid response type");
        }
    }
}
