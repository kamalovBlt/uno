package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.SignUpRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.SignUpResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.exception.RegisterException;
import ru.itis.unogame.handler.RequestHandler;
import ru.itis.unogame.service.AuthService;

import java.net.Socket;

@Component
@RequiredArgsConstructor
public class SignUpRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;
    private final AuthService authService;

    @Override
    public void handle(Socket socket, Request request) {
        SignUpRequestContent signUpRequestContent = (SignUpRequestContent) request.content();
        try {
            int id = authService.register(signUpRequestContent.getUsername(), signUpRequestContent.getPassword());
            serverProtocolService.send(
                    new Response(
                            ResponseType.SIGN_UP,
                            new SignUpResponseContent(id)
                    ),
                    socket
            );
        } catch (RegisterException e) {
            serverProtocolService.send(
                    new Response(
                            ResponseType.ERROR,
                            new ErrorResponseContent(e.getMessage())

                    ),
                    socket
            );
        }

    }

    @Override
    public RequestType getType() {
        return RequestType.SIGN_UP;
    }
}
