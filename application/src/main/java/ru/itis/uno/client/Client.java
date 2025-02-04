package ru.itis.uno.client;

import lombok.Getter;
import lombok.Setter;
import ru.itis.service.ClientProtocolService;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class Client {

    private static volatile Client instance;

    private final static String HOST = "localhost";
    private final static int PORT = 11111;

    private final Socket socket;
    private final ClientProtocolService clientProtocolService;

    private int id;
    private String username;
    private int currentGameId;

    private Client () {
        try {

            socket = new Socket(HOST, PORT);
            clientProtocolService = new ClientProtocolService();
            id = SessionManager.getId();
            username = SessionManager.getUsername();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Client getInstance () {
        if (instance == null) {
            synchronized (Client.class) {
                if (instance == null) {
                    instance = new Client();
                }
            }
        }
        return instance;
    }

}
