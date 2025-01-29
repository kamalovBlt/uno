package ru.itis.unogame;

import ru.itis.request.Request;
import ru.itis.request.RequestType;
import ru.itis.request.content.SignInRequestContent;
import ru.itis.response.Response;
import ru.itis.response.ResponseType;
import ru.itis.response.content.SignInResponseContent;
import ru.itis.service.ServerProtocolService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Optional;

public class Server {
    public static void main(String[] args) throws SQLException, IOException {

        /*DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
        databaseConfig.getDataSource().getConnection();*/

        try(ServerSocket serverSocket = new ServerSocket(11111)) {
            Socket clientSocket = serverSocket.accept();
            while(true) {
                ServerProtocolService serverProtocolService = new ServerProtocolService();
                Optional<Request> read = serverProtocolService.read(clientSocket);
                if(read.isPresent()) {
                    Request request = read.get();
                    if (request.requestType().equals(RequestType.SIGN_IN)) {
                        SignInRequestContent signInRequestContent = (SignInRequestContent) request.content();
                        System.out.println(signInRequestContent.getUsername());
                        System.out.println(signInRequestContent.getPassword());
                        serverProtocolService.send(
                                new Response(
                                        ResponseType.SIGN_IN,
                                        new SignInResponseContent(123)
                                ),
                                clientSocket
                        );
                    }
                }

                /*Тут обрабатываешь типы request и отправляешь response*/
                /*serverProtocolService.send(new Response());*/
                /*Каждому пользователю давать ID
                * Потом работа только с ним
                * У каждой комнаты будет ID и т.д.
                * Типы сообщений добавлять с помошью имплементации от Content
                * Если пустое сообщение, то отправляй просто content
                * */
            }
        }

    }
}
