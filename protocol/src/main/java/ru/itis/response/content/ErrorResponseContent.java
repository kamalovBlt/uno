package ru.itis.response.content;

import ru.itis.Content;

public class ErrorResponseContent implements Content {
    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
