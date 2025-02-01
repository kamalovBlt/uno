package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.GameLobbies;
import ru.itis.unogame.GameLobby;
import ru.itis.unogame.handler.RequestHandler;

import java.net.Socket;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateLobbyRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;

    @Override
    public void handle(Socket socket, Request request) {
        CreateLobbyRequestContent createLobbyRequestContent = (CreateLobbyRequestContent) request.content();
        log.info("Creating lobby for user: {}", createLobbyRequestContent.getClientId());
        int clientId = createLobbyRequestContent.getClientId();
        String username = createLobbyRequestContent.getUsername();
        Player creator = new Player(clientId, username);

        GameLobby gameLobby = new GameLobby(75);
        gameLobby.setPlayer1(creator);
        GameLobbies.addGameLobby(gameLobby);

        serverProtocolService.send(
                new Response(
                        ResponseType.LOBBY_ID,
                        new LobbyIdResponseContent(75)
                ),
                socket
        );
    }

    @Override
    public RequestType getType() {
        return RequestType.CREATE_LOBBY;
    }
}
