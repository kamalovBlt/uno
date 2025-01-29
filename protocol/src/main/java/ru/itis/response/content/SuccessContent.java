package ru.itis.response.content;

import ru.itis.Content;

public class SuccessContent implements Content {
    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }
}
