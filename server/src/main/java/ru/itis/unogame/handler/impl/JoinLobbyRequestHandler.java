package ru.itis.unogame.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.JoinLobbyRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.ErrorResponseContent;
import ru.itis.response.content.LobbyToClientResponseContent;
import ru.itis.service.MessageService;
import ru.itis.unogame.handler.RequestHandler;
import ru.itis.unogame.model.Game;
import ru.itis.unogame.model.GamePlayer;
import ru.itis.unogame.service.GameService;
import ru.itis.unogame.util.LoopedList;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinLobbyRequestHandler implements RequestHandler {

    private final MessageService<Response, Request> serverProtocolService;
    private final GameService gameService;

    @Value("${game.count-of-players}")
    private int countOfPlayersInGame;

    @Override
    public void handle(Socket socket, Request request) {
        JoinLobbyRequestContent joinLobbyRequestContent = (JoinLobbyRequestContent) request.content();
        int lobbyId = joinLobbyRequestContent.getLobbyId();
        int playerId = joinLobbyRequestContent.getClientId();
        String username = joinLobbyRequestContent.getUsername();
        Optional<Game> optionalGame = gameService.findGameById(lobbyId);
        if (optionalGame.isEmpty()) {
            serverProtocolService.send(
                    new Response(
                            ResponseType.ERROR,
                            new ErrorResponseContent("Lobby with ID: " + lobbyId + " do not exist")
                    ),
                    socket
            );
            return;
        }
        Game game = optionalGame.get();
        if (game.getCountOfPlayers() == countOfPlayersInGame) {
            serverProtocolService.send(
                    new Response(
                            ResponseType.ERROR,
                            new ErrorResponseContent("Lobby already full, you can't join.")
                    ),
                    socket
            );
            return;
        }
        LoopedList<GamePlayer> players = game.getPlayers();
        players.add(new GamePlayer(playerId, username, socket, new ArrayList<>(), false));
        while (players.getCurrent().id() != playerId) {
            players.next();
        }
        List<GamePlayer> outputGamePlayers = new ArrayList<>();
        outputGamePlayers.add(players.getCurrent());
        players.next();
        while (players.getCurrent().id() != playerId) {
            outputGamePlayers.add(players.getCurrent());
            players.next();
        }
        if (game.getPlayers().size() == countOfPlayersInGame) {
            game.start();
        } else {
            outputGamePlayers.forEach((player) -> {
                List<Player> outputPlayers = new ArrayList<>();
                while (players.getCurrent().id() != player.id()) players.next();
                do {
                    GamePlayer currentPlayer = players.getCurrent();
                    outputPlayers.add(new Player(currentPlayer.id(), currentPlayer.username(), currentPlayer.cards()));
                    players.next();
                }
                while (players.getCurrent().id() != player.id());
                serverProtocolService.send(
                        new Response(
                                ResponseType.LOBBY_TO_CLIENT,
                                new LobbyToClientResponseContent(outputPlayers)
                        ),
                        player.socket()
                );
            });
        }
    }

    @Override
    public RequestType getType() {
        return RequestType.JOIN_LOBBY;
    }
}
