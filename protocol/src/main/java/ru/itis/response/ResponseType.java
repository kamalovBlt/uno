package ru.itis.response;

import java.util.Optional;

public enum ResponseType {

    SUCCESS(1),
    SIGN_IN(2),
    SIGN_UP(3),
    LOBBY_TO_CLIENT(4),
    LOBBY_ID(5),
    ERROR(6),
    GAME_STATE(7),
    GAME_IS_OVER(8);

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
