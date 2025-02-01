package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.lobby.Lobby;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.RefreshLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.GameLobbies;
import ru.itis.unogame.GameLobby;
import ru.itis.unogame.handler.RequestHandler;

import java.net.Socket;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshLobbyRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;

    @Override
    public void handle(Socket socket, Request request) {
        RefreshLobbyRequestContent joinLobbyRequestContent = (RefreshLobbyRequestContent) request.content();
        int lobbyId = joinLobbyRequestContent.getLobbyId();
        GameLobby gameLobby = GameLobbies.getGameLobby(lobbyId);
        if (gameLobby != null) {

            if (gameLobby.isFull()) {
                serverProtocolService.send(
                        new Response(
                                ResponseType.ERROR,
                                new ErrorResponseContent("Lobby already full, you can't join.")
                        ),
                        socket
                );
            }

            int position = gameLobby.getPosition(joinLobbyRequestContent.getClientId());
            Lobby lobby = null;

            if (position == 1) {
                lobby = new Lobby(
                        gameLobby.getPlayer2(),
                        gameLobby.getPlayer3(),
                        gameLobby.getPlayer4()
                );
            }
            if (position == 2) {
                lobby = new Lobby(
                        gameLobby.getPlayer3(),
                        gameLobby.getPlayer4(),
                        gameLobby.getPlayer1()
                );
            }
            if (position == 3) {
                lobby = new Lobby(
                        gameLobby.getPlayer4(),
                        gameLobby.getPlayer1(),
                        gameLobby.getPlayer2()
                );
            }
            if (position == 4) {
                lobby = new Lobby(
                        gameLobby.getPlayer1(),
                        gameLobby.getPlayer2(),
                        gameLobby.getPlayer3()
                );
            }
            serverProtocolService.send(
                    new Response(
                            ResponseType.LOBBY_TO_CLIENT,
                            new LobbyToClientResponseContent(
                                    lobby
                            )
                    ),
                    socket
            );
        }
    }

    @Override
    public RequestType getType() {
        return RequestType.REFRESH_LOBBY;
    }
}
