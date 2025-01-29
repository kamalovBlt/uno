package ru.itis.request;

import ru.itis.Content;

public record Request(RequestType requestType, Content content) {
}
