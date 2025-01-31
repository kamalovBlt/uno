package ru.itis.unogame;

import ru.itis.lobby.Lobby;
import ru.itis.lobby.Player;
import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.*;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.*;
import ru.itis.service.ServerProtocolService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Optional;
import java.util.Random;

public class Server {
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(11111)) {
            System.out.println("Server started on port 11111");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                ServerProtocolService serverProtocolService = new ServerProtocolService();
                Optional<Request> read;
                Random random = new Random();
                while ((read = serverProtocolService.read(clientSocket)).isPresent()) {
                    Request request = read.get();
                    if (request.requestType().equals(RequestType.SIGN_IN)) {
                        SignInRequestContent signInRequestContent = (SignInRequestContent) request.content();
                        System.out.println("Sign in attempt: " + signInRequestContent.getUsername());
                        System.out.println("Password: " + signInRequestContent.getPassword());
                        serverProtocolService.send(
                                new Response(
                                        ResponseType.SIGN_IN,
                                        new SignInResponseContent(random.nextInt(1, 100))
                                ),
                                clientSocket
                        );
                    } else if (request.requestType().equals(RequestType.SIGN_UP)) {
                        SignUpRequestContent signUpRequestContent = (SignUpRequestContent) request.content();
                        System.out.println("Sign up attempt: " + signUpRequestContent.getUsername());
                        System.out.println("Password: " + signUpRequestContent.getPassword());
                        serverProtocolService.send(
                                new Response(
                                        ResponseType.SIGN_UP,
                                        new SignUpResponseContent(random.nextInt(0, 100))
                                ),
                                clientSocket
                        );
                    } else if (request.requestType().equals(RequestType.CREATE_LOBBY)) {
                        CreateLobbyRequestContent createLobbyRequestContent = (CreateLobbyRequestContent) request.content();
                        System.out.println("Creating lobby for user: " + createLobbyRequestContent.getClientId());
                        int clientId = createLobbyRequestContent.getClientId();
                        String username = createLobbyRequestContent.getUsername();
                        Player creator = new Player(clientId, username);
                        System.out.println(username);

                        GameLobby gameLobby = new GameLobby(75);
                        gameLobby.setPlayer1(creator);
                        GameLobbies.addGameLobby(gameLobby);

                        serverProtocolService.send(
                                new Response(
                                        ResponseType.LOBBY_ID,
                                        new LobbyIdResponseContent(75)
                                ),
                                clientSocket
                        );
                    } else if (request.requestType().equals(RequestType.JOIN_LOBBY)) {
                        JoinLobbyRequestContent joinLobbyRequestContent = (JoinLobbyRequestContent) request.content();
                        int lobbyId = joinLobbyRequestContent.getLobbyId();
                        GameLobby gameLobby = GameLobbies.getGameLobby(lobbyId);
                        if (gameLobby != null) {

                            if (gameLobby.isFull()) {
                                serverProtocolService.send(
                                        new Response(
                                                ResponseType.ERROR,
                                                new ErrorResponseContent()
                                        ),
                                        clientSocket
                                );
                            }

                            int position = gameLobby.addPlayer(new Player(
                                    joinLobbyRequestContent.getClientId(), joinLobbyRequestContent.getUsername()
                            ));
                            Lobby lobby = null;
                            /*TODO: внимательнее к порядку*/
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
                                    clientSocket
                            );
                        } else {
                            serverProtocolService.send(
                                    new Response(
                                            ResponseType.ERROR,
                                            new ErrorResponseContent()
                                    ),
                                    clientSocket
                            );
                        }
                    } else if (request.requestType().equals(RequestType.REFRESH_LOBBY)) {
                        RefreshLobbyRequestContent joinLobbyRequestContent = (RefreshLobbyRequestContent) request.content();
                        int lobbyId = joinLobbyRequestContent.getLobbyId();
                        GameLobby gameLobby = GameLobbies.getGameLobby(lobbyId);
                        if (gameLobby != null) {

                            if (gameLobby.isFull()) {
                                serverProtocolService.send(
                                        new Response(
                                                ResponseType.ERROR,
                                                new ErrorResponseContent()
                                        ),
                                        clientSocket
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
                                    clientSocket
                            );
                        } else {
                            serverProtocolService.send(
                                    new Response(
                                            ResponseType.ERROR,
                                            new ErrorResponseContent()
                                    ),
                                    clientSocket
                            );
                        }
                    }
                }
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}