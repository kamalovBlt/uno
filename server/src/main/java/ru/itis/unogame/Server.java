package ru.itis.unogame;

import ru.itis.request.Request;
import ru.itis.service.ServerProtocolService;
import ru.itis.unogame.config.DatabaseConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

public class Server {
    public static void main(String[] args) throws SQLException, IOException {

        DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
        databaseConfig.getDataSource().getConnection();

        try(ServerSocket serverSocket = new ServerSocket(11111)) {
            while(true) {
                Socket clientSocket = serverSocket.accept();
                ServerProtocolService serverProtocolService = new ServerProtocolService();
                Optional<Request> read = serverProtocolService.read(clientSocket);

                /*Тут обрабатываешь типы request и отправляешь response*/
                /*serverProtocolService.send(new Response());*/
                /*Каждого пользователя определять по ID
                * У каждой комнаты будет ID и т.д.
                * Типы сообщений добавлять с помошью имплементации от Content*/
            }
        }

    }
}
