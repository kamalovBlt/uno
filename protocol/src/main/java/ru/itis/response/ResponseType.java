package ru.itis.response;

import java.util.Optional;

public enum ResponseType {

    SUCCESS(1),
    SIGN_IN(2);

    private final int index;


    ResponseType(int index) {
        this.index = index;
    }

    public static Optional<ResponseType> fromIndex(int index) {
        for (ResponseType type : ResponseType.values()) {
            if (type.index == index) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public int getIndex() {
        return index;
    }

}
