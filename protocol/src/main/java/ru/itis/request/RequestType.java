package ru.itis.request;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum RequestType {

    SIGN_IN(1),
    SIGN_UP(2),
    JOIN_LOBBY(3),
    CREATE_LOBBY(4),
    UNKNOWN(5),
    COVER_CARD(6),
    TAKE_CARD_FROM_DECK(7),
    CALL_UNO(8);

    private final int index;

    RequestType(int index) {
        this.index = index;
    }

    public static Optional<RequestType> fromIndex(int index) {
        for (RequestType type : RequestType.values()) {
            if (type.index == index) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

}
