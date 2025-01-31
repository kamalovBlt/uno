package ru.itis.uno.services;

import ru.itis.uno.client.SessionManager;

public class AuthenticationService {

    public boolean checkAuthentication() {
        return SessionManager.getId() != 0;
    }

}
