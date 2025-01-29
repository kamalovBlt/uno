package ru.itis.response;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum ResponseType {

    SUCCESS(1);

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

}
