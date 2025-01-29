package ru.itis.response;

import ru.itis.Content;

public record Response(ResponseType responseType, Content content) {
}
