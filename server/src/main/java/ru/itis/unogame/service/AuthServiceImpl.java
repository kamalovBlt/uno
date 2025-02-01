package ru.itis.unogame.service;

import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.unogame.exception.LoginException;
import ru.itis.unogame.exception.RegisterException;
import ru.itis.unogame.model.User;
import ru.itis.unogame.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Value("${salt.round}")
    private int saltRound;

    @Override
    public int login(String username, String password) throws LoginException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new LoginException("User with username: " + username + " do not exist"));
        if (!BCrypt.checkpw(password, user.password())) {
            throw new LoginException("Incorrect password");
        }
        return user.id();
    }

    @Override
    public int register(String username, String password) throws RegisterException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RegisterException("Username: " + username + " already exist");
        }
        return userRepository.save(username, BCrypt.hashpw(password, BCrypt.gensalt(saltRound)));
    }
}
