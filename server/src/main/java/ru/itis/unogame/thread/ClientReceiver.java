package ru.itis.unogame.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
@Component
public class ClientReceiver extends Thread {

    @Value("${server.port}")
    private int port;
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Server started on port {}", port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                log.info("New client connected: {}", clientSocket.getInetAddress());
                UserThread userThread = getUserThread();
                userThread.setSocket(clientSocket);
                userThread.start();
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Lookup
    public UserThread getUserThread() {
        return null;
    }
}
