package ru.itis.unogame.handler;

import ru.itis.request.Request;
import ru.itis.request.RequestType;

import java.net.Socket;

public interface RequestHandler {
    void handle(Socket socket, Request request);
    RequestType getType();
}
