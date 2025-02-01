package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.handler.RequestHandler;

import java.net.Socket;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnknownRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;

    @Override
    public void handle(Socket socket, Request request) {
        serverProtocolService.send(
                new Response(
                        ResponseType.ERROR,
                        new ErrorResponseContent("Unknown request type")
                ),
                socket
        );
    }

    @Override
    public RequestType getType() {
        return RequestType.UNKNOWN;
    }
}
