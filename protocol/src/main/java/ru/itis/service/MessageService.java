package ru.itis.service;

import java.net.Socket;
import java.util.Optional;

public interface MessageService<T, K> {
    void send(T message, Socket socket);
    Optional<K> read(Socket socket);
}
