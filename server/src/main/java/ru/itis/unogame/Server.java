package ru.itis.unogame;

import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.CreateLobbyRequestContent;
import ru.itis.request.content.SignInRequestContent;
import ru.itis.request.content.SignUpRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.LobbyIdResponseContent;
import ru.itis.response.content.SignInResponseContent;
import ru.itis.response.content.SignUpResponseContent;
import ru.itis.service.ServerProtocolService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

public class Server {
    public static void main(String[] args) throws SQLException, IOException {
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
                        serverProtocolService.send(
                                new Response(
                                        ResponseType.LOBBY_ID,
                                        new LobbyIdResponseContent(random.nextInt(0, 100))
                                ),
                                clientSocket
                        );
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