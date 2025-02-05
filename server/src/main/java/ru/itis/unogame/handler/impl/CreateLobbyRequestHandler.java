package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.service.MessageService;

import ru.itis.unogame.handler.RequestHandler;
import ru.itis.unogame.model.Game;
import ru.itis.unogame.model.GamePlayer;
import ru.itis.unogame.service.GameService;

import java.net.Socket;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateLobbyRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;
    private final GameService gameService;

    @Override
    public void handle(Socket socket, Request request) {
        CreateLobbyRequestContent createLobbyRequestContent = (CreateLobbyRequestContent) request.content();
        log.info("Creating lobby for user: {}", createLobbyRequestContent.getClientId());
        int clientId = createLobbyRequestContent.getClientId();
        String username = createLobbyRequestContent.getUsername();
        GamePlayer gamePlayer = new GamePlayer(clientId, username, socket, new ArrayList<>(), false);
        Game game = gameService.createGame(gamePlayer);
        serverProtocolService.send(
                new Response(
                        ResponseType.LOBBY_ID,
                        new LobbyIdResponseContent(game.getId())
                ),
                socket
        );
    }

    @Override
    public RequestType getType() {
        return RequestType.CREATE_LOBBY;
    }
}
