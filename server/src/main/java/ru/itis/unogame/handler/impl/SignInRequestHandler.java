package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.SignInRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.SignInResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.exception.LoginException;
import ru.itis.unogame.handler.RequestHandler;
import ru.itis.unogame.service.AuthService;

import java.net.Socket;

@Component
@RequiredArgsConstructor
public class SignInRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;
    private final AuthService authService;

    @Override
    public void handle(Socket socket, Request request) {
        SignInRequestContent signInRequestContent = (SignInRequestContent) request.content();
        try {
            int id = authService.login(signInRequestContent.getUsername(), signInRequestContent.getPassword());
            serverProtocolService.send(
                    new Response(
                            ResponseType.SIGN_IN,
                            new SignInResponseContent(id)
                    ),
                    socket
            );
        } catch (LoginException e) {
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
        return RequestType.SIGN_IN;
    }
}
