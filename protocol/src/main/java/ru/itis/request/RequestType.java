package ru.itis.request;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum RequestType {

    SIGN_IN(1),
    SIGN_UP(2),
    JOIN_LOBBY(3),
    CREATE_LOBBY(4),
    REFRESH_LOBBY(5);

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

    public int getIndex() {
        return index;
    }

}
