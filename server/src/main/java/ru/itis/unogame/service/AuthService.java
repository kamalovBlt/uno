package ru.itis.unogame.service;

import ru.itis.unogame.exception.LoginException;
import ru.itis.unogame.exception.RegisterException;

public interface AuthService {
    int login(String username, String password) throws LoginException;
    int register(String username, String password) throws RegisterException;
}
