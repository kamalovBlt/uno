package ru.itis.unogame.repository;


import ru.itis.unogame.model.User;

import java.util.Optional;

public interface UserRepository {

    int save(String username, String password);
    Optional<User> findById(int id);
    Optional<User> findByUsername(String username);

}
